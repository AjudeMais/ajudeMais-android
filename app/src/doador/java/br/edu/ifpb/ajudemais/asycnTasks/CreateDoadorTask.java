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
 * <b>{@link CreateDoadorTask}</b>
 * </p>
 * <p>
 * <p>
 * Asycn Task para criar Doador
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */
public class CreateDoadorTask extends AsyncTask<Void, Void, Doador> {

    private String message;
    private Doador doador;
    private String password;
    private DoadorRemoteService doadorRemoteService;
    private AuthRemoteService authRemoteService;
    private ProgressDialog progressDialog;
    private Context context;
    public AsyncResponse<Doador> delegate;

    public CreateDoadorTask(Context context, Doador doador) {
        this.context = context;
        this.doador = doador;
        this.doadorRemoteService = new DoadorRemoteService(context);
        this.authRemoteService = new AuthRemoteService(context);
        this.progressDialog = new ProgressDialog(context);
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.showProgressDialog();
    }

    @Override
    protected Doador doInBackground(Void... params) {
        try {
            password = doador.getConta().getSenha();
            doador = doadorRemoteService.saveDoador(doador);
            authRemoteService.createAuthenticationToken(new Conta(doador.getConta().getUsername(), password), Grupo.DOADOR);
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

    @Override
    protected void onPostExecute(Doador doador) {
        progressDialog.dismissProgressDialog();
        if (doador != null) {
            delegate.processFinish(doador);
        }

        if (message != null){
            CustomToast.getInstance(context).createSuperToastSimpleCustomSuperToast(message);
        }
    }
}
