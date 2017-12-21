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
import br.edu.ifpb.ajudemais.domain.Donativo;
import br.edu.ifpb.ajudemais.dto.LatLng;
import br.edu.ifpb.ajudemais.remoteServices.DonativoRemoteService;
import br.edu.ifpb.ajudemais.storage.SharedPrefManager;
import br.edu.ifpb.ajudemais.utils.AndroidUtil;
import br.edu.ifpb.ajudemais.utils.CustomToast;
import br.edu.ifpb.ajudemais.utils.ProgressDialog;

/**
 * Created by amsv on 08/07/17.
 */

public class MainSearchSolicitacoesFragmentTask extends AsyncTask<Void, Void, List<Donativo>> {

    private DonativoRemoteService donativoRemoteService;
    private String message = null;
    private List<Donativo> donativosResult;
    private SharedPrefManager sharedPrefManager;

    private LatLng latLng;
    private Location mLastLocation;
    private AndroidUtil androidUtil;
    private Context context;
    public AsyncResponse<List<Donativo>> delegate;
    private ProgressDialog progressDialog;

    public MainSearchSolicitacoesFragmentTask(Context context) {
        this.context = context;
        donativoRemoteService = new DonativoRemoteService(context);
        sharedPrefManager = new SharedPrefManager(context);
        progressDialog = new ProgressDialog(context);
        androidUtil = new AndroidUtil(context);
    }

    @Override
    protected List<Donativo> doInBackground(Void... params) {
        try {

            latLng = sharedPrefManager.getLocation();

            mLastLocation = getUpdateLocation();
            if (mLastLocation != null) {
                latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            }

            if (latLng != null) {
                donativosResult = donativoRemoteService.findByDoadorLocal(latLng);
            }
        } catch (RestClientException e) {
            message = e.getMessage();
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return donativosResult;
    }

    @Override
    protected void onPostExecute(List<Donativo> donativos) {
        if (donativos != null) {
            delegate.processFinish(donativos);
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
