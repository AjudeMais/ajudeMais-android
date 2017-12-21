package br.edu.ifpb.ajudemais.asycnTasks;

import android.content.Context;
import android.os.AsyncTask;

import org.springframework.web.client.RestClientException;

import br.edu.ifpb.ajudemais.asyncTasks.AsyncResponse;
import br.edu.ifpb.ajudemais.domain.Doador;
import br.edu.ifpb.ajudemais.dto.LatLng;
import br.edu.ifpb.ajudemais.remoteServices.DoadorRemoteService;
import br.edu.ifpb.ajudemais.utils.CustomToast;
import br.edu.ifpb.ajudemais.utils.ProgressDialog;

/**
 * <p>
 * <b>br.edu.ifpb.ajudemais.asyncTasks.UpdateLocationDoadorTask</b>
 * </p>
 * <p>
 * <p>
 * Asycn Task para Atualizar localizacao de um Doador
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */
public class UpdateLocationDoadorTask extends AsyncTask<Void, Void, Void> {

    /**
     *
     */
    public AsyncResponse<LatLng> delegate;
    private String message = null;
    private ProgressDialog progressDialog;
    private DoadorRemoteService doadorRemoteService;
    private Context context;
    private LatLng latLng;
    private Long doadorId;
    private Doador doador;
    private boolean progressAtivo = true;


    /**
     *
     */
    @Override
    protected void onPreExecute() {
        if (progressAtivo) {
            progressDialog.showProgressDialog();
        }
        super.onPreExecute();

    }


    /**
     * @param context
     */
    public UpdateLocationDoadorTask(Context context, LatLng latLng, Long doadorId) {
        this.context = context;
        this.latLng = latLng;
        this.doadorId = doadorId;
        this.progressDialog = new ProgressDialog(context);
        this.doadorRemoteService = new DoadorRemoteService(context);
    }

    /**
     * @param params
     * @return
     */
    @Override
    protected Void doInBackground(Void... params) {
        try {
            doadorRemoteService.updateLocationDoador(latLng, doadorId);
        } catch (RestClientException e) {
            message = e.getMessage();
            e.printStackTrace();
        } catch (Exception e) {
            message = e.getMessage();
            e.printStackTrace();
        }

        return null;
    }

    public void setProgressAtivo(boolean progressAtivo) {
        this.progressAtivo = progressAtivo;
    }

}
