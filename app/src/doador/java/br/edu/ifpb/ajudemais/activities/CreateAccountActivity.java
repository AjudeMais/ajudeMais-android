package br.edu.ifpb.ajudemais.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.iid.FirebaseInstanceId;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifpb.ajudemais.R;
import br.edu.ifpb.ajudemais.asycnTasks.CreateDoadorTask;
import br.edu.ifpb.ajudemais.asycnTasks.UpdateDoadorTask;
import br.edu.ifpb.ajudemais.asyncTasks.AsyncResponse;
import br.edu.ifpb.ajudemais.domain.Conta;
import br.edu.ifpb.ajudemais.domain.Doador;
import br.edu.ifpb.ajudemais.domain.FcmToken;
import br.edu.ifpb.ajudemais.storage.SharedPrefManager;
import br.edu.ifpb.ajudemais.utils.CustomToast;

/**
 * <p>
 * <b>{@link CreateAccountActivity}</b>
 * </p>
 * <p>
 * Activity para controlar Conta Usuário.
 * <p>
 * <p>
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a> and
 * @author <a href="https://github.com/FranckAJ">Franck Aragão</a>
 */
public class CreateAccountActivity extends BaseActivity implements View.OnClickListener, Validator.ValidationListener {


    private Toolbar mToolbar;
    private Button btnCreateAccount;
    private Doador doadorEdit;
    private TextInputLayout ltedtConfirmPassword;
    private TextInputLayout ltedtPassword;
    private TextInputLayout ltedtUserName;
    private Validator validator;
    private SharedPrefManager sharedPrefManager;

    private CreateDoadorTask createDoadorTask;
    private UpdateDoadorTask updateDoadorTask;

    @Order(6)
    @NotEmpty(messageResId = R.string.msgNameNotInformed, sequence = 1)
    private TextInputEditText edtName;

    @Order(5)
    @NotEmpty(messageResId = R.string.msgUserNameNotInformed, sequence = 1)
    @Length(min = 4, messageResId = R.string.msgInvalideUserName, sequence = 2)
    private TextInputEditText edtUserName;

    @Order(4)
    @NotEmpty(messageResId = R.string.msgPhoneNotInformed, sequence = 1)
    @Length(min = 15, messageResId = R.string.msgPhoneNotCompleted, sequence = 2)
    private TextInputEditText edtPhone;

    @Order(3)
    @Email(messageResId = R.string.msgInvalideEmail, sequence = 1)
    private TextInputEditText edtEmail;

    @Order(2)
    @Password(min = 6, messageResId = R.string.msgInvalidePassword, sequence = 2)
    @NotEmpty(messageResId = R.string.msgPasswordNotInformed, sequence = 1)
    private TextInputEditText edtPassword;

    @Order(1)
    @NotEmpty(messageResId = R.string.msgPasswordNotInformed, sequence = 1)
    @ConfirmPassword(messageResId = R.string.msgPasswordAndConfirmPasswordDoesNotMatch, sequence = 3)
    @Length(messageResId = R.string.msgInvalidePassword, sequence = 2)
    private TextInputEditText edtConfirmPassword;


    /**
     * Método Que é executado no momento inicial da inicialização da activity.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        initProperties();
        init();

        if (doadorEdit != null) {
            setEditValueInFields();
        }

        validator = new Validator(this);
        validator.setValidationListener(this);

        btnCreateAccount.setOnClickListener(this);

    }

    /**
     * Inicializa todos os atributos e propriedades utilizadas na activity.
     */
    public void init() {
        sharedPrefManager = new SharedPrefManager(this);
        initProperties();
        doadorEdit = (Doador) getIntent().getSerializableExtra("Doador");
        mToolbar = (Toolbar) findViewById(R.id.nav_action);
        if (doadorEdit != null) {
            mToolbar.setTitle("Editar Conta");
        }

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btnCreateAccount = (Button) findViewById(R.id.btnCreateAccount);
        edtName = (TextInputEditText) findViewById(R.id.edtNome);
        edtUserName = (TextInputEditText) findViewById(R.id.edtUserName);
        edtPhone = (TextInputEditText) findViewById(R.id.edtPhone);
        edtEmail = (TextInputEditText) findViewById(R.id.edtEmail);
        edtPassword = (TextInputEditText) findViewById(R.id.edtPassword);
        edtConfirmPassword = (TextInputEditText) findViewById(R.id.edtConfirmPassword);
        ltedtConfirmPassword = (TextInputLayout) findViewById(R.id.ltedtConfirmPassword);
        ltedtPassword = (TextInputLayout) findViewById(R.id.ltedtPassword);
        ltedtUserName = (TextInputLayout) findViewById(R.id.ltedtUserName);

        androidUtil.setMaskPhone(edtPhone);
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
                Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);

