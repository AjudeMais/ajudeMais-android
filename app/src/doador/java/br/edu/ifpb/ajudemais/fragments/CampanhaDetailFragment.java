package br.edu.ifpb.ajudemais.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifpb.ajudemais.R;
import br.edu.ifpb.ajudemais.activities.DoacaoActivity;
import br.edu.ifpb.ajudemais.adapters.CategoriasAdapter;
import br.edu.ifpb.ajudemais.adapters.MetasAdapter;
import br.edu.ifpb.ajudemais.domain.Campanha;
import br.edu.ifpb.ajudemais.domain.Categoria;
import br.edu.ifpb.ajudemais.domain.Meta;
import br.edu.ifpb.ajudemais.listeners.RecyclerItemClickListener;

/**
 * <p>
 * <b>CampanhaDetailFragment</b>
 * </p>
 * <p>
 * Fragmento de tela para visualização de detalhes de uma campanha.
 * <p>
 * <p>
 * </p>
 *
 * @author <a href="https://github.com/FranckAJ">Franck Aragão</a>
 */
public class CampanhaDetailFragment extends Fragment implements RecyclerItemClickListener.OnItemClickListener{

    private RecyclerView recyclerViewCategoria;
    private RecyclerView recyclerViewMetas;
    private TextView descricaoCampanha;
    private TextView nomeInstituicao;
    private TextView termino;
    private Campanha campanha;
    private CategoriasAdapter categoriasAdapter;
    private MetasAdapter metasAdapter;
    private TextView labeListInstituicoes;
    private View view;
    private List<Categoria> categorias;

    /**
     *
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_campanha_detail, container, false);

        setHasOptionsMenu(true);
        return view;
    }


    /**
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Intent intentCampanha = getActivity().getIntent();
        campanha = (Campanha) intentCampanha.getSerializableExtra("campanha");

        categorias = new ArrayList<>();

        descricaoCampanha = (TextView) getView().findViewById(R.id.tv_campanha_detail_descricao);
        nomeInstituicao = (TextView) getView().findViewById(R.id.tv_campanha_detail_inst_name);
        termino = (TextView) getView().findViewById(R.id.tv_campanha_detail_term);
        labeListInstituicoes = (TextView) getView().findViewById(R.id.tv_campanha_list_itens_doaveis);

        recyclerViewCategoria = (RecyclerView) view.findViewById(R.id.recycle_view_campanha_list);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewCategoria.setLayoutManager(layout);

        recyclerViewMetas = (RecyclerView) view.findViewById(R.id.recycle_view_metas_list);
        layout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewMetas.setLayoutManager(layout);

        if (campanha.getMetas() == null || campanha.getMetas().size() < 1) {
            labeListInstituicoes.setVisibility(View.GONE);
        }
        setCategoriasInList(campanha);
        categoriasAdapter = new CategoriasAdapter(categorias, getActivity());
        recyclerViewCategoria.setAdapter(categoriasAdapter);
        recyclerViewCategoria.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), CampanhaDetailFragment.this));


        metasAdapter = new MetasAdapter(getActivity(),campanha.getMetas());
        recyclerViewMetas.setAdapter(metasAdapter);
    }

    private void setCategoriasInList(Campanha campanha){
        for (Meta m : campanha.getMetas()){
            categorias.add(m.getCategoria());
        }
    }

    /**
     *
     */
    @Override
    public void onStart() {
        super.onStart();
        descricaoCampanha.setText(campanha.getDescricao());
        nomeInstituicao.setText(campanha.getInstituicaoCaridade().getNome());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String date = sdf.format(campanha.getDataFim());
        termino.setText(date);
    }

    @Override
    public void onItemClick(View childView, int position) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), DoacaoActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Categoria", categorias.get(position));
        intent.putExtra("Campanha", campanha);
        startActivity(intent);
    }

    @Override
    public void onItemLongPress(View childView, int position) {

    }
}
