package br.edu.ifpb.ajudemais.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;

import org.springframework.web.client.RestClientException;

import br.edu.ifpb.ajudemais.domain.Doador;
import br.edu.ifpb.ajudemais.domain.Imagem;
import br.edu.ifpb.ajudemais.domain.Mensageiro;
import br.edu.ifpb.ajudemais.remoteServices.DoadorRemoteService;
import br.edu.ifpb.ajudemais.remoteServices.ImagemStorageRemoteService;
import br.edu.ifpb.ajudemais.remoteServices.MensageiroRemoteService;
import br.edu.ifpb.ajudemais.utils.CustomToast;
import br.edu.ifpb.ajudemais.utils.ProgressDialog;

/**
 * <p>
 * <b>{@link UploadImageTask}</b>
 * </p>
 * <p>
 * <p>
 * Async Task para realização do upload de foto.
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */
public class UploadImageTask extends AsyncTask<Void, Void, Imagem> {

    /**
     *
     */
    public AsyncResponse<Imagem> delegate;

    private ImagemStorageRemoteService imagemStorageRemoteService;
    private String message;
    private Imagem imagem;
    private ProgressDialog progressDialog;
    private byte[] array;
    private Context context;
    private boolean isActiveProgress = true;


    public UploadImageTask(Context context, byte[] array) {
        this.array = array;
        this.context = context;
        progressDialog = new ProgressDialog(context);
        imagemStorageRemoteService = new ImagemStorageRemoteService(context);

    }

    /**
     *
     */
    @Override
    protected void onPreExecute() {
        if (isActiveProgress) {
            progressDialog.showProgressDialog();
        }
        super.onPreExecute();

    }


    /**
     * @param params
     * @return
     */
    @Override
    protected Imagem doInBackground(Void... params) {
        try {

            imagem = imagemStorageRemoteService.uploadImage(array);

        } catch (RestClientException e) {
            message = e.getMessage();
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imagem;
    }


    @Override
    protected void onPostExecute(Imagem imagem) {
        progressDialog.dismissProgressDialog();

        if (message != null) {
            CustomToast.getInstance(context).createSuperToastSimpleCustomSuperToast(message);
        }
        if (imagem != null) {
            delegate.processFinish(imagem);
        }

    }

    public void setActiveProgress(boolean activeProgress) {
        isActiveProgress = activeProgress;
    }
}