                if (doadorEdit != null) {
                    intent = new Intent(CreateAccountActivity.this, ProfileSettingsActivity.class);
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * Set valores do Doador.
     */
    public void setEditValueInFields() {
        if (doadorEdit != null) {
            edtName.setText(doadorEdit.getNome());
            edtUserName.setVisibility(View.GONE);
            ltedtUserName.setVisibility(View.GONE);
            edtPhone.setText(doadorEdit.getTelefone());
            edtEmail.setText(doadorEdit.getConta().getEmail());
            edtConfirmPassword.setVisibility(View.GONE);
            edtPassword.setVisibility(View.GONE);
            ltedtConfirmPassword.setVisibility(View.GONE);
            ltedtPassword.setVisibility(View.GONE);
            btnCreateAccount.setText("Editar");
        }
    }

    /**
     * Implementa as operação de clique.
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnCreateAccount) {
            validator.validate();
        }
    }

    /**
     * Executada Doador de Task.
     *
     * @param doador
     */
    private void executeCreateDoadorTask(Doador doador) {
        createDoadorTask = new CreateDoadorTask(this, doador);

        createDoadorTask.delegate = new AsyncResponse<Doador>() {
            @Override
            public void processFinish(Doador output) {
                SharedPrefManager.getInstance(CreateAccountActivity.this).storeUser(output.getConta());
                Intent intent = new Intent();
                intent.setClass(CreateAccountActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("Conta", output.getConta());
                startActivity(intent);
                finish();
            }
        };
        createDoadorTask.execute();
    }


    /**
     * Executada Doador Update de Task.
     *
     * @param doador
     */
    private void executeUpdateDoadorTask(Doador doador) {
        updateDoadorTask = new UpdateDoadorTask(this, doador);
        updateDoadorTask.delegate = new AsyncResponse<Doador>() {
            @Override
            public void processFinish(Doador output) {
                SharedPrefManager.getInstance(CreateAccountActivity.this).storeUser(output.getConta());
                Intent intent = new Intent();
                intent.setClass(CreateAccountActivity.this, ProfileSettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                CustomToast.getInstance(CreateAccountActivity.this).createSuperToastSimpleCustomSuperToast(getString(R.string.updatedInformation));
            }
        };
        updateDoadorTask.execute();
    }

    @Override
    public void onValidationSucceeded() {
        if (doadorEdit != null) {
            doadorEdit.setNome(edtName.getText().toString().trim());
            doadorEdit.setTelefone(edtPhone.getText().toString().trim());
            doadorEdit.getConta().setEmail(edtEmail.getText().toString().trim());

            executeUpdateDoadorTask(doadorEdit);
        } else {
            List<String> grupos = new ArrayList<>();
            grupos.add("ROLE_DOADOR");
            FcmToken fcmToken = new FcmToken(FirebaseInstanceId.getInstance().getToken());
            Doador doador = new Doador(edtName.getText().toString().trim(), edtPhone.getText().toString().trim(), fcmToken,
                    new Conta(edtUserName.getText().toString().trim(),
                            edtPassword.getText().toString().trim(), true, edtEmail.getText().toString().trim(), grupos));

            executeCreateDoadorTask(doador);
        }
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
                CustomToast.getInstance(CreateAccountActivity.this).createSuperToastSimpleCustomSuperToast(message);
            }
        }
    }

}
