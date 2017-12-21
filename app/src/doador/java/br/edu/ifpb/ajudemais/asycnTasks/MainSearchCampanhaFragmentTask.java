package br.edu.ifpb.ajudemais.asycnTasks;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;

import org.springframework.web.client.RestClientException;

import java.util.List;

import br.edu.ifpb.ajudemais.asyncTasks.AsyncResponse;
import br.edu.ifpb.ajudemais.domain.Campanha;
import br.edu.ifpb.ajudemais.dto.LatLng;
import br.edu.ifpb.ajudemais.remoteServices.CampanhaRemoteService;
import br.edu.ifpb.ajudemais.storage.SharedPrefManager;
import br.edu.ifpb.ajudemais.utils.AndroidUtil;
import br.edu.ifpb.ajudemais.utils.CustomToast;
import br.edu.ifpb.ajudemais.utils.ProgressDialog;

/**
 * <p>
 * <b>MainSearchCampanhaFragmentTask</b>
 * </p>
 * <p>
 * Task para busca de campanhas na API.
 * <p>
 * <p>
 * </p>
 *
 * @author <a href="https://github.com/FranckAJ">Franck Aragão</a>
 */
public class MainSearchCampanhaFragmentTask extends AsyncTask<Void, Void, List<Campanha>> {

    private CampanhaRemoteService campanhaRemoteService;
    private String message = null;
    private List<Campanha> campanhasResult;
    private SharedPrefManager sharedPrefManager;

    private LatLng latLng;
    private Location mLastLocation;
    private AndroidUtil androidUtil;
    private Context context;
    public AsyncResponse<List<Campanha>> delegate;
    private ProgressDialog progressDialog;


    public MainSearchCampanhaFragmentTask(Context context) {
        this.context = context;
        campanhaRemoteService = new CampanhaRemoteService(context);
        sharedPrefManager = new SharedPrefManager(context);
        progressDialog = new ProgressDialog(context);
        androidUtil = new AndroidUtil(context);

    }

    /**
     * @param voids
     * @return
     */
    @Override
    protected List<Campanha> doInBackground(Void... voids) {
        try {

            latLng = sharedPrefManager.getLocation();

            mLastLocation = getUpdateLocation();
            if (mLastLocation != null) {
                latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            }

            if (latLng != null) {
                campanhasResult = campanhaRemoteService.filterByInstituicaoLocal(latLng);
            } else {
                campanhasResult = campanhaRemoteService.findByStatusAtivo();
            }
        } catch (RestClientException e) {
            message = e.getMessage();
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return campanhasResult;
    }

    /**
     * @param result
     */
    @Override
    protected void onPostExecute(List<Campanha> result) {
        if (result != null) {
            delegate.processFinish(result);
        } else {
            CustomToast.getInstance(context).createSuperToastSimpleCustomSuperToast(message);
        }
    }

    /**
     * Pega a Localização atual do device.
     *
     * @return
     */
    private Location getUpdateLocation() {
        LocationManager locationManager = (LocationManager) this.context.getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        }
        return mLastLocation;
    }
}
