package br.edu.ifpb.ajudemais.asycnTasks;

import android.content.Context;
import android.os.AsyncTask;

import org.springframework.web.client.RestClientException;

import br.edu.ifpb.ajudemais.asyncTasks.AsyncResponse;
import br.edu.ifpb.ajudemais.domain.Conta;
import br.edu.ifpb.ajudemais.domain.Mensageiro;
import br.edu.ifpb.ajudemais.enumarations.Grupo;
import br.edu.ifpb.ajudemais.remoteServices.AuthRemoteService;
import br.edu.ifpb.ajudemais.remoteServices.MensageiroRemoteService;
import br.edu.ifpb.ajudemais.utils.CustomToast;
import br.edu.ifpb.ajudemais.utils.ProgressDialog;

/**
 * <p>
 * <b>{@link LoginMensageiroTask}</b>
 * </p>
 * <p>
 * <p>
 * Asycn Task para realização do Login em mensageiro
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */

public class LoginMensageiroTask extends AsyncTask<Void, Void, Mensageiro> {

    private String message = null;
    private AuthRemoteService authRemoteService;
    private MensageiroRemoteService mensageiroRemoteService;
    private ProgressDialog progressDialog;
    private Context context;
    public AsyncResponse<Mensageiro> delegate = null;

    private String username;
    private String senha;

    /**
     * @param context
     * @param username
     * @param senha
     */
    public LoginMensageiroTask(Context context, String username, String senha) {
        this.context = context;
        this.authRemoteService = new AuthRemoteService(context);
        this.progressDialog = new ProgressDialog(context);
        this.mensageiroRemoteService = new MensageiroRemoteService(context);
        this.username = username;
        this.senha = senha;
    }

    /**
     *
     */
    @Override
    protected void onPreExecute() {
        progressDialog.showProgressDialog();

    }

    /**
     * @param params
     * @return
     */
    @Override
    protected Mensageiro doInBackground(Void... params) {

        try {
            authRemoteService.createAuthenticationToken(new Conta(username, senha), Grupo.MENSAGEIRO);
            Mensageiro mensageiro = mensageiroRemoteService.getMensageiro(username);
            return mensageiro;

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
     * @param mensageiroLogado
     */
    @Override
    protected void onPostExecute(Mensageiro mensageiroLogado) {
        progressDialog.dismissProgressDialog();

        if (mensageiroLogado != null) {
            delegate.processFinish(mensageiroLogado);

        } else {
            CustomToast.getInstance(context).createSimpleCustomSuperToastActivity(message);
        }
    }

}
