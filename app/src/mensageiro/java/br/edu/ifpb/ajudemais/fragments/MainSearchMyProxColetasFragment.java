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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifpb.ajudemais.R;
import br.edu.ifpb.ajudemais.activities.DetailSolicitacaoActivity;
import br.edu.ifpb.ajudemais.adapters.DonativosAdapter;
import br.edu.ifpb.ajudemais.asycnTasks.LoadingDonativoByMensageiroEstadoTask;
import br.edu.ifpb.ajudemais.asyncTasks.AsyncResponse;
import br.edu.ifpb.ajudemais.domain.Donativo;
import br.edu.ifpb.ajudemais.dto.DoacaoAdapterDto;
import br.edu.ifpb.ajudemais.listeners.RecyclerItemClickListener;
import br.edu.ifpb.ajudemais.storage.SharedPrefManager;
import br.edu.ifpb.ajudemais.utils.AndroidUtil;

/**
 *
 */
public class MainSearchMyProxColetasFragment extends Fragment implements RecyclerItemClickListener.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, SearchView.OnQueryTextListener {

    private DonativosAdapter donativosAdapter;
    private static RecyclerView recyclerView;
    private static View view;
    private List<DoacaoAdapterDto> donativos;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AndroidUtil androidUtil;
    private LoadingDonativoByMensageiroEstadoTask loadingDonativoByMensageiroEstadoTask;
    private RecyclerItemClickListener.OnItemClickListener clickListener;
    private SearchView searchView;
    private SharedPrefManager sharedPrefManager;

    /**
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    /**
     *
     */
    @Override
    public void onStart() {
        super.onStart();
        this.clickListener = this;
        androidUtil = new AndroidUtil(getContext());

        if (androidUtil.isOnline()) {
            executeLoadingDonativosTask();
        } else {
            setVisibleNoConnection();
        }
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

        view = inflater.inflate(R.layout.fragment_main_search_my_coletas, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view_list_my_coletas);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        view.findViewById(R.id.no_internet_fragment).setVisibility(View.GONE);

        swipeRefreshLayout.setOnRefreshListener(this);

        view.findViewById(R.id.loading_panel_my_coletas).setVisibility(View.VISIBLE);
        view.findViewById(R.id.container_fragment_my_coletas).setVisibility(View.GONE);
        view.findViewById(R.id.empty_list).setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onRefresh() {
        if (androidUtil.isOnline()) {
            executeLoadingDonativosTask();
        } else {
            setVisibleNoConnection();
            swipeRefreshLayout.setRefreshing(false);
        }
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
    public void onItemClick(View childView, int position) {
        Donativo donativo = donativos.get(position).getDonativo();
        Intent intent = new Intent(getContext(), DetailSolicitacaoActivity.class);
        intent.putExtra("Donativo", donativo);
        startActivity(intent);
    }

    @Override
    public void onItemLongPress(View childView, int position) {

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<DoacaoAdapterDto> filteredModelList = filter(donativos, newText);

        if (filteredModelList.size() < 1) {
            showListEmpty();
        } else {
            showListDonativos();
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

        inflater.inflate(R.menu.menu_search, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(item);

        if (donativos != null) {
            searchView.setOnQueryTextListener(MainSearchMyProxColetasFragment.this);
        }
        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        donativosAdapter.setFilter(donativos);
                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        return true;
                    }
                });

    }

    /**
     * Auxiliar para mostrar fragmento para lista vazia.
     */
    private void showListEmpty() {
        view.findViewById(R.id.no_internet_fragment).setVisibility(View.GONE);
        view.findViewById(R.id.loading_panel_my_coletas).setVisibility(View.GONE);
        view.findViewById(R.id.container_fragment_my_coletas).setVisibility(View.GONE);
        view.findViewById(R.id.empty_list).setVisibility(View.VISIBLE);
    }

    /**
     * Auxiliar para mostrar fragmento de sem conexão quando não houver internet no device.
     */
    private void setVisibleNoConnection() {
        view.findViewById(R.id.no_internet_fragment).setVisibility(View.VISIBLE);
        view.findViewById(R.id.loading_panel_my_coletas).setVisibility(View.GONE);
        view.findViewById(R.id.container_fragment_my_coletas).setVisibility(View.GONE);
        view.findViewById(R.id.empty_list).setVisibility(View.GONE);
    }

    /**
     * Auxiliar para mostrar lista de campanhas e esconder demais fragmentos.
     */
    private void showListDonativos() {
        view.findViewById(R.id.no_internet_fragment).setVisibility(View.GONE);
        view.findViewById(R.id.loading_panel_my_coletas).setVisibility(View.GONE);
        view.findViewById(R.id.container_fragment_my_coletas).setVisibility(View.VISIBLE);
        view.findViewById(R.id.empty_list).setVisibility(View.GONE);
    }

    private void executeLoadingDonativosTask() {
        if (androidUtil.isOnline()) {
            sharedPrefManager = SharedPrefManager.getInstance(getContext());
            String username = sharedPrefManager.getUser().getUsername();
            loadingDonativoByMensageiroEstadoTask = new LoadingDonativoByMensageiroEstadoTask(getContext(), username);
            loadingDonativoByMensageiroEstadoTask.delegate = new AsyncResponse<List<DoacaoAdapterDto>>() {

                @Override
                public void processFinish(List<DoacaoAdapterDto> output) {
                    if (output.size() < 1) {
                        showListEmpty();

                    } else {
                        donativos = output;
                        showListDonativos();
                        donativosAdapter = new DonativosAdapter(donativos, getActivity());
                        recyclerView.setAdapter(donativosAdapter);
                        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), clickListener));
                    }
                    swipeRefreshLayout.setRefreshing(false);

                }
            };

            loadingDonativoByMensageiroEstadoTask.execute();
        } else {
            setVisibleNoConnection();
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
