package br.edu.ifpb.ajudemais.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;



import org.springframework.web.client.RestClientException;

import br.edu.ifpb.ajudemais.domain.Conta;
import br.edu.ifpb.ajudemais.remoteServices.AuthRemoteService;
import br.edu.ifpb.ajudemais.storage.SharedPrefManager;

/**
 * <p>
 * <b>{@link LoginTask}</b>
 * </p>
 * <p>
 * <p>
 * Asycn Task para login.
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */


public class LoginTask extends AsyncTask<Void, Void, Conta> {

    private Conta conta;
    private AuthRemoteService authRemoteService;
    private Context context;
    public AsyncResponse<Conta> delegate = null;

    public LoginTask(Context context) {
        this.context = context;
    }

    /**
     *
     */
    @Override
    protected void onPreExecute() {
        authRemoteService = new AuthRemoteService(context);
    }

    /**
     *
     * @param params
     * @return
     */
    @Override
    protected Conta doInBackground(Void... params) {
        try {

            if (authRemoteService.logged()) {

                conta = SharedPrefManager.getInstance(context).getUser();

                return conta;
            }

        } catch (RestClientException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     *
     * @param conta
     */
    @Override
    protected void onPostExecute(Conta conta) {
        delegate.processFinish(conta);
    }
}
