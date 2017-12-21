package br.edu.ifpb.ajudemais.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.edu.ifpb.ajudemais.R;
import br.edu.ifpb.ajudemais.activities.DoacaoActivity;
import br.edu.ifpb.ajudemais.adapters.CategoriasAdapter;
import br.edu.ifpb.ajudemais.asycnTasks.LoadingCategoriasTask;
import br.edu.ifpb.ajudemais.asyncTasks.AsyncResponse;
import br.edu.ifpb.ajudemais.domain.Categoria;
import br.edu.ifpb.ajudemais.domain.InstituicaoCaridade;
import br.edu.ifpb.ajudemais.listeners.RecyclerItemClickListener;

/**
 * <p>
 * <b>InstituicaoDetailFragment</b>
 * </p>
 * <p>
 * InstituicaoDetailFragment para lista de instituições
 * <p>
 * <p>
 * </p>
 *
 * @author <a href="https://github.com/FranckAJ">Franck Aragão</a>
 */
public class InstituicaoDetailFragment extends Fragment implements RecyclerItemClickListener.OnItemClickListener {

    private RecyclerView recyclerView;
    private TextView descricaoInstituicao;
    private TextView emailInstituicao;
    private TextView logradouroInstituicao;
    private TextView localidadeInstituicao;
    private CardView cardViewEmail;
    private InstituicaoCaridade instituicaoCaridade;
    private CategoriasAdapter categoriasAdapter;
    private TextView listInstituicoes;
    private View view;
    private List<Categoria> categorias;
    private LoadingCategoriasTask loadingCategoriasTask;
    private CardView cardView;

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_instituicao_detail, container, false);

        Intent intentInstituicao = getActivity().getIntent();
        instituicaoCaridade = (InstituicaoCaridade) intentInstituicao.getSerializableExtra("instituicao");

        setHasOptionsMenu(true);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view_list);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layout);
        view.findViewById(R.id.loadingPanelMainSearchInst).setVisibility(View.VISIBLE);
        listInstituicoes = (TextView) view.findViewById(R.id.tv_list_itens_doaveis);
        cardView = (CardView) view.findViewById(R.id.card_no_itens);

        executeLoadingCategoriasTask();

        return view;
    }

    /**
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        descricaoInstituicao = (TextView) getView().findViewById(R.id.tv_instituicao_detail_descricao);
        emailInstituicao = (TextView) getView().findViewById(R.id.tv_instituicao_detail_email);
        logradouroInstituicao = (TextView) getView().findViewById(R.id.tv_instituicao_detail_logradouro);
        localidadeInstituicao = (TextView) getView().findViewById(R.id.tv_instituicao_detail_localidade);

        cardViewEmail = (CardView) getView().findViewById(R.id.card_view_intituicao_detail_email);


    }

    /**
     *
     */
    @Override
    public void onStart() {
        super.onStart();
        descricaoInstituicao.setText(instituicaoCaridade.getDescricao());
        emailInstituicao.setText(instituicaoCaridade.getConta().getEmail());
        logradouroInstituicao.setText(instituicaoCaridade.getEndereco().getLogradouro() + ", " +
                instituicaoCaridade.getEndereco().getNumero());
        localidadeInstituicao.setText(
                instituicaoCaridade.getEndereco().getBairro() + ", " +
                        instituicaoCaridade.getEndereco().getLocalidade() + " - " +
                        instituicaoCaridade.getEndereco().getUf());

        cardViewEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail(instituicaoCaridade.getConta().getEmail());
            }
        });


    }

    /**
     * Criar intent apropriada para abrir o app do gmail.
     *
     * @param email
     */
    private void sendEmail(String email) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/html");
        final PackageManager pm = getActivity().getPackageManager();
        final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
        String className = null;
        for (final ResolveInfo info : matches) {
            if (info.activityInfo.packageName.equals("com.google.android.gm")) {
                className = info.activityInfo.name;

                if (className != null && !className.isEmpty()) {
                    break;
                }
            }
        }
        emailIntent.setClassName("com.google.android.gm", className);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "[Ajude Mais App]");
        startActivity(emailIntent);
    }

    @Override
    public void onItemClick(View childView, int position) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), DoacaoActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Categoria", categorias.get(position));
        startActivity(intent);
    }

    @Override
    public void onItemLongPress(View childView, int position) {

    }

    /**
     * Executa a asycn task para carregamento das categorias ativas da instituição.
     */
    private void executeLoadingCategoriasTask() {
        loadingCategoriasTask = new LoadingCategoriasTask(getContext(), instituicaoCaridade.getId());
        loadingCategoriasTask.delegate = new AsyncResponse<List<Categoria>>() {
            @Override
            public void processFinish(List<Categoria> categoriasResult) {
                categorias = categoriasResult;
                view.findViewById(R.id.loadingPanelMainSearchInst).setVisibility(View.GONE);
                if (categoriasResult.size() > 0) {
                    categoriasAdapter = new CategoriasAdapter(categorias, getActivity());
                    recyclerView.setAdapter(categoriasAdapter);
                    recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), InstituicaoDetailFragment.this));
                    recyclerView.setVisibility(View.VISIBLE);
                }else {
                    cardView.setVisibility(View.VISIBLE);
                }
            }
        };

        loadingCategoriasTask.execute();
    }

}
