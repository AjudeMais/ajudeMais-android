package br.edu.ifpb.ajudemais.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import br.edu.ifpb.ajudemais.R;
import br.edu.ifpb.ajudemais.asyncTasks.AsyncResponse;
import br.edu.ifpb.ajudemais.asyncTasks.LoginTask;
import br.edu.ifpb.ajudemais.domain.Conta;
import br.edu.ifpb.ajudemais.storage.SharedPrefManager;
import br.edu.ifpb.ajudemais.util.NotificationRedirectUtil;

/**
 * <p>
 * <b>ApresentationActivity</b>
 * </p>
 * <p>
 * Activity para controlar tele inicial de carregamento do aplicativo.
 * <p>
 * <p>
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */
public class ApresentationActivity extends BaseActivity {


    private LoginTask loginTask;
    private NotificationRedirectUtil notificationRedirectUtil;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apresentation);
        notificationRedirectUtil = new NotificationRedirectUtil(this);
        init();

        ProgressBar mBar = (ProgressBar) findViewById(R.id.progress_presentation);
        mBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#FFFFFF"),
                android.graphics.PorterDuff.Mode.MULTIPLY);

        executeLoginTask();
    }

    @Override
    public void init() {
        initProperties();
    }

    /**
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.apresentation_menu, menu);
        return true;
    }

    /**
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_about || super.onOptionsItemSelected(item);
    }


    /**
     * Executa asycn Task para renovar login doador.
     */
    private void executeLoginTask() {
        if (SharedPrefManager.getInstance(this).getUser() != null) {
            loginTask = new LoginTask(this);
            loginTask.delegate = new AsyncResponse<Conta>() {
                @Override
                public void processFinish(Conta conta) {
                    if (conta != null) {
                        redirectNotification();
                        Intent intent = new Intent();
                        intent.setClass(ApresentationActivity.this, MainActivity.class);
                        intent.putExtra("Conta", conta);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();

                    } else {
                       goToLoginActivity();
                    }
                }
            };

            loginTask.execute();
        }else {
            goToLoginActivity();
        }
    }

    /**
     * Redirect for activity Login
     */
    private void goToLoginActivity(){
        if (AccessToken.getCurrentAccessToken() != null) {
            LoginManager.getInstance().logOut();
            SharedPrefManager.getInstance(ApresentationActivity.this).clearSharedPrefs();
            capturePhotoUtils.deleteImageProfile();
        }
        Intent intent = new Intent();
        intent.setClass(ApresentationActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }


    private void redirectNotification() {

        Intent startingIntent = getIntent();
        if (startingIntent != null) {
            String tipoAs = startingIntent.getStringExtra("tipo");
            String idStr = startingIntent.getStringExtra("id");

            if (tipoAs != null && idStr != null) {
                Long id = Long.parseLong(idStr);
                notificationRedirectUtil.redirectNotification(id, tipoAs);
            }
        }
    }


}

