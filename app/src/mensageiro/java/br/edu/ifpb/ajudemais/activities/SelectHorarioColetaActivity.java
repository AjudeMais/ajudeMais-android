package br.edu.ifpb.ajudemais.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.edu.ifpb.ajudemais.R;
import br.edu.ifpb.ajudemais.adapters.SelectHorarioColetaAdapter;
import br.edu.ifpb.ajudemais.asycnTasks.LoadingMensageiroTask;
import br.edu.ifpb.ajudemais.asyncTasks.AsyncResponse;
import br.edu.ifpb.ajudemais.asyncTasks.UpdateEstadoDonativoTask;
import br.edu.ifpb.ajudemais.asyncTasks.ValidateColetaTask;
import br.edu.ifpb.ajudemais.domain.DisponibilidadeHorario;
import br.edu.ifpb.ajudemais.domain.Donativo;
import br.edu.ifpb.ajudemais.domain.EstadoDoacao;
import br.edu.ifpb.ajudemais.domain.Mensageiro;
import br.edu.ifpb.ajudemais.enumarations.Estado;
import br.edu.ifpb.ajudemais.storage.SharedPrefManager;
import br.edu.ifpb.ajudemais.utils.CustomToast;

public class SelectHorarioColetaActivity extends BaseActivity implements View.OnClickListener{

    private Donativo donativo;
    private Button btnConfirm;
    private RecyclerView recyclerView;
    private UpdateEstadoDonativoTask updateEstadoDonativoTask;
    private SelectHorarioColetaAdapter selectHorarioColetaAdapter;
    private List<DisponibilidadeHorario> currentSelectedHorarios;
    private LoadingMensageiroTask loadingMensageiroTask;
    private Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_horario_coleta);

        Intent intentDonativo = getIntent();
        this.donativo = (Donativo) intentDonativo.getSerializableExtra("Donativo");
        currentSelectedHorarios = new ArrayList<>();
        init();
    }

    @Override
    public void init() {
        initProperties();
        recyclerView = (RecyclerView) findViewById(R.id.recycle_view_list);
        btnConfirm = (Button) findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(this);

        mToolbar = (Toolbar) findViewById(R.id.nav_action);
        mToolbar.setTitle(getString(R.string.SelectHorarioColeta));

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layout);
        selectHorarioColetaAdapter = new SelectHorarioColetaAdapter(donativo.getHorariosDisponiveis(), this, new SelectHorarioColetaAdapter.OnItemCheckListener() {
            @Override
            public void onItemCheck(DisponibilidadeHorario item) {
                currentSelectedHorarios.add(item);
            }

            @Override
            public void onItemUncheck(DisponibilidadeHorario item) {
                currentSelectedHorarios.remove(item);
            }
        });
        recyclerView.setAdapter(selectHorarioColetaAdapter);
    }

    /**
     * Verifica se donativo ainda está disponível para ser coletado
     */
    public void executeValidateColetaDonativoTask() {
        ValidateColetaTask validateColetaTask = new ValidateColetaTask(this, donativo.getId());
        validateColetaTask.delegate = new AsyncResponse<Boolean>() {
            @Override
            public void processFinish(Boolean output) {
                if (!output) {
                    CustomToast.getInstance(getApplicationContext()).createSuperToastSimpleCustomSuperToast(getString(R.string.coleta_not_avaible));
                }else {
                    if (validateSelectedHorario()){
                        for(int i = 0 ; i<donativo.getHorariosDisponiveis().size();i++){
                            if(donativo.getHorariosDisponiveis().get(i).getId().equals(currentSelectedHorarios.get(0).getId())){
                                donativo.getHorariosDisponiveis().get(i).setAtivo(true);
                            }
                        }
                        validateAndSetStateDoacao();
                        executeLoadingMensageiroAndUpdateDonativoTasks(donativo);
                    }

                }
            }
        };
        validateColetaTask.execute();

    }

    /**
     * Valida o estado da doação e seta o estado na label.
     */
    private void validateAndSetStateDoacao() {
        for(int i = 0 ; i<donativo.getEstadosDaDoacao().size();i++){
            if (donativo.getEstadosDaDoacao().get(i).getAtivo() != null && donativo.getEstadosDaDoacao().get(i).getAtivo()) {
                donativo.getEstadosDaDoacao().get(i).setAtivo(false);
            }
        }

        EstadoDoacao estadoDoacao = new EstadoDoacao();
        estadoDoacao.setAtivo(true);
        estadoDoacao.setEstadoDoacao(Estado.ACEITO);
        estadoDoacao.setData(new Date());

        donativo.getEstadosDaDoacao().add(estadoDoacao);
    }

    /**
     * Executa Asycn task para atualizar o estado do donativo;
     */
    private void executeUpdateDonativoTask(final Donativo donativo) {
        updateEstadoDonativoTask = new UpdateEstadoDonativoTask(this, donativo);
        updateEstadoDonativoTask.delegate = new AsyncResponse<Donativo>() {
            @Override
            public void processFinish(Donativo output) {
                Intent intent = new Intent(SelectHorarioColetaActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                CustomToast.getInstance(SelectHorarioColetaActivity.this).createSuperToastSimpleCustomSuperToast(getString(R.string.acepted_coleta));

            }
        };
        updateEstadoDonativoTask.execute();
    }
    /**
     * Executa Asycn task para recuperar mensageiro logado;
     */
    private void executeLoadingMensageiroAndUpdateDonativoTasks(final Donativo donativo) {
        loadingMensageiroTask = new LoadingMensageiroTask(this, SharedPrefManager.getInstance(this).getUser().getUsername());
        loadingMensageiroTask.delegate = new AsyncResponse<Mensageiro>() {
            @Override
            public void processFinish(Mensageiro output) {
                donativo.setMensageiro(output);
                executeUpdateDonativoTask(donativo);
            }
        };
        loadingMensageiroTask.execute();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_confirm){
            executeValidateColetaDonativoTask();
        }
    }

    /**
     * Valida horário escolhido
     * @return
     */
    private boolean validateSelectedHorario(){

        if (currentSelectedHorarios.size()<1){
            CustomToast.getInstance(this).createSuperToastSimpleCustomSuperToast(getString(R.string.horario_not_informed));
            return false;
        }
        if (currentSelectedHorarios.size() > 1){
            CustomToast.getInstance(this).createSuperToastSimpleCustomSuperToast(getString(R.string.unique_horario_select));
            return false;
        }

        return true;
    }
}
