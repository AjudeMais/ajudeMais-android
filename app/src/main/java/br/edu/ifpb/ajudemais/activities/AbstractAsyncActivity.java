package br.edu.ifpb.ajudemais.activities;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;

import br.edu.ifpb.ajudemais.R;
import br.edu.ifpb.ajudemais.utils.AndroidUtil;

/**
 * <p>
 * <b>{@link AbstractAsyncActivity}</b>
 * </p>
 * <p>
 *     Activity base para reaproveitar métodos de carregamento de TASKS asycn
 * <p>
 *
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */
public class AbstractAsyncActivity extends AppCompatActivity implements AsyncActivity {

    protected static final String TAG = AbstractAsyncActivity.class.getSimpleName();

    private ProgressDialog progressDialog;

    private boolean destroyed = false;



    /**
     * Destroy
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.destroyed = true;
    }

    /**
     *
     */
    @Override
    public void showLoadingProgressDialog() {
        this.showProgressDialog(getResources().getString(R.string.msgDialogLoading));
    }


    /**
     *
     * @param message
     */
    @Override
    public void showProgressDialog(CharSequence message) {
        if (this.progressDialog == null) {
            this.progressDialog = new ProgressDialog(this);
            this.progressDialog.setIndeterminate(true);
        }

        this.progressDialog.setMessage(message);
        this.progressDialog.show();
    }

    /**
     *
     */
    @Override
    public void dismissProgressDialog() {
        if (this.progressDialog != null && !this.destroyed) {
            this.progressDialog.dismiss();
        }
    }
}
