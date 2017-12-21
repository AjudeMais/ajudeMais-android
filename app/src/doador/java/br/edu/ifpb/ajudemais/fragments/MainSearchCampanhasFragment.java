package br.edu.ifpb.ajudemais.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifpb.ajudemais.R;
import br.edu.ifpb.ajudemais.activities.CampanhaActivity;
import br.edu.ifpb.ajudemais.adapters.CampanhasAdapter;
import br.edu.ifpb.ajudemais.asycnTasks.MainSearchCampanhaFragmentTask;
import br.edu.ifpb.ajudemais.asyncTasks.AsyncResponse;
import br.edu.ifpb.ajudemais.domain.Campanha;
import br.edu.ifpb.ajudemais.listeners.RecyclerItemClickListener;
import br.edu.ifpb.ajudemais.utils.AndroidUtil;

/**
 * <p>
 * <b>MainSearchCampanhasFragment</b>
 * </p>
 * <p>
 * MainSearchCampanhasFragment para pesquisa de campanhas
 * <p>
 * <p>
 * </p>
 *
 * @author <a href="https://github.com/FranckAJ">Franck Aragão</a>
 */
public class MainSearchCampanhasFragment extends Fragment implements RecyclerItemClickListener.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, SearchView.OnQueryTextListener {

    private CampanhasAdapter campanhasAdapter;
    private static RecyclerView recyclerView;
    private static View view;
    private List<Campanha> campanhas;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AndroidUtil androidUtil;
    private MainSearchCampanhaFragmentTask mainSearchCampanhaFragmentTask;
    private RecyclerItemClickListener.OnItemClickListener clickListener;
    private SearchView searchView;


    public MainSearchCampanhasFragment() {

    }

    /**
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        androidUtil = new AndroidUtil(getContext());
        setHasOptionsMenu(true);

    }

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_main_search_campanha, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view_list_campanha);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        view.findViewById(R.id.no_internet_fragment).setVisibility(View.GONE);

        swipeRefreshLayout.setOnRefreshListener(this);

        view.findViewById(R.id.loadingPanelMainSearchCampanha).setVisibility(View.VISIBLE);
        view.findViewById(R.id.containerViewSearchCampanha).setVisibility(View.GONE);
        view.findViewById(R.id.empty_list).setVisibility(View.GONE);

        return view;
    }

    /**
     *
     */
    @Override
    public void onStart() {
        super.onStart();
        this.clickListener = this;

        if (androidUtil.isOnline()) {
            executeLoadingCampanhasTask();
        } else {
            setVisibleNoConnection();
        }
    }

    /**
     *
     */
    @Override
    public void onRefresh() {
        if (androidUtil.isOnline()) {
            this.clickListener = this;
            executeLoadingCampanhasTask();
        } else {
            setVisibleNoConnection();
            swipeRefreshLayout.setRefreshing(false);

        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    /**
     * @param newText
     * @return
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        final List<Campanha> filteredModelList = filter(campanhas, newText);

        if (filteredModelList.size() < 1) {
            showListEmpty();
        } else {
            showListCampanhas();
            campanhasAdapter.setFilter(filteredModelList);

        }
        return true;
    }


    @Override
    public void onItemClick(View childView, int position) {
        Campanha campanha = campanhas.get(position);

        Intent intent = new Intent(getContext(), CampanhaActivity.class);
        intent.putExtra("campanha", campanha);
        startActivity(intent);
    }

    @Override
    public void onItemLongPress(View childView, int position) {

    }

    /**
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_view, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(item);
        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        if (campanhasAdapter != null) {
                            campanhasAdapter.setFilter(campanhas);
                        }
                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        return true;
                    }
                });
    }

    private void executeLoadingCampanhasTask() {
        if (androidUtil.isOnline()) {
            mainSearchCampanhaFragmentTask = new MainSearchCampanhaFragmentTask(getContext());
            mainSearchCampanhaFragmentTask.delegate = new AsyncResponse<List<Campanha>>() {

                @Override
                public void processFinish(List<Campanha> output) {
                    if (output.size() < 1) {
                        showListEmpty();

                    } else {
                        campanhas = output;
                        showListCampanhas();
                        campanhasAdapter = new CampanhasAdapter(campanhas, getActivity());
                        recyclerView.setAdapter(campanhasAdapter);
                        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), clickListener));
                        searchView.setOnQueryTextListener(MainSearchCampanhasFragment.this);
                    }
                    swipeRefreshLayout.setRefreshing(false);

                }
            };

            mainSearchCampanhaFragmentTask.execute();
        }else {
            setVisibleNoConnection();
        }
    }

    /**
     * Filtra campanhas pelo nome digitado
     *
     * @param models
     * @param query
     * @return
     */
    private List<Campanha> filter(List<Campanha> models, String query) {
        query = query.toLowerCase();
        final List<Campanha> filteredModelList = new ArrayList<>();
        if (models != null) {
            for (Campanha model : models) {
                final String text = model.getNome().toLowerCase();

                if (text.contains(query)) {
                    filteredModelList.add(model);
                }
            }
        }
        return filteredModelList;
    }

    /**
     * Auxiliar para mostrar fragmento para lista vazia.
     */
    private void showListEmpty() {
        view.findViewById(R.id.no_internet_fragment).setVisibility(View.GONE);
        view.findViewById(R.id.loadingPanelMainSearchCampanha).setVisibility(View.GONE);
        view.findViewById(R.id.containerViewSearchCampanha).setVisibility(View.GONE);
        view.findViewById(R.id.empty_list).setVisibility(View.VISIBLE);

        Button btnReload = (Button) view.findViewById(R.id.empty_list).findViewById(R.id.btn_reload);
        TextView tvReload = (TextView) view.findViewById(R.id.empty_list).findViewById(R.id.tv_reload);
        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenCliqueReload();

            }
        });
        tvReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenCliqueReload();
            }
        });

    }

    private void listenCliqueReload(){
        view.findViewById(R.id.loadingPanelMainSearchCampanha).setVisibility(View.VISIBLE);
        view.findViewById(R.id.containerViewSearchCampanha).setVisibility(View.GONE);
        view.findViewById(R.id.empty_list).setVisibility(View.GONE);
        executeLoadingCampanhasTask();

    }

    /**
     * Auxiliar para mostrar fragmento de sem conexão quando não houver internet no device.
     */
    private void setVisibleNoConnection() {
        view.findViewById(R.id.no_internet_fragment).setVisibility(View.VISIBLE);
        view.findViewById(R.id.loadingPanelMainSearchCampanha).setVisibility(View.GONE);
        view.findViewById(R.id.containerViewSearchCampanha).setVisibility(View.GONE);
        view.findViewById(R.id.empty_list).setVisibility(View.GONE);

        Button btnReload = (Button) view.findViewById(R.id.no_internet_fragment).findViewById(R.id.btn_reload);
        TextView tvReload = (TextView) view.findViewById(R.id.no_internet_fragment).findViewById(R.id.tv_reload);
        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenCliqueReload();

            }
        });
        tvReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenCliqueReload();
            }
        });
    }

    /**
     * Auxiliar para mostrar lista de campanhas e esconder demais fragmentos.
     */
    private void showListCampanhas() {
        view.findViewById(R.id.no_internet_fragment).setVisibility(View.GONE);
        view.findViewById(R.id.loadingPanelMainSearchCampanha).setVisibility(View.GONE);
        view.findViewById(R.id.containerViewSearchCampanha).setVisibility(View.VISIBLE);
        view.findViewById(R.id.empty_list).setVisibility(View.GONE);
    }
}
