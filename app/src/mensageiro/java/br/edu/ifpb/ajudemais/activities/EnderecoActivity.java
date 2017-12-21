package br.edu.ifpb.ajudemais.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;

import java.util.List;

import br.edu.ifpb.ajudemais.R;
import br.edu.ifpb.ajudemais.asycnTasks.UpdateMensageiroTask;
import br.edu.ifpb.ajudemais.asyncTasks.AsyncResponse;
import br.edu.ifpb.ajudemais.domain.Endereco;
import br.edu.ifpb.ajudemais.domain.Mensageiro;
import br.edu.ifpb.ajudemais.storage.SharedPrefManager;
import br.edu.ifpb.ajudemais.utils.AndroidUtil;
import br.edu.ifpb.ajudemais.utils.CustomToast;

public class EnderecoActivity extends AbstractAsyncActivity implements View.OnClickListener, Validator.ValidationListener {

    private Toolbar mToolbar;
    private Resources resources;
    private AndroidUtil androidUtil;
    private Mensageiro mensageiroEdit;
    private Integer indexEndereco;

    @Order(6)
    @NotEmpty(messageResId = R.string.msgEmptyCep, sequence = 1)
    private TextInputEditText edtCep;

    @Order(5)
    @NotEmpty(messageResId = R.string.msgEmptyLogradouro, sequence = 1)
    private TextInputEditText edtLogradouro;

    @Order(4)
    @NotEmpty(messageResId = R.string.msgEmptyNumero, sequence = 1)
    private TextInputEditText edtNumero;

    @Order(3)
    @NotEmpty(messageResId = R.string.msgEmptyBairro, sequence = 1)
    private TextInputEditText edtBairro;

    @Order(2)
    @NotEmpty(messageResId = R.string.msgEmptyLocalidade, sequence = 1)
    private TextInputEditText edtLocalidade;

    @Order(1)
    @Length(max = 2, min = 2, messageResId = R.string.msgFormatUfInvalide, sequence = 3)
    @NotEmpty(messageResId = R.string.msgEmptyUf, sequence = 1)
    private TextInputEditText edtUf;

    private TextInputEditText edtComplemento;

