package br.edu.ifpb.ajudemais.asycnTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.springframework.web.client.RestClientException;

import br.edu.ifpb.ajudemais.asyncTasks.AsyncResponse;
import br.edu.ifpb.ajudemais.domain.Doador;
import br.edu.ifpb.ajudemais.remoteServices.DoadorRemoteService;
import br.edu.ifpb.ajudemais.utils.CustomToast;
import br.edu.ifpb.ajudemais.utils.ProgressDialog;

/**
 * <p>
 * <b>br.edu.ifpb.ajudemais.asyncTasks</b>
 * </p>
 * <p>
 * <p>
 * Asycn Task para Atualizar um Doador
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */
public class UpdateDoadorTask extends AsyncTask<Void, Void, Doador> {

    /**
     *
     */
    public AsyncResponse<Doador> delegate;
    private String message = null;
    private ProgressDialog progressDialog;
    private DoadorRemoteService doadorRemoteService;
    private Context context;
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
    public UpdateDoadorTask(Context context, Doador doador) {
        this.context = context;
        this.doador = doador;
        this.progressDialog = new ProgressDialog(context);
        this.doadorRemoteService = new DoadorRemoteService(context);
    }

    /**
     * @param params
     * @return
     */
    @Override
    protected Doador doInBackground(Void... params) {
        try {
            Log.i("Ajudemais", doador.toString());

            doador = doadorRemoteService.updateDoador(doador);

            return  doador;
        } catch (RestClientException e) {
            message = e.getMessage();
            e.printStackTrace();
        } catch (Exception e) {
            message = e.getMessage();
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param doadorUpdated
     */
    @Override
    protected void onPostExecute(Doador doadorUpdated) {
        progressDialog.dismissProgressDialog();

        if (doadorUpdated != null) {
            delegate.processFinish(doadorUpdated);
        }

        if (message != null) {
            CustomToast.getInstance(context).createSuperToastSimpleCustomSuperToast(message);
        }
    }

    public void setProgressAtivo(boolean progressAtivo) {
        this.progressAtivo = progressAtivo;
    }

}
