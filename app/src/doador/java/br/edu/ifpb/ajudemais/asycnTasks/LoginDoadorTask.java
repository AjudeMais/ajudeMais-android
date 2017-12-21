package br.edu.ifpb.ajudemais.asycnTasks;

import android.content.Context;
import android.os.AsyncTask;

import org.springframework.web.client.RestClientException;

import br.edu.ifpb.ajudemais.asyncTasks.AsyncResponse;
import br.edu.ifpb.ajudemais.domain.Conta;
import br.edu.ifpb.ajudemais.domain.Doador;
import br.edu.ifpb.ajudemais.enumarations.Grupo;
import br.edu.ifpb.ajudemais.remoteServices.AuthRemoteService;
import br.edu.ifpb.ajudemais.remoteServices.DoadorRemoteService;
import br.edu.ifpb.ajudemais.utils.CustomToast;
import br.edu.ifpb.ajudemais.utils.ProgressDialog;

/**
 * <p>
 * <b>{@link LoginDoadorTask}</b>
 * </p>
 * <p>
 * <p>
 * Asycn Task para Login.
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */
public class LoginDoadorTask extends AsyncTask<Void, Void, Doador> {

    public AsyncResponse<Doador> delegate = null;
    private String message;
    private AuthRemoteService authRemoteService;
    private DoadorRemoteService doadorRemoteService;
    private Context context;
    private ProgressDialog progressDialog;
    private String username;
    private String senha;
    private boolean isLoginFacebook;

    public LoginDoadorTask(Context context, String username, String senha) {
        this.context = context;
        this.authRemoteService = new AuthRemoteService(context);
        this.progressDialog = new ProgressDialog(context);
        this.doadorRemoteService = new DoadorRemoteService(context);
        this.username = username;
        this.senha = senha;
    }

    @Override
    protected void onPreExecute() {
        progressDialog.showProgressDialog();
    }

    @Override
    protected Doador doInBackground(Void... params) {
        try {
            authRemoteService.createAuthenticationToken(new Conta(username, senha), Grupo.DOADOR);
            Doador doador = doadorRemoteService.getDoador(username);
            return doador;

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
     * @param doador
     */
    @Override
    protected void onPostExecute(Doador doador) {
        progressDialog.dismissProgressDialog();
        delegate.processFinish(doador);

        if (message != null && !isLoginFacebook) {
            CustomToast.getInstance(context).createSuperToastSimpleCustomSuperToast(message);
        }
    }

    /**
     *
     * @param loginFacebook
     */
    public void setLoginFacebook(boolean loginFacebook) {
        isLoginFacebook = loginFacebook;
    }
}
