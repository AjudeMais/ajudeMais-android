package br.edu.ifpb.ajudemais.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifpb.ajudemais.R;
import br.edu.ifpb.ajudemais.asycnTasks.CreateDoadorTask;
import br.edu.ifpb.ajudemais.asyncTasks.AsyncResponse;
import br.edu.ifpb.ajudemais.domain.Doador;
import br.edu.ifpb.ajudemais.domain.FcmToken;
import br.edu.ifpb.ajudemais.storage.SharedPrefManager;
import br.edu.ifpb.ajudemais.utils.AndroidUtil;
import br.edu.ifpb.ajudemais.utils.CustomToast;

/**
 * <p>
 * <b>{@link CreateAccountHelperActivity}</b>
 * </p>
 * <p>
 * Activity para finalizar criação de conta utilizando o facebook
 * <p>
 * <p>
 * </p>
 *
 * @author <a href="https://github.com/amslv">Ana Silva</a>
 */
public class CreateAccountHelperActivity extends BaseActivity implements View.OnClickListener, Validator.ValidationListener {

    private Toolbar mToolbar;
    private Button btnFinalizeAccountCreation;
    private Resources resources;
    private Doador doador;
    private TextInputLayout ltEdtEmail;
    private Validator validator;

    @Order(1)
    @NotEmpty(messageResId = R.string.msgPhoneNotInformed, sequence = 1)
    @Length(min = 15, messageResId = R.string.msgPhoneNotCompleted, sequence = 2)
    private TextInputEditText edtTelefone;

    @Order(2)
    @Email(messageResId = R.string.msgInvalideEmail, sequence = 1)
    private TextInputEditText edtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_helper);

        validator = new Validator(this);
        validator.setValidationListener(this);
        init();
        if (doador.getConta().getEmail() != null) {
            editFieldsVisualization();
        }
        btnFinalizeAccountCreation.setOnClickListener(this);
    }

    private void editFieldsVisualization() {
        ltEdtEmail.setVisibility(View.GONE);
    }

    public void init() {
        initProperties();
        doador = (Doador) getIntent().getSerializableExtra("Doador");
        mToolbar = (Toolbar) findViewById(R.id.nav_action);
        mToolbar.setTitle(R.string.complete_account);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        resources = getResources();

        btnFinalizeAccountCreation = (Button) findViewById(R.id.btnFinalizeAccountCreation);
        edtTelefone = (TextInputEditText) findViewById(R.id.edtTelefone);
        androidUtil.setMaskPhone(edtTelefone);

        edtEmail = (TextInputEditText) findViewById(R.id.edtEmail);
        ltEdtEmail = (TextInputLayout) findViewById(R.id.ltEdtEmail);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnFinalizeAccountCreation) {
            validator.validate();
        }
    }

    @Override
    public void onValidationSucceeded() {
        List<String> grupos = new ArrayList<>();
        grupos.add("ROLE_DOADOR");
        FcmToken fcmToken = new FcmToken(FirebaseInstanceId.getInstance().getToken());
        doador.getConta().setGrupos(grupos);
        doador.setTokenFCM(fcmToken);
        doador.setTelefone(edtTelefone.getText().toString().trim());
        if (doador.getConta().getEmail() == null) {
            doador.getConta().setEmail(edtEmail.getText().toString().trim());
        }
        executeCreateDoadorTask(doador);
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            if (view instanceof TextInputEditText) {
                ((TextInputEditText) view).setError(message);
                view.requestFocus();
            } else {
                CustomToast.getInstance(CreateAccountHelperActivity.this).createSuperToastSimpleCustomSuperToast(message);
            }
        }
    }

    /**
     * Executada Doador de Task.
     *
     * @param doador
     */
    private void executeCreateDoadorTask(Doador doador) {
        CreateDoadorTask createDoadorTask = new CreateDoadorTask(this, doador);

        createDoadorTask.delegate = new AsyncResponse<Doador>() {
            @Override
            public void processFinish(Doador output) {
                SharedPrefManager.getInstance(CreateAccountHelperActivity.this).storeUser(output.getConta());
                Intent intent = new Intent();
                intent.setClass(CreateAccountHelperActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("Conta", output.getConta());
                startActivity(intent);
                finish();
            }
        };
        createDoadorTask.execute();
    }

    /**
     * Implementação para controlar operações na action bar
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (AccessToken.getCurrentAccessToken()!= null){
                    LoginManager.getInstance().logOut();
                }
                onBackPressed();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
