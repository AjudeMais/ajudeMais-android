package br.edu.ifpb.ajudemais.asyncTasks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import org.springframework.web.client.RestClientException;

import br.edu.ifpb.ajudemais.R;
import br.edu.ifpb.ajudemais.activities.LoginActivity;
import br.edu.ifpb.ajudemais.domain.Conta;
import br.edu.ifpb.ajudemais.dto.ChangePasswordDTO;
import br.edu.ifpb.ajudemais.remoteServices.ContaRemoteService;
import br.edu.ifpb.ajudemais.storage.SharedPrefManager;
import br.edu.ifpb.ajudemais.utils.AndroidUtil;
import br.edu.ifpb.ajudemais.utils.CapturePhotoUtils;
import br.edu.ifpb.ajudemais.utils.CustomToast;
import br.edu.ifpb.ajudemais.utils.ProgressDialog;

/**
 * <p>
 * <b>{@link ChangePasswordTask}</b>
 * </p>
 * <p>
 * <p>
 * Auxiliar Task para mudar a senha do usuário
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */
public class ChangePasswordTask extends AsyncTask<Void, Void, Conta> {

    private ContaRemoteService contaRemoteService;
    private String message = null;
    private String password;
    private String newPassword;
    private ProgressDialog progressDialog;
    private Context context;
    private AndroidUtil androidUtil;
    private CapturePhotoUtils capturePhotoUtils;

    public ChangePasswordTask(Context context, String password, String newPassword) {
        this.password = password;
        this.newPassword = newPassword;
        this.context = context;
        androidUtil = new AndroidUtil(context);
        this.capturePhotoUtils = new CapturePhotoUtils(context);
    }

    /**
     *
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.showProgressDialog();
        contaRemoteService = new ContaRemoteService(context);
    }

    /**
     * @param params
     * @return
     */
    @Override
    protected Conta doInBackground(Void... params) {
        try {

            contaRemoteService.changePassword(new ChangePasswordDTO(password, newPassword));

        } catch (RestClientException e) {
            message = e.getMessage();
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Conta conta) {
        progressDialog.dismissProgressDialog();
        if (message != null) {
            CustomToast.getInstance(context).createSuperToastSimpleCustomSuperToast(message);
        } else {
            Intent intent = new Intent(context, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            ((Activity) context).finish();
            CustomToast.getInstance(context).createSuperToastSimpleCustomSuperToast(context.getString(R.string.passwordUpdated));
            SharedPrefManager.getInstance(context).clearSharedPrefs();
            capturePhotoUtils.deleteImageProfile();
        }
    }
}
