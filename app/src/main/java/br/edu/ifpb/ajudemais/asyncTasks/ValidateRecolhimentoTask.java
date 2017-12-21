package br.edu.ifpb.ajudemais.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;

import org.springframework.web.client.RestClientException;

import br.edu.ifpb.ajudemais.remoteServices.DonativoRemoteService;
import br.edu.ifpb.ajudemais.utils.CustomToast;
import br.edu.ifpb.ajudemais.utils.ProgressDialog;

/**
 * <p>
 * <b>{@link ValidateRecolhimentoTask}</b>
 * </p>
 * <p>
 * <p>
 * Async Task para Verificar ser donativo está valido para ser recolhido.
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */
public class ValidateRecolhimentoTask extends AsyncTask<Void, Void, Boolean> {

    /**
     *
     */
    public AsyncResponse<Boolean> delegate;

    private DonativoRemoteService donativoRemoteService;
    private String message;
    private ProgressDialog progressDialog;
    private Long id;
    private Context context;


    public ValidateRecolhimentoTask(Context context, Long id) {
        this.id = id;
        this.context = context;
        progressDialog = new ProgressDialog(context);
        donativoRemoteService = new DonativoRemoteService(context);

    }

    /**
     *
     */
    @Override
    protected void onPreExecute() {
        progressDialog.showProgressDialog();
        super.onPreExecute();

    }


    /**
     * @param params
     * @return
     */
    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            return donativoRemoteService.isValidRecolhimentoByDonativo(id);
        } catch (RestClientException e) {
            message = e.getMessage();
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPostExecute(Boolean valid) {
        progressDialog.dismissProgressDialog();

        if (message != null) {
            CustomToast.getInstance(context).createSuperToastSimpleCustomSuperToast(message);
        }
        if (valid != null) {
            delegate.processFinish(valid);
        }

    }
}
