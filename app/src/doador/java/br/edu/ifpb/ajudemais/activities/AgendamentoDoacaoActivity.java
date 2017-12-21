package br.edu.ifpb.ajudemais.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import br.edu.ifpb.ajudemais.R;
import br.edu.ifpb.ajudemais.adapters.DisponibilidadeHorarioAdapter;
import br.edu.ifpb.ajudemais.domain.Campanha;
import br.edu.ifpb.ajudemais.domain.DisponibilidadeHorario;
import br.edu.ifpb.ajudemais.domain.Donativo;
import br.edu.ifpb.ajudemais.listeners.RecyclerItemClickListener;
import br.edu.ifpb.ajudemais.utils.CustomToast;


public class AgendamentoDoacaoActivity extends BaseActivity implements View.OnClickListener, RecyclerItemClickListener.OnItemClickListener {

    private Button btnKeep;
    private Donativo donativo;
    private FloatingActionButton fbAddDisponibilidade;
    private RecyclerView recyclerView;
    private Toolbar mToolbar;
    private Integer position = -1;
    private FrameLayout componentListEmpty;
    private DisponibilidadeHorarioAdapter disponibilidadeHorarioAdapte;
    private RelativeLayout componentNoInternet;
    private Campanha campanha;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendamento_doacao);
        init();
        showListEmpty();

        if (donativo != null &&
                (donativo.getHorariosDisponiveis() != null
                        && donativo.getHorariosDisponiveis().size() > 0)) {

            showList();
            disponibilidadeHorarioAdapte = new DisponibilidadeHorarioAdapter(donativo.getHorariosDisponiveis(), this);
            recyclerView.setAdapter(disponibilidadeHorarioAdapte);
        }

    }


    @Override
    public void init() {
        initProperties();
        donativo = (Donativo) getIntent().getSerializableExtra("Donativo");
        campanha = (Campanha) getIntent().getSerializableExtra("Campanha");

        mToolbar = (Toolbar) findViewById(R.id.nav_action);
        btnKeep = (Button) findViewById(R.id.btnKeepDetalhes);
        btnKeep.setOnClickListener(this);
        fbAddDisponibilidade = (FloatingActionButton) findViewById(R.id.fbAddDisponibilidade);
        fbAddDisponibilidade.setOnClickListener(this);
        componentListEmpty = (FrameLayout) findViewById(R.id.empty_list);
        componentNoInternet = (RelativeLayout) findViewById(R.id.no_internet_fragment);

        recyclerView = (RecyclerView) findViewById(R.id.recycle_view_list);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, this));
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layout);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fbAddDisponibilidade) {
            Intent intent = new Intent();
            intent.setClass(AgendamentoDoacaoActivity.this, AddIntervalDataDoacaoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("Donativo", donativo);
            intent.putExtra("Campanha",campanha);
            startActivity(intent);
        } else if (v.getId() == R.id.btnKeepDetalhes) {
            if (donativo.getHorariosDisponiveis() != null && donativo.getHorariosDisponiveis().size() > 0) {
                Intent intent = new Intent();
                intent.setClass(AgendamentoDoacaoActivity.this, ConfirmDoacaoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("Donativo",donativo);
                intent.putExtra("Campanha",campanha);
                startActivity(intent);

            }else {
                CustomToast.getInstance(this).createSuperToastSimpleCustomSuperToast(getString(R.string.disponibilidade_not_informed));
            }

        }
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
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * @param outState
     */

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("Donativo", donativo);
    }

    @Override
    public void onItemClick(View childView, int position) {
        this.position = position;
        openDialogEditAgendamento();

    }

    @Override
    public void onItemLongPress(View childView, int position) {

    }

    /**
     * Dialog para seleção da opção para editar ou remover Agendamento.
     */
    private void openDialogEditAgendamento() {
        final CharSequence[] items = {getString(R.string.tv_edit), getString(R.string.tv_delete), getString(R.string.cancelar)};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.selectOption));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals(getString(R.string.tv_edit))) {
                    Intent intent = new Intent();
                    intent.setClass(AgendamentoDoacaoActivity.this, AddIntervalDataDoacaoActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("Donativo", donativo);
                    intent.putExtra("position", position);
                    startActivity(intent);

                } else if (items[item].equals(getString(R.string.tv_delete))) {
                    DisponibilidadeHorario disponibilidadeHorario = donativo.getHorariosDisponiveis().get(position);
                    donativo.getHorariosDisponiveis().remove(disponibilidadeHorario);
                    if (donativo.getHorariosDisponiveis().size() > 0) {
                        disponibilidadeHorarioAdapte = new DisponibilidadeHorarioAdapter(donativo.getHorariosDisponiveis(), AgendamentoDoacaoActivity.this);
                        recyclerView.setAdapter(disponibilidadeHorarioAdapte);
                        CustomToast.getInstance(AgendamentoDoacaoActivity.this).createSuperToastSimpleCustomSuperToast(getString(R.string.disponibilidade_removida));

                    } else {
                        showListEmpty();
                    }
                } else if (items[item].equals(getString(R.string.cancelar))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    /**
     * Auxiliar para mostrar fragmento para lista vazia.
     */
    private void showListEmpty() {
        componentNoInternet.setVisibility(View.GONE);
        componentListEmpty.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    /**
     * Auxiliar para mostrar fragmento para lista vazia.
     */
    private void showList() {
        componentNoInternet.setVisibility(View.GONE);
        componentListEmpty.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * Auxiliar para mostrar fragmento de sem conexão quando não houver internet no device.
     */
    public void setVisibleNoConnection() {
        componentNoInternet.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        componentListEmpty.setVisibility(View.GONE);
    }

}
