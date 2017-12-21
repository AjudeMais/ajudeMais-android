package br.edu.ifpb.ajudemais.utils;

import android.content.Context;

import br.edu.ifpb.ajudemais.R;

/**
 * <p>
 * <b>{@link ProgressDialog}</b>
 * </p>
 * <p>
 * <p>
 * Auxilia a criação, exibição e finalização de ProgressDialog nas operações ao qual se necessite esperar.
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */

public class ProgressDialog {

    private Context context;
    private android.app.ProgressDialog progressDialog;


    public ProgressDialog(Context context){
        this.context = context;
    }

    /**
     * Cria ProgressDialog e o executa
     */
    public void showProgressDialog() {
        if (this.progressDialog == null) {
            this.progressDialog = new android.app.ProgressDialog(context);
            this.progressDialog.setIndeterminate(true);
            this.progressDialog.setCancelable(false);
        }

        this.progressDialog.setMessage(context.getResources().getString(R.string.msgDialogLoading));
        this.progressDialog.show();
    }

    /**
     * Finaliza ProgressDialog em execução
     */
    public void dismissProgressDialog() {
        if (this.progressDialog != null) {
            this.progressDialog.dismiss();
        }
    }


}
