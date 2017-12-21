package br.edu.ifpb.ajudemais.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import br.edu.ifpb.ajudemais.R;
import br.edu.ifpb.ajudemais.asycnTasks.LoginDoadorTask;
import br.edu.ifpb.ajudemais.asycnTasks.UpdateDoadorTask;
import br.edu.ifpb.ajudemais.asyncTasks.AsyncResponse;
import br.edu.ifpb.ajudemais.asyncTasks.GetImageTask;
import br.edu.ifpb.ajudemais.domain.Conta;
import br.edu.ifpb.ajudemais.domain.Doador;
import br.edu.ifpb.ajudemais.storage.SharedPrefManager;
import br.edu.ifpb.ajudemais.util.FacebookAccount;
import br.edu.ifpb.ajudemais.utils.CustomToast;


/**
 * <p>
 * <b>{@link LoginActivity}</b>
 * </p>
 * <p>
 * Activity para controlar Login.
 * <p>
 * <p>
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener, FacebookCallback<LoginResult>, Validator.ValidationListener {

    private Button btnCreateAccount;
    private Button btnOpenApp;
    private LoginButton btnFacebook;
    private TextView tvRecoveryPassword;
    private CallbackManager callbackManager;
    private LoginDoadorTask loginDoadorTask;
    private byte[] imagem;
    private GetImageTask getImageTask;
    private Validator validator;
    private ProfileTracker mProfileTracker;
    private LoginResult loginResult;

    @Order(2)
    @Length(min = 4, messageResId = R.string.msgInvalideUserName, sequence = 2)
    @NotEmpty(messageResId = R.string.msgUserNameNotInformed, sequence = 1)
    private EditText edtUserName;

    @Order(1)
    @Length(min = 6, messageResId = R.string.msgInvalidePassword, sequence = 2)
    @NotEmpty(messageResId = R.string.msgPasswordNotInformed, sequence = 1)
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

        btnCreateAccount.setOnClickListener(this);
        btnOpenApp.setOnClickListener(this);
        tvRecoveryPassword.setOnClickListener(this);

        callbackManager = CallbackManager.Factory.create();
        btnFacebook.setReadPermissions(Arrays.asList("public_profile", "email"));

        btnFacebook.registerCallback(callbackManager, this);
    }


    /**
     * Inicializa todos os atributos e propriedades utilizadas na activity.
     */
    public void init() {
        initProperties();
        validator = new Validator(this);
        validator.setValidationListener(this);
        btnCreateAccount = (Button) findViewById(R.id.btnCreateAccount);
        btnOpenApp = (Button) findViewById(R.id.btnOpen);
        btnFacebook = (LoginButton) findViewById(R.id.btnFacebook);
        tvRecoveryPassword = (TextView) findViewById(R.id.tvForgotPassword);
        edtUserName = (EditText) findViewById(R.id.edtUserName);
        edtPassword = (EditText) findViewById(R.id.edtPassword);

    }


    /**
     * Método que retorna para a tela de login em casos de cancelamento de login via Facebook
     * ou falha na tentativa de login
     */
    private void goBackToLoginScreen() {
        Intent intent = new Intent();
        intent.setClass(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    /**
     * Método que obtem os dados de um usuário do facebook após uma solicitação bem sucedida
     * de login. A partir deste resultado, encaminha o user para a tela principal da aplicação
     */
    private void goToMainActivity() {
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
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
        } else if (v.getId() == R.id.btnCreateAccount) {
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this, CreateAccountActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        } else if (v.getId() == R.id.tvForgotPassword) {
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this, RecoveryPasswordActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }


    /**
     * Executa Asycn para Login Task
     */
    private void executeTasksLoginDoador() {
        loginDoadorTask = new LoginDoadorTask(this, edtUserName.getText().toString().trim(), edtPassword.getText().toString().trim());
        loginDoadorTask.delegate = new AsyncResponse<Doador>() {
            @Override
            public void processFinish(final Doador output) {
                if (output != null) {
                    if (output.getFoto() != null) {
                        getImageTask = new GetImageTask(LoginActivity.this, output.getFoto().getNome());
                        getImageTask.delegate = new AsyncResponse<byte[]>() {
                            @Override
                            public void processFinish(byte[] imaBytes) {
                                imagem = imaBytes;

                                executeUpdateDoadorToken(output);
                            }
                        };
                        getImageTask.execute();

                    } else {
                        executeUpdateDoadorToken(output);
                    }
                }
            }
        };

        loginDoadorTask.execute();
    }


    /**
     * Redireciona para main actiivity
     *
     * @param conta
     */
    private void redirectMainActivity(Conta conta) {
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Conta", conta);
        if (imagem != null) {
            intent.putExtra("ImageByteArray", imagem);
        }
        startActivity(intent);
        finish();
    }

    /**
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * @param loginResult
     */
    @Override
    public void onSuccess(final LoginResult loginResult) {

        this.loginResult = loginResult;
        if (Profile.getCurrentProfile() == null) {
            mProfileTracker = new ProfileTracker() {
                @Override
                protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                    executeLoadingAccountUser(currentProfile.getId(), loginResult);

                    mProfileTracker.stopTracking();
                }
            };
        } else {
            final Profile profile = Profile.getCurrentProfile();
            executeLoadingAccountUser(profile.getId(), loginResult);
        }
    }

    /**
     * Verifica se já existe essa conta do facebook na aplicação.
     *
     * @param username
     * @param loginResult
     */
    private void executeLoadingAccountUser(String username, final LoginResult loginResult) {
        loginDoadorTask = new LoginDoadorTask(this, username, username);
        loginDoadorTask.setLoginFacebook(true);
        loginDoadorTask.delegate = new AsyncResponse<Doador>() {
            @Override
            public void processFinish(final Doador output) {
                if (output == null) {
                    FacebookAccount.userFacebookData(LoginActivity.this, loginResult, LoginActivity.this);
                } else {
                    SharedPrefManager.getInstance(getApplication()).storeUser(output.getConta());

                    if (output.getFoto() != null) {
                        getImageTask = new GetImageTask(LoginActivity.this, output.getFoto().getNome());
                        getImageTask.delegate = new AsyncResponse<byte[]>() {
                            @Override
                            public void processFinish(byte[] imaBytes) {
                                imagem = imaBytes;
                                executeUpdateDoadorToken(output);
                            }
                        };
                        getImageTask.execute();

                    } else {
                        executeUpdateDoadorToken(output);
                    }
                }

            }
        };

        loginDoadorTask.execute();
    }

    private void executeUpdateDoadorToken(Doador doador) {
        if (FirebaseInstanceId.getInstance().getToken().trim().length() > 0) {
            doador.getTokenFCM().setDate(new Date());
            doador.getTokenFCM().setToken(FirebaseInstanceId.getInstance().getToken());
            executeUpdateDoadorTask(doador);
        } else {
            redirectMainActivity(doador.getConta());
        }
    }

    /**
     *
     */
    @Override
    public void onCancel() {
        goBackToLoginScreen();
        CustomToast.getInstance(this).createSuperToastSimpleCustomSuperToast(getString(R.string.cancelOperation));
    }

    /**
     * @param e
     */
    @Override
    public void onError(FacebookException e) {
        goBackToLoginScreen();
        CustomToast.getInstance(this).createSuperToastSimpleCustomSuperToast(getString(R.string.errorOnLogin));

    }

    @Override
    public void onValidationSucceeded() {
        executeTasksLoginDoador();
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
                CustomToast.getInstance(LoginActivity.this).createSuperToastSimpleCustomSuperToast(message);
            }
        }
    }

    /**
     * Executada Doador Update de Task.
     *
     * @param doador
     */
    private void executeUpdateDoadorTask(Doador doador) {
        UpdateDoadorTask updateDoadorTask = new UpdateDoadorTask(this, doador);
        updateDoadorTask.delegate = new AsyncResponse<Doador>() {
            @Override
            public void processFinish(Doador output) {
                redirectMainActivity(output.getConta());
            }
        };
        updateDoadorTask.execute();
    }

}
