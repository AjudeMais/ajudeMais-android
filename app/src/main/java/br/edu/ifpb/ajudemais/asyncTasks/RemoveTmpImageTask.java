package br.edu.ifpb.ajudemais.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;

import org.springframework.web.client.RestClientException;

import br.edu.ifpb.ajudemais.remoteServices.ImagemStorageRemoteService;
import br.edu.ifpb.ajudemais.utils.CustomToast;
import br.edu.ifpb.ajudemais.utils.ProgressDialog;

/**
 * <p>
 * <b>{@link RemoveTmpImageTask}</b>
 * </p>
 * <p>
 * <p>
 * Asycn para remover imagens de temp que não serão mais utilizadas.
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */


public class RemoveTmpImageTask extends AsyncTask<Void, Void, Void>{

    private ProgressDialog progressDialog;
    private String imageName;
    private ImagemStorageRemoteService imagemStorageRemoteService;
    private String message;
    private Context context;

    public RemoveTmpImageTask(Context context, String imageName){
        this.context = context;
        this.imageName = imageName;
        this.progressDialog = new ProgressDialog(context);
        this.imagemStorageRemoteService = new ImagemStorageRemoteService(context);
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
     * Executa remote service que recupera um imagem.
     *
     * @param params
     * @return
     */
    @Override
    protected Void doInBackground(Void... params) {
        try {
          imagemStorageRemoteService.removeTmpImage(imageName);

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
    protected void onPostExecute(Void aVoid) {
        progressDialog.dismissProgressDialog();
        if (message != null){
            CustomToast.getInstance(context).createSuperToastSimpleCustomSuperToast(message);
        }
    }
}