    private Button btnCadastrarEndereco;
    private Endereco endereco;
    private Validator validator;
    private UpdateMensageiroTask updateMensageiroTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endereco);
        androidUtil = new AndroidUtil(this);
        init();
        if (indexEndereco != null) {
            setEditEnderecoValues();
        }

        validator = new Validator(this);
        validator.setValidationListener(this);

        btnCadastrarEndereco.setOnClickListener(this);
    }

    /**
     * Set nos campos do formulário o endereço
     */
    private void setEndereco(Endereco endereco) {
        if (endereco != null) {
            edtCep.setText(endereco.getCep() != null ? endereco.getCep() : "");
            edtLogradouro.setText(endereco.getLogradouro() != null ? endereco.getLogradouro() : "");
            edtLocalidade.setText(endereco.getLocalidade() != null ? endereco.getLocalidade() : "");
            edtBairro.setText(endereco.getBairro() != null ? endereco.getBairro() : "");
            edtUf.setText(endereco.getUf() != null ? endereco.getUf() : "");

        }

    }

    /**
     * Inicializa todos os componentes da activity
     */
    private void init() {

        mensageiroEdit = (Mensageiro) getIntent().getSerializableExtra("Mensageiro");
        indexEndereco = (Integer) getIntent().getExtras().get("Index");
        endereco = (Endereco) getIntent().getExtras().get("Endereco");

        edtCep = (TextInputEditText) findViewById(R.id.edtCep);
        edtLogradouro = (TextInputEditText) findViewById(R.id.edtLogradouro);
        edtNumero = (TextInputEditText) findViewById(R.id.edtNumero);
        edtBairro = (TextInputEditText) findViewById(R.id.edtBairro);
        edtLocalidade = (TextInputEditText) findViewById(R.id.edtLocalidade);
        edtComplemento = (TextInputEditText) findViewById(R.id.edtComplemento);
        edtUf = (TextInputEditText) findViewById(R.id.edtUf);
        btnCadastrarEndereco = (Button) findViewById(R.id.btnCadastrarEndereco);

        mToolbar = (Toolbar) findViewById(R.id.nav_action);
        setEndereco(endereco);


        if (indexEndereco != null) {
            mToolbar.setTitle("Editar Endereço");
            btnCadastrarEndereco.setText(getString(R.string.btn_edit));
        } else {
            mToolbar.setTitle("Novo Endereço");
        }

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        resources = getResources();


        androidUtil.setMaskCep(edtCep);
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
                Intent intent = new Intent(EnderecoActivity.this, MyEnderecosActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setEditEnderecoValues() {
        edtCep.setText(mensageiroEdit.getEnderecos().get(indexEndereco).getCep());
        edtLogradouro.setText(mensageiroEdit.getEnderecos().get(indexEndereco).getLogradouro());
        edtLocalidade.setText(mensageiroEdit.getEnderecos().get(indexEndereco).getLocalidade());
        edtBairro.setText(mensageiroEdit.getEnderecos().get(indexEndereco).getBairro());
        edtUf.setText(mensageiroEdit.getEnderecos().get(indexEndereco).getUf());
        edtNumero.setText(mensageiroEdit.getEnderecos().get(indexEndereco).getNumero());
        if (mensageiroEdit.getEnderecos().get(indexEndereco).getComplemento() != null) {
            edtComplemento.setText(mensageiroEdit.getEnderecos().get(indexEndereco).getComplemento());
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnCadastrarEndereco) {
            validator.validate();
        }
    }

    /**
     * Executa task para criar novo endreco.
     */
    private void executeCreateEnderecoTask() {
        updateMensageiroTask = new UpdateMensageiroTask(this, mensageiroEdit);
        updateMensageiroTask.delegate = new AsyncResponse<Mensageiro>() {
            @Override
            public void processFinish(Mensageiro output) {
                mensageiroEdit = output;
                SharedPrefManager.getInstance(EnderecoActivity.this).storeUser(output.getConta());
                Intent intent = new Intent(EnderecoActivity.this, MyEnderecosActivity.class);
                intent.putExtra("Mensageiro", output);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                if (indexEndereco != null) {
                    CustomToast.getInstance(EnderecoActivity.this).createSuperToastSimpleCustomSuperToast(getString(R.string.updatedAddress));
                } else {
                    CustomToast.getInstance(EnderecoActivity.this).createSuperToastSimpleCustomSuperToast(getString(R.string.createdAddress));
                }
            }
        };

        updateMensageiroTask.execute();
    }

    @Override
    public void onValidationSucceeded() {

        if (indexEndereco != null) {
            mensageiroEdit.getEnderecos().get(indexEndereco).setLogradouro(edtLogradouro.getText().toString().trim());
            mensageiroEdit.getEnderecos().get(indexEndereco).setBairro(edtBairro.getText().toString().trim());
            mensageiroEdit.getEnderecos().get(indexEndereco).setNumero(edtNumero.getText().toString().trim());
            mensageiroEdit.getEnderecos().get(indexEndereco).setLocalidade(edtLocalidade.getText().toString().trim());
            mensageiroEdit.getEnderecos().get(indexEndereco).setUf(edtUf.getText().toString().trim());

        } else {
            endereco = new Endereco(edtCep.getText().toString().trim(),
                    edtNumero.getText().toString().trim(),
                    edtBairro.getText().toString().trim(),
                    edtLocalidade.getText().toString().trim(),
                    edtLogradouro.getText().toString().trim(),
                    edtUf.getText().toString().trim());

            mensageiroEdit.getEnderecos().add(endereco);
        }

        if (edtComplemento.getText().toString().trim().length() > 0) {
            if (indexEndereco != null) {
                mensageiroEdit.getEnderecos().get(indexEndereco).setComplemento(edtComplemento.getText().toString().trim());

            } else {
                endereco.setComplemento(edtComplemento.getText().toString().trim());
            }
        }
        executeCreateEnderecoTask();

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
                CustomToast.getInstance(EnderecoActivity.this).createSuperToastSimpleCustomSuperToast(message);
            }
        }
    }

}
