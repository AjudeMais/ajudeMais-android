package br.edu.ifpb.ajudemais.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.springframework.web.client.RestClientException;
import br.edu.ifpb.ajudemais.domain.Donativo;
import br.edu.ifpb.ajudemais.remoteServices.DonativoRemoteService;

/**
 * <p>
 * <b>{@Link GetDonativoByIdTask}</b>
 * </p>
 * <p>
 * <p>
 * Asycn task que recupera um donativo com base no id.
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */
public class GetDonativoByIdTask extends AsyncTask<Void, Void, Donativo>{

    private Long id;
    public AsyncResponse<Donativo> delegate = null;
    private Context context;
    private DonativoRemoteService donativoRemoteService;
    private String message = null;

    public GetDonativoByIdTask(Context context, Long id){
        this.context = context;
        this.id = id;
        donativoRemoteService = new DonativoRemoteService(context);
    }

    /**
     * @param params
     * @return
     */
    @Override
    protected Donativo doInBackground(Void... params) {
        try {
            Donativo donativo = donativoRemoteService.findByDonativoId(id);
            return donativo;
        } catch (RestClientException e) {
            message = e.getMessage();
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param donativo
     */
    @Override
    protected void onPostExecute(Donativo donativo) {
        if (message != null) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }

        if (donativo != null){
            delegate.processFinish(donativo);
        }
    }
}
