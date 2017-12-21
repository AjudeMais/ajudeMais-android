package br.edu.ifpb.ajudemais.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
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
import br.edu.ifpb.ajudemais.asycnTasks.CreateMensageiroTask;
import br.edu.ifpb.ajudemais.asycnTasks.UpdateMensageiroTask;
import br.edu.ifpb.ajudemais.asyncTasks.AsyncResponse;
import br.edu.ifpb.ajudemais.domain.Conta;
import br.edu.ifpb.ajudemais.domain.FcmToken;
import br.edu.ifpb.ajudemais.domain.Mensageiro;
import br.edu.ifpb.ajudemais.storage.SharedPrefManager;
import br.edu.ifpb.ajudemais.utils.CustomToast;
import br.edu.ifpb.ajudemais.validator.annotations.CPF;

/**
 * <p>
 * <b>{@link CreateMensageiroAccountActivity}</b>
 * </p>
 * <p>
 * Activity para criar uma conta de mensageiro no sistema.
 * <p>
 * <p>
 * </p>
 *
 * @author <a href="https://github.com/amslv">Ana Silva</a>
 */
public class CreateMensageiroAccountActivity extends BaseActivity implements View.OnClickListener, Validator.ValidationListener {


    private Toolbar mToolbar;
    private Button btnCreateAccount;
    private Validator validator;
    private Mensageiro mensageiroEdit;
    private TextInputLayout ltedtConfirmPassword;
    private TextInputLayout ltedtPassword;
    private TextInputLayout ltedtCpf;
    private TextInputLayout ltedtUserName;
    private CreateMensageiroTask createMensageiroTask;
    private UpdateMensageiroTask updateMensageiroTask;
    private SharedPrefManager sharedPrefManager;


    @Order(7)
    @NotEmpty(messageResId = R.string.msgNameNotInformed, sequence = 1)
    private TextInputEditText edtName;

    @Order(6)
    @Length(min = 4, messageResId = R.string.msgInvalideUserName)
    @NotEmpty(messageResId = R.string.msgUserNameNotInformed, sequence = 1)
    private TextInputEditText edtUserName;

    @Order(5)
    @Length(min = 15, max = 15, messageResId = R.string.msgPhoneNotCompleted, sequence = 2)
    @NotEmpty(messageResId = R.string.msgPhoneNotInformed, sequence = 1)
    private TextInputEditText edtPhone;

    @Order(4)
    @Email(messageResId = R.string.msgInvalideEmail, sequence = 3)
    @NotEmpty(messageResId = R.string.msgEmailNotInformed, sequence = 1)
    private TextInputEditText edtEmail;

    @Order(3)
    @CPF(messageResId = R.string.msgCpfInvalid, sequence = 2)
    @NotEmpty(messageResId = R.string.msgCpfNotInformed, sequence = 1)
    private TextInputEditText edtCpf;

    @Order(2)
    @Password(min = 6, messageResId = R.string.msgInvalidePassword, sequence = 2)
    @NotEmpty(messageResId = R.string.msgInvalidePassword, sequence = 1)
    private TextInputEditText edtPassword;

    @Order(1)
    @ConfirmPassword(messageResId = R.string.msgPasswordAndConfirmPasswordDoesNotMatch)
    @Length(min = 6, messageResId = R.string.msgInvalidePassword)
    @NotEmpty(messageResId = R.string.msgConfirmPasswordNotInformed, sequence = 1)
    private TextInputEditText edtConfirmPassword;


    /**
     * Método Que é executado no momento inicial da inicialização da activity.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_mensageiro_account);
        init();

        validator = new Validator(this);
        Validator.registerAnnotation(CPF.class);
        validator.setValidationListener(this);

        if (mensageiroEdit != null) {
            setEditValueInFields();
        }
        btnCreateAccount.setOnClickListener(this);

    }

    /**
     * Inicializa todos os atributos e propriedades utilizadas na activity.
     */

