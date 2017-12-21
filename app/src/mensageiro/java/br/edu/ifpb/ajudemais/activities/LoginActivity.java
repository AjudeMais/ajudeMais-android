package br.edu.ifpb.ajudemais.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;

import java.util.Date;
import java.util.List;

import br.edu.ifpb.ajudemais.R;
import br.edu.ifpb.ajudemais.asycnTasks.LoginMensageiroTask;
import br.edu.ifpb.ajudemais.asycnTasks.UpdateMensageiroTask;
import br.edu.ifpb.ajudemais.asyncTasks.AsyncResponse;
import br.edu.ifpb.ajudemais.asyncTasks.GetImageTask;
import br.edu.ifpb.ajudemais.domain.Conta;
import br.edu.ifpb.ajudemais.domain.Mensageiro;
import br.edu.ifpb.ajudemais.utils.CustomToast;

import static br.edu.ifpb.ajudemais.R.id.tvForgotPassword;


/**
 * <p>
 * <b>LoginActivity</b>
 * </p>
 * <p>
 * Activity para controlar Login.
 * <p>
 * <p>
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a> and
 * @author <a href="https://github.com/amslv">Ana Silva</a>
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener, Validator.ValidationListener {

    private Button btnOpenApp;
    private TextView tvRecoveryPassword;
    private Button btnCreateAccount;
    private Validator validator;
    private LoginMensageiroTask loginMensageiroTask;
    private GetImageTask getImageTask;
    private byte[] imagem;

    @Order(2)
    @NotEmpty(messageResId = R.string.msgUserNameNotInformed, sequence = 1)
    @Length(min = 4, messageResId = R.string.msgInvalideUserName, sequence = 2)
    private EditText edtUserName;

    @Order(1)
    @NotEmpty(messageResId = R.string.msgPasswordNotInformed, sequence = 1)
    @Length(min = 6, messageResId = R.string.msgInvalidePassword, sequence = 2)
    private EditText edtPassword;


    /**
     * Método Que é executado no momento inicial da inicialização da activity.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        validator = new Validator(this);
        validator.setValidationListener(this);

        btnOpenApp.setOnClickListener(this);
        btnCreateAccount.setOnClickListener(this);
        tvRecoveryPassword.setOnClickListener(this);
    }

    /**
     * Inicializa todos os atributos e propriedades utilizadas na activity.
     */
    public void init() {
        initProperties();
        btnOpenApp = (Button) findViewById(R.id.btnOpen);
        tvRecoveryPassword = (TextView) findViewById(tvForgotPassword);
        edtUserName = (EditText) findViewById(R.id.edtUserName);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btnCreateAccount = (Button) findViewById(R.id.btnCreateAccountAccount);

    }


    /**
     * Método implementado da interface View.OnClickListener para criação de eventos de clicks.
     *
     * @param v
     */
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btnOpen) {
            validator.validate();

        } else if (v.getId() == R.id.btnCreateAccountAccount) {
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this, CreateMensageiroAccountActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (v.getId() == tvForgotPassword) {
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this, RecoveryPasswordActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    @Override
    public void onValidationSucceeded() {
        if (androidUtil.isOnline()) {
            executeTasksLoginMensageiro();
        } else {
            CustomToast.getInstance(this).createSimpleCustomSuperToastActivity(getString(R.string.no_internet_connection));
        }
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            if (view instanceof EditText) {
                ((EditText) view).setError(message);
                view.requestFocus();
            } else {
                CustomToast.getInstance(this).createSimpleCustomSuperToastActivity(message);
            }
        }
    }


    private void executeTasksLoginMensageiro() {
        loginMensageiroTask = new LoginMensageiroTask(this, edtUserName.getText().toString().trim(), edtPassword.getText().toString().trim());
        loginMensageiroTask.delegate = new AsyncResponse<Mensageiro>() {
            @Override
            public void processFinish(final Mensageiro output) {
                if (output.getFoto() != null) {

                    getImageTask = new GetImageTask(LoginActivity.this, output.getFoto().getNome());
                    getImageTask.delegate = new AsyncResponse<byte[]>() {
                        @Override
                        public void processFinish(byte[] imaBytes) {
                            imagem = imaBytes;
                            executeUpdateMensageiroToken(output);
                        }
                    };
                    getImageTask.execute();

                } else {
                    executeUpdateMensageiroToken(output);
                }
            }
        };

        loginMensageiroTask.execute();
    }

    private void redirectMainActivity(Conta conta) {
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Conta", conta);
        intent.putExtra("ImageByteArray", imagem);
        startActivity(intent);
        finish();
    }


    private void executeUpdateMensageiroToken(Mensageiro mensageiro) {
        if (FirebaseInstanceId.getInstance().getToken().trim().length() > 0) {
            mensageiro.getTokenFCM().setDate(new Date());
            mensageiro.getTokenFCM().setToken(FirebaseInstanceId.getInstance().getToken());
            executeUpdateMensageiroTask(mensageiro);
        } else {
            redirectMainActivity(mensageiro.getConta());
        }
    }


    /**
     * Executada Mensageiro Update de Task.
     *
     * @param mensageiro
     */
    private void executeUpdateMensageiroTask(Mensageiro mensageiro) {
        UpdateMensageiroTask updateMensageiroTask = new UpdateMensageiroTask(this, mensageiro);
        updateMensageiroTask.delegate = new AsyncResponse<Mensageiro>() {
            @Override
            public void processFinish(Mensageiro output) {
                redirectMainActivity(output.getConta());
            }
        };
        updateMensageiroTask.execute();
    }

}
