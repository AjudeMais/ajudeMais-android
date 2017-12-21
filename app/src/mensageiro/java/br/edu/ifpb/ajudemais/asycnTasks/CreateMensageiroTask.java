package br.edu.ifpb.ajudemais.asycnTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.springframework.web.client.RestClientException;

import br.edu.ifpb.ajudemais.asyncTasks.AsyncResponse;
import br.edu.ifpb.ajudemais.domain.Conta;
import br.edu.ifpb.ajudemais.domain.Mensageiro;
import br.edu.ifpb.ajudemais.enumarations.Grupo;
import br.edu.ifpb.ajudemais.remoteServices.AuthRemoteService;
import br.edu.ifpb.ajudemais.remoteServices.MensageiroRemoteService;
import br.edu.ifpb.ajudemais.utils.ProgressDialog;

/**
 * <p>
 * <b>{@link CreateMensageiroTask}</b>
 * </p>
 * <p>
 * <p>
 * Asycn Task para executar operação de criação de Mensageiro
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */
public class CreateMensageiroTask extends AsyncTask<Void, Void, Mensageiro> {

    private String message;
    private Mensageiro mensageiro;
    private String password;
    private MensageiroRemoteService mensageiroRemoteService;
    private AuthRemoteService authRemoteService;
    private ProgressDialog progressDialog;
    private Context context;
    public AsyncResponse<Mensageiro> delegate = null;

    public CreateMensageiroTask(Context context, Mensageiro mensageiro) {
        this.context = context;
        this.mensageiro = mensageiro;
        this.mensageiroRemoteService = new MensageiroRemoteService(context);
        this.authRemoteService = new AuthRemoteService(context);
        this.progressDialog = new ProgressDialog(context);
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
            password = mensageiro.getConta().getSenha();
            mensageiro = mensageiroRemoteService.saveMensageiro(mensageiro);
            Conta conta = authRemoteService.createAuthenticationToken(new Conta(mensageiro.getConta().getUsername(), password), Grupo.MENSAGEIRO);

            return mensageiro;

        } catch (RestClientException e) {
            message = e.getMessage();
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * @param mensageiroSaved
     */
    @Override
    protected void onPostExecute(Mensageiro mensageiroSaved) {
        progressDialog.dismissProgressDialog();

        if (mensageiroSaved != null) {
            delegate.processFinish(mensageiroSaved);
        } else {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }

    }
}
