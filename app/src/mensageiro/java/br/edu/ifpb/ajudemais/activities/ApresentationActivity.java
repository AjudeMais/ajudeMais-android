package br.edu.ifpb.ajudemais.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import br.edu.ifpb.ajudemais.R;
import br.edu.ifpb.ajudemais.asyncTasks.AsyncResponse;
import br.edu.ifpb.ajudemais.asyncTasks.LoginTask;
import br.edu.ifpb.ajudemais.domain.Conta;
import br.edu.ifpb.ajudemais.util.NotificationRedirectUtil;


/**
 * <p>
 * <b>ApresentationActivity</b>
 * </p>
 * <p>
 *     Activity para controlar tele inicial de carregamento do aplicativo.
 * <p>
 *
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */
public class ApresentationActivity extends AppCompatActivity {

    private LoginTask loginTask;
    private NotificationRedirectUtil notificationRedirectUtil;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apresentation);
        notificationRedirectUtil = new NotificationRedirectUtil(this);

        ProgressBar mBar= (ProgressBar) findViewById(R.id.progress_presentation);
        mBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#FFFFFF"),
                android.graphics.PorterDuff.Mode.MULTIPLY);

        executeLoginTask();
    }

    /**
     * Executa asycn Task para renovar login Mensageiro.
     */
    private void executeLoginTask(){
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
                    Intent intent = new Intent();
                    intent.setClass(ApresentationActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

                }
            }
        };

        loginTask.execute();
    }


    /**
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.apresentation_menu, menu);
        return true;
    }

    /**
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_about || super.onOptionsItemSelected(item);
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