    @Override
    public void init() {
        sharedPrefManager = new SharedPrefManager(this);
        initProperties();
        mensageiroEdit = (Mensageiro) getIntent().getSerializableExtra("Mensageiro");
        mToolbar = (Toolbar) findViewById(R.id.nav_action);
        if (mensageiroEdit != null) {
            mToolbar.setTitle("Editar Conta");
        } else {
            mToolbar.setTitle("Nova Conta");
        }

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btnCreateAccount = (Button) findViewById(R.id.btnCreateAccount);
        edtName = (TextInputEditText) findViewById(R.id.edtNome);
        edtUserName = (TextInputEditText) findViewById(R.id.edtUserName);
        edtPhone = (TextInputEditText) findViewById(R.id.edtPhone);
        edtEmail = (TextInputEditText) findViewById(R.id.edtEmail);
        edtCpf = (TextInputEditText) findViewById(R.id.edtCpf);
        edtPassword = (TextInputEditText) findViewById(R.id.edtPassword);
        edtConfirmPassword = (TextInputEditText) findViewById(R.id.edtConfirmPassword);
        ltedtConfirmPassword = (TextInputLayout) findViewById(R.id.ltedtConfirmPassword);
        ltedtPassword = (TextInputLayout) findViewById(R.id.ltedtPassword);
        ltedtUserName = (TextInputLayout) findViewById(R.id.ltedtUserName);
        ltedtCpf = (TextInputLayout) findViewById(R.id.ltEditCpf);

        androidUtil.setMaskPhone(edtPhone);
        androidUtil.setMaskCPF(edtCpf);
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
                Intent intent = new Intent(CreateMensageiroAccountActivity.this, LoginActivity.class);

                if (mensageiroEdit != null) {
                    intent = new Intent(CreateMensageiroAccountActivity.this, ProfileSettingsActivity.class);
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
        if (mensageiroEdit != null) {
            edtName.setText(mensageiroEdit.getNome());
            edtUserName.setVisibility(View.GONE);
            ltedtUserName.setVisibility(View.GONE);
            edtPhone.setText(mensageiroEdit.getTelefone());
            edtEmail.setText(mensageiroEdit.getConta().getEmail());
            edtConfirmPassword.setVisibility(View.GONE);
            edtPassword.setVisibility(View.GONE);
            edtCpf.setVisibility(View.GONE);
            ltedtCpf.setVisibility(View.GONE);
            ltedtConfirmPassword.setVisibility(View.GONE);
            ltedtPassword.setVisibility(View.GONE);
            btnCreateAccount.setText(R.string.btn_atualizar_conta);
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
     * Se o formulário foi validado este método é executado
     */
    @Override
    public void onValidationSucceeded() {
        if (mensageiroEdit != null) {

            mensageiroEdit.getConta().setEmail(edtEmail.getText().toString().trim());
            mensageiroEdit.setNome(edtName.getText().toString().trim());
            mensageiroEdit.setTelefone(edtPhone.getText().toString().trim());

            executeUpdateMensageiroTask(mensageiroEdit);
        } else {
            List<String> grupos = new ArrayList<>();
            grupos.add("ROLE_MENSAGEIRO");
            FcmToken fcmToken = new FcmToken(FirebaseInstanceId.getInstance().getToken());
            Mensageiro mensageiro = new Mensageiro(edtName.getText().toString().trim(),
                    edtCpf.getText().toString().trim(),
                    edtPhone.getText().toString().trim(),
                    new Conta(edtUserName.getText().toString().trim(),
                            edtPassword.getText().toString().trim(), true,
                            edtEmail.getText().toString().trim(), grupos),
                    fcmToken);

            executeCreateMensageiroTask(mensageiro);
        }

    }

    /**
     * Executa task para criação de um novo mensageiro.
     *
     * @param mensageiro
     */
    private void executeCreateMensageiroTask(Mensageiro mensageiro) {
        createMensageiroTask = new CreateMensageiroTask(this, mensageiro);

        createMensageiroTask.delegate = new AsyncResponse<Mensageiro>() {
            @Override
            public void processFinish(Mensageiro output) {
                SharedPrefManager.getInstance(CreateMensageiroAccountActivity.this).storeUser(output.getConta());
                Intent intent = new Intent();
                intent.setClass(CreateMensageiroAccountActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("Conta", output.getConta());
                startActivity(intent);
                finish();
            }
        };

        createMensageiroTask.execute();
    }

    /**
     * Executa task para atualização  de um mensageiro.
     *
     * @param mensageiro
     */
    private void executeUpdateMensageiroTask(Mensageiro mensageiro) {
        updateMensageiroTask = new UpdateMensageiroTask(this, mensageiro);

        updateMensageiroTask.delegate = new AsyncResponse<Mensageiro>() {
            @Override
            public void processFinish(Mensageiro output) {
                SharedPrefManager.getInstance(CreateMensageiroAccountActivity.this).storeUser(output.getConta());
                Intent intent = new Intent(CreateMensageiroAccountActivity.this, ProfileSettingsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("Mensageiro", output);
                startActivity(intent);
                finish();
                CustomToast.getInstance(CreateMensageiroAccountActivity.this).createSuperToastSimpleCustomSuperToast(getString(R.string.Informationupdated));
            }
        };

        updateMensageiroTask.execute();
    }


    /**
     * Se a validação falhar este método é executado
     *
     * @param errors
     */
    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            if (view instanceof TextInputEditText) {
                ((TextInputEditText) view).setError(message);
                view.requestFocus();
            } else {
                CustomToast.getInstance(CreateMensageiroAccountActivity.this).createSuperToastSimpleCustomSuperToast(message);
            }
        }
    }


}
