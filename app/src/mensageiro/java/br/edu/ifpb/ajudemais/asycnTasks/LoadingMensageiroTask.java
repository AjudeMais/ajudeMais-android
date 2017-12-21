package br.edu.ifpb.ajudemais.asycnTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.springframework.web.client.RestClientException;
import br.edu.ifpb.ajudemais.asyncTasks.AsyncResponse;
import br.edu.ifpb.ajudemais.domain.Mensageiro;
import br.edu.ifpb.ajudemais.remoteServices.MensageiroRemoteService;

/**
 * <p>
 * <b>br.edu.ifpb.ajudemais.asycnTasks</b>
 * </p>
 * <p>
 * <p>
 * AsycnTask que carrega um mensageiro pelo o username.
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */


public class LoadingMensageiroTask extends AsyncTask<Void, Void, Mensageiro>{

    public AsyncResponse<Mensageiro> delegate = null;
    private MensageiroRemoteService mensageiroRemoteService;
    private String message;
    private Mensageiro mensageiro;
    private Context context;
    private String userName;

    public LoadingMensageiroTask(Context context, String usermaneMensageiro){
        this.context = context;
        this.userName = usermaneMensageiro;
    }

    /**
     *
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mensageiroRemoteService = new MensageiroRemoteService(context);
    }

    /**
     * @param params
     * @return
     */
    @Override
    protected Mensageiro doInBackground(Void... params) {
        try {
            mensageiro = mensageiroRemoteService.getMensageiro(userName);
        } catch (RestClientException e) {
            message = e.getMessage();
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mensageiro;
    }

    /**
     * @param mensageiro
     */
    @Override
    protected void onPostExecute(Mensageiro mensageiro) {
        if (mensageiro != null && message == null) {
            delegate.processFinish(mensageiro);
        }else {
            android.widget.Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }
}
