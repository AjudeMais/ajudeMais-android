package br.edu.ifpb.ajudemais.asycnTasks;

import android.content.Context;
import android.os.AsyncTask;

import org.springframework.web.client.RestClientException;

import br.edu.ifpb.ajudemais.asyncTasks.AsyncResponse;
import br.edu.ifpb.ajudemais.domain.DonativoCampanha;
import br.edu.ifpb.ajudemais.remoteServices.DonativoRemoteService;
import br.edu.ifpb.ajudemais.utils.CustomToast;
import br.edu.ifpb.ajudemais.utils.ProgressDialog;

/**
 * <p>
 * <b>{@link GetDonativoCampanhaByDonativoIdTask}/b>
 * </p>
 * <p>
 * <p>
 * Asycn task para buscar donativoCampanha pelo id do donativo
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */


public class GetDonativoCampanhaByDonativoIdTask extends AsyncTask<Void, Void, DonativoCampanha> {


    /**
     *
     */
    public AsyncResponse<DonativoCampanha> delegate;
    private String message = null;
    private ProgressDialog progressDialog;
    private DonativoRemoteService donativoRemoteService;
    private Context context;
    private Long idDonativo;
    private boolean progressAtivo = true;


    /**
     *
     */
    @Override
    protected void onPreExecute() {
        if (progressAtivo) {
            progressDialog.showProgressDialog();
        }
        super.onPreExecute();

    }


    /**
     * @param context
     */
    public GetDonativoCampanhaByDonativoIdTask(Context context, Long idDonativo) {
        this.context = context;
        this.idDonativo = idDonativo;
        this.progressDialog = new ProgressDialog(context);
        this.donativoRemoteService = new DonativoRemoteService(context);
    }

    /**
     * @param params
     * @return
     */
    @Override
    protected DonativoCampanha doInBackground(Void... params) {
        try {
            return donativoRemoteService.findDonativoCampanhaByDonativoId(idDonativo);
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
     * @param donativoCampanha
     */
    @Override
    protected void onPostExecute(DonativoCampanha donativoCampanha) {
        progressDialog.dismissProgressDialog();

        if (donativoCampanha != null) {
            delegate.processFinish(donativoCampanha);
        }
    }

    public void setProgressAtivo(boolean progressAtivo) {
        this.progressAtivo = progressAtivo;
    }

}
