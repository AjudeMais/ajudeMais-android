package br.edu.ifpb.ajudemais.asycnTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.view.Gravity;
import android.widget.Toast;

import org.springframework.web.client.RestClientException;

import br.edu.ifpb.ajudemais.asyncTasks.AsyncResponse;
import br.edu.ifpb.ajudemais.domain.Mensageiro;
import br.edu.ifpb.ajudemais.remoteServices.MensageiroRemoteService;
import br.edu.ifpb.ajudemais.utils.ProgressDialog;

/**
 * <p>
 * <b>{@link UpdateMensageiroTask}</b>
 * </p>
 * <p>
 * <p>
 * AsycnTask para atualizar mensageiro
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */


public class UpdateMensageiroTask extends AsyncTask<Void, Void, Mensageiro> {

    private String message;
    private Mensageiro mensageiro;
    private MensageiroRemoteService mensageiroRemoteService;
    private Mensageiro mensageiroUpdated;
    private Toast toast;
    private ProgressDialog progressDialog;
    private Context context;
    public AsyncResponse<Mensageiro> delegate = null;
    private boolean progressAtivo = true;


    public UpdateMensageiroTask(Context context, Mensageiro mensageiro) {
        this.mensageiro = mensageiro;
        this.context = context;
        mensageiroRemoteService = new MensageiroRemoteService(context);
    }

    @Override
    protected void onPreExecute() {
        if (progressAtivo) {
            progressDialog = new ProgressDialog(context);
            progressDialog.showProgressDialog();
        }

    }

    @Override
    protected Mensageiro doInBackground(Void... params) {
        try {
            mensageiroUpdated = mensageiroRemoteService.updateMensageiro(mensageiro);
        } catch (RestClientException e) {
            message = e.getMessage();
        }
        return mensageiroUpdated;
    }

    @Override
    protected void onPostExecute(Mensageiro mensageiro) {
        progressDialog.dismissProgressDialog();
        if (mensageiroUpdated != null) {
            delegate.processFinish(mensageiroUpdated);
        } else {
            toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM, 0, 0);
            toast.show();
        }
    }

    public void setProgressAtivo(boolean progressAtivo) {
        this.progressAtivo = progressAtivo;
    }
}

