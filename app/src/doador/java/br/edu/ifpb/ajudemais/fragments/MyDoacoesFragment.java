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
import br.edu.ifpb.ajudemais.activities.DonativoActivity;
import br.edu.ifpb.ajudemais.adapters.DonativosAdapter;
import br.edu.ifpb.ajudemais.asycnTasks.LoadingDoacoesTask;
import br.edu.ifpb.ajudemais.asyncTasks.AsyncResponse;
import br.edu.ifpb.ajudemais.domain.Donativo;
import br.edu.ifpb.ajudemais.dto.DoacaoAdapterDto;
import br.edu.ifpb.ajudemais.listeners.RecyclerItemClickListener;
import br.edu.ifpb.ajudemais.storage.SharedPrefManager;
import br.edu.ifpb.ajudemais.utils.AndroidUtil;

/**
 * <p>
 * <b>{@link MyDoacoesFragment}</b>
 * </p>
 * <p>
 * Fragmento da main activity para exibir as doação do doador logado.
 * <p>
 * <p>
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */
public class MyDoacoesFragment extends Fragment implements RecyclerItemClickListener.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, SearchView.OnQueryTextListener {

    private View view;
    private RecyclerView recyclerView;
    private DonativosAdapter donativosAdapter;
    private List<DoacaoAdapterDto> donativos;
    private LoadingDoacoesTask loadingDoacoesTask;
    private AndroidUtil androidUtil;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SearchView searchView;

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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_my_doacoes, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view_list_donativos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        view.findViewById(R.id.no_internet_fragment).setVisibility(View.GONE);

        swipeRefreshLayout.setOnRefreshListener(this);

        view.findViewById(R.id.loadingPanelMainSearchCampanha).setVisibility(View.VISIBLE);
        view.findViewById(R.id.containerViewSearchDoacoes).setVisibility(View.GONE);
        view.findViewById(R.id.empty_list).setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (androidUtil.isOnline()) {
            executeLoadingDoacoesTask();
        } else {
            setVisibleNoConnection();
        }
    }

    private void executeLoadingDoacoesTask() {
        if (androidUtil.isOnline()) {

            loadingDoacoesTask = new LoadingDoacoesTask(getContext(), SharedPrefManager.getInstance(getContext()).getUser().getUsername());
            loadingDoacoesTask.delegate = new AsyncResponse<List<DoacaoAdapterDto>>() {
                @Override
                public void processFinish(List<DoacaoAdapterDto> output) {
                    if (output.size() < 1) {
                        showListEmpty();
                    } else {
                        donativos = output;
                        showListDoacoes();
                        donativosAdapter = new DonativosAdapter(donativos, getActivity());
                        recyclerView.setAdapter(donativosAdapter);
                        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), MyDoacoesFragment.this));
                        if (searchView != null) {
                            searchView.setOnQueryTextListener(MyDoacoesFragment.this);
                        }
                    }
                    swipeRefreshLayout.setRefreshing(false);

                }
            };

            loadingDoacoesTask.execute();
        } else {
            setVisibleNoConnection();
            swipeRefreshLayout.setRefreshing(false);

        }
    }

    /**
     * Auxiliar para mostrar fragmento para lista vazia.
     */
    private void showListEmpty() {
        view.findViewById(R.id.no_internet_fragment).setVisibility(View.GONE);
        view.findViewById(R.id.loadingPanelMainSearchCampanha).setVisibility(View.GONE);
        view.findViewById(R.id.containerViewSearchDoacoes).setVisibility(View.GONE);
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


    private void listenCliqueReload() {
        view.findViewById(R.id.loadingPanelMainSearchCampanha).setVisibility(View.VISIBLE);
        //view.findViewById(R.id.containerViewSearchCampanha).setVisibility(View.GONE);
        view.findViewById(R.id.empty_list).setVisibility(View.GONE);
        executeLoadingDoacoesTask();
    }

    /**
     * Auxiliar para mostrar fragmento de sem conexão quando não houver internet no device.
     */
    private void setVisibleNoConnection() {
        view.findViewById(R.id.no_internet_fragment).setVisibility(View.VISIBLE);
        view.findViewById(R.id.loadingPanelMainSearchCampanha).setVisibility(View.GONE);
        if (view.findViewById(R.id.containerViewSearchCampanha) != null) {
            view.findViewById(R.id.containerViewSearchCampanha).setVisibility(View.GONE);
        }
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
    private void showListDoacoes() {
        view.findViewById(R.id.no_internet_fragment).setVisibility(View.GONE);
        view.findViewById(R.id.loadingPanelMainSearchCampanha).setVisibility(View.GONE);
        view.findViewById(R.id.containerViewSearchDoacoes).setVisibility(View.VISIBLE);
        view.findViewById(R.id.empty_list).setVisibility(View.GONE);
    }

    @Override
    public void onRefresh() {
        if (androidUtil.isOnline()) {
            executeLoadingDoacoesTask();
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
     * Filtra donativos pelo nome digitado
     *
     * @param models
     * @param query
     * @return
     */
    private List<DoacaoAdapterDto> filter(List<DoacaoAdapterDto> models, String query) {
        query = query.toLowerCase();
        final List<DoacaoAdapterDto> filteredModelList = new ArrayList<>();
        if (models != null) {
            for (DoacaoAdapterDto model : models) {
                final String text = model.getDonativo().getNome().toLowerCase();

                if (text.contains(query)) {
                    filteredModelList.add(model);
                }
            }
        }
        return filteredModelList;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<DoacaoAdapterDto> filteredModelList = filter(donativos, newText);

        if (filteredModelList.size() < 1) {
            showListEmpty();
        } else {
            showListDoacoes();
            donativosAdapter.setFilter(filteredModelList);
        }
        return true;

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

        if (donativos != null) {
            searchView.setOnQueryTextListener(MyDoacoesFragment.this);
        }
        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        if (donativos != null) {
                            donativosAdapter.setFilter(donativos);
                        }
                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        return true;
                    }
                });

    }


    @Override
    public void onItemClick(View childView, int position) {
        Donativo donativo = donativos.get(position).getDonativo();
        Intent intent = new Intent(getContext(), DonativoActivity.class);
        intent.putExtra("Donativo", donativo);
        startActivity(intent);
    }

    @Override
    public void onItemLongPress(View childView, int position) {

    }


}
