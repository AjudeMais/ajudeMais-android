package br.edu.ifpb.ajudemais.asycnTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.springframework.web.client.RestClientException;

import br.edu.ifpb.ajudemais.asyncTasks.AsyncResponse;
import br.edu.ifpb.ajudemais.domain.Doador;
import br.edu.ifpb.ajudemais.remoteServices.DoadorRemoteService;
import br.edu.ifpb.ajudemais.utils.ProgressDialog;

/**
 * <p>
 * <b>{@link LoadingDoadorTask}</b>
 * </p>
 * <p>
 * <p>
 * AsycnTask para carregar Doador pelo username.
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */


public class LoadingDoadorTask extends AsyncTask<Void, Void, Doador> {

    private DoadorRemoteService doadorRemoteService;
    public AsyncResponse<Doador> delegate = null;
    private String message = null;
    private Doador doador;
    private ProgressDialog progressDialog;
    private Context context;
    private String username;
    private boolean progressAtivo = false;


    public LoadingDoadorTask(Context context, String usernameDoador) {
        this.username = usernameDoador;
        this.context = context;
        doadorRemoteService = new DoadorRemoteService(context);
        this.progressDialog = new ProgressDialog(context);

    }

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
     * @param params
     * @return
     */
    @Override
    protected Doador doInBackground(Void... params) {
        try {

            doador = doadorRemoteService.getDoador(username);

        } catch (RestClientException e) {
            message = e.getMessage();
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doador;
    }

    @Override
    protected void onPostExecute(Doador doador) {
        progressDialog.dismissProgressDialog();
        if (message == null) {
            delegate.processFinish(doador);
        }else
            android.widget.Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

    }
    public void setProgressAtivo(boolean progressAtivo) {
        this.progressAtivo = progressAtivo;
    }

}
