package br.edu.ifpb.ajudemais.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.springframework.web.client.RestClientException;

import br.edu.ifpb.ajudemais.asyncTasks.AsyncResponse;
import br.edu.ifpb.ajudemais.domain.Donativo;
import br.edu.ifpb.ajudemais.remoteServices.DonativoRemoteService;
import br.edu.ifpb.ajudemais.utils.CustomToast;
import br.edu.ifpb.ajudemais.utils.ProgressDialog;


/**
 * <p>
 * <b>{@link UpdateEstadoDonativoTask}</b>
 * </p>
 * <p>
 * <p>
 * Asycn para atualizar estado de uma doação.
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */
public class UpdateEstadoDonativoTask extends AsyncTask<Void, Void, Donativo> {

    private String message;
    private DonativoRemoteService donativoRemoteService;
    private Donativo donativo;
    private Context context;
    public AsyncResponse<Donativo> delegate;
    private ProgressDialog progressDialog;

    public UpdateEstadoDonativoTask(Context context, Donativo donativo) {
        this.context = context;
        this.progressDialog = new ProgressDialog(context);
        this.donativoRemoteService = new DonativoRemoteService(context);
        this.donativo = donativo;
    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
        progressDialog.showProgressDialog();
    }


    @Override
    protected Donativo doInBackground(Void... params) {
        try {
            return donativoRemoteService.updateDonativo(donativo);
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
    protected void onPostExecute(Donativo donativo) {
        progressDialog.dismissProgressDialog();
        if (donativo != null){
            delegate.processFinish(donativo);
        }

        if (message != null){
            CustomToast.getInstance(context).createSuperToastSimpleCustomSuperToast(message);
        }
    }
}
