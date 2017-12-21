package br.edu.ifpb.ajudemais.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.springframework.web.client.RestClientException;

import br.edu.ifpb.ajudemais.domain.Endereco;
import br.edu.ifpb.ajudemais.dto.LatLng;
import br.edu.ifpb.ajudemais.remoteServices.LocalizacaoRemoteService;
import br.edu.ifpb.ajudemais.utils.ProgressDialog;

/**
 * <p>
 * <b>{@link FindByMyLocationActualTask}</b>
 * </p>
 * <p>
 * <p>
 * {@link AsyncTask} para recuperar endreço com base na latitude e logintude do device.
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */


public class FindByMyLocationActualTask extends AsyncTask<Void, Void, Endereco> {


    /**
     *
     */
    public AsyncResponse<Endereco> delegate = null;
    private String message = null;
    private ProgressDialog progressDialog;
    private LocalizacaoRemoteService localizacaoRemoteService;
    private Context context;
    private Endereco endereco;
    private LatLng latLng;


    /**
     * @param context
     */
    public FindByMyLocationActualTask(Context context, LatLng latLng) {
        this.context = context;
        this.latLng = latLng;
        this.localizacaoRemoteService = new LocalizacaoRemoteService(context);
    }

    /**
     *
     */
    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(context);
        progressDialog.showProgressDialog();
        super.onPreExecute();


    }


    /**
     * @param params
     * @return
     */
    @Override
    protected Endereco doInBackground(Void... params) {
        try {

            endereco = localizacaoRemoteService.findByEnderecoActual(latLng);


        } catch (RestClientException e) {
            message = e.getMessage();
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return endereco;
    }

    /**
     * @param endereco
     */
    @Override
    protected void onPostExecute(Endereco endereco) {
        progressDialog.dismissProgressDialog();
        if (message != null) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        } else {
            delegate.processFinish(endereco);
        }
    }
}
