package br.edu.ifpb.ajudemais.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.springframework.web.client.RestClientException;

import br.edu.ifpb.ajudemais.domain.Campanha;
import br.edu.ifpb.ajudemais.remoteServices.CampanhaRemoteService;

/**
 * <p>
 * <b>{@link GetCampanhaByIdTask}</b>
 * </p>
 * <p>
 * <p>
 * {@link AsyncTask} para recuperar campanha por id.
 * </p>
 *
 * @author <a href="https://github.com/FranckAJ">Franck Aragão</a>
 */


public class GetCampanhaByIdTask extends AsyncTask<Void, Void, Campanha> {

    public AsyncResponse<Campanha> delegate = null;
    private String message = null;
    private CampanhaRemoteService campanhaRemoteService;
    private Context context;
    private Campanha campanha;
    private Long id;


    /**
     * @param context
     */
    public GetCampanhaByIdTask(Context context, Long id) {
        this.context = context;
        this.id = id;
        this.campanhaRemoteService = new CampanhaRemoteService(context);
    }

    /**
     * @param params
     * @return
     */
    @Override
    protected Campanha doInBackground(Void... params) {
        try {
            campanha = campanhaRemoteService.findById(id);
        } catch (RestClientException e) {
            message = e.getMessage();
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return campanha;
    }

    /**
     * @param campanha
     */
    @Override
    protected void onPostExecute(Campanha campanha) {
        if (message != null) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        } else {
            delegate.processFinish(campanha);
        }
    }
}
