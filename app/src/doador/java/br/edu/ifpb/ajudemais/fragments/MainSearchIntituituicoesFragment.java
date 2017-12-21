package br.edu.ifpb.ajudemais.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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
import android.widget.Button;
import android.widget.TextView;

import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifpb.ajudemais.R;
import br.edu.ifpb.ajudemais.activities.InstituicaoActivity;
import br.edu.ifpb.ajudemais.adapters.InstituicoesAdapter;
import br.edu.ifpb.ajudemais.domain.InstituicaoCaridade;
import br.edu.ifpb.ajudemais.dto.LatLng;
import br.edu.ifpb.ajudemais.listeners.RecyclerItemClickListener;
import br.edu.ifpb.ajudemais.remoteServices.InstituicaoRemoteService;
import br.edu.ifpb.ajudemais.storage.SharedPrefManager;
import br.edu.ifpb.ajudemais.utils.AndroidUtil;
import br.edu.ifpb.ajudemais.utils.CustomToast;

/**
 * <p>
 * <b>MainSearchIntituituicoesFragment</b>
 * </p>
 * <p>
 * MainSearchIntituituicoesFragment para pesquisa de formas de doar
 * <p>
 * <p>
 * </p>
 *
 * @author <a href="https://github.com/FranckAJ">Franck Aragão</a>
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */
public class MainSearchIntituituicoesFragment extends Fragment implements RecyclerItemClickListener.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, SearchView.OnQueryTextListener {

    private InstituicoesAdapter instituicoesAdapter;
    private static RecyclerView recyclerView;
    private static View view;
    private List<InstituicaoCaridade> instituicoes;
    private LatLng latLng;
    private Location mLastLocation;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AndroidUtil androidUtil;
    private SearchView searchView;

    /**
     *
     */
    public MainSearchIntituituicoesFragment() {
    }

    /**
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_search_inst, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        view.findViewById(R.id.no_internet_fragment).setVisibility(View.GONE);

        swipeRefreshLayout.setOnRefreshListener(this);

        view.findViewById(R.id.loadingPanelMainSearchInst).setVisibility(View.VISIBLE);
        view.findViewById(R.id.containerViewSearchInst).setVisibility(View.GONE);
        view.findViewById(R.id.empty_list).setVisibility(View.GONE);

        return view;
    }

    /**
     *
     */
    @Override
    public void onStart() {
        super.onStart();
        androidUtil = new AndroidUtil(getContext());
        new MainSearchInstituicoesFragmentTask(this).execute();
        mLastLocation = getUpdateLocation();
    }

    /**
     * @param childView View of the item that was clicked.
     * @param position  Position of the item that was clicked.
     */
    @Override
    public void onItemClick(View childView, int position) {
        InstituicaoCaridade instituicaoCaridade = instituicoes.get(position);

        Intent intent = new Intent(getContext(), InstituicaoActivity.class);
        intent.putExtra("instituicao", instituicaoCaridade);
        startActivity(intent);

    }

    /**
     * Pega a Localização atual do device.
     *
     * @return
     */
    private Location getUpdateLocation() {
        LocationManager locationManager = null;

        if (Context.LOCATION_SERVICE != null) {
            locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        }

        if (locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }

        }

        return mLastLocation;
    }

    /**
     * @param childView View of the item that was long pressed.
     * @param position  Position of the item that was long pressed.
     */
    @Override
    public void onItemLongPress(View childView, int position) {

    }

    /**
     *
     */
    @Override
    public void onRefresh() {
        if (androidUtil.isOnline()) {
            new MainSearchInstituicoesFragmentTask(this).execute();
        } else {
            setVisibleNoConnection();
            swipeRefreshLayout.setRefreshing(false);

        }

    }

    /**
     * Auxiliar para mostrar fragmento de sem conexão quando não houver internet no device.
     */
    public void setVisibleNoConnection() {
        view.findViewById(R.id.no_internet_fragment).setVisibility(View.VISIBLE);
        view.findViewById(R.id.loadingPanelMainSearchInst).setVisibility(View.GONE);
        view.findViewById(R.id.containerViewSearchInst).setVisibility(View.GONE);
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
                        if (instituicoes != null) {
                            searchView.setOnQueryTextListener(MainSearchIntituituicoesFragment.this);
                        }
                        instituicoesAdapter.setFilter(instituicoes);

                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        return true; // Return true to expand action view
                    }
                });
    }

    /**
     * @param query
     * @return
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    /**
     * Auxiliar para mostrar fragmento para lista vazia.
     */
    private void showListEmpty() {
        view.findViewById(R.id.no_internet_fragment).setVisibility(View.GONE);
        view.findViewById(R.id.loadingPanelMainSearchInst).setVisibility(View.GONE);
        view.findViewById(R.id.containerViewSearchInst).setVisibility(View.GONE);
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
        if (view.findViewById(R.id.loadingPanelMainSearchCampanha) != null) {
            view.findViewById(R.id.loadingPanelMainSearchCampanha).setVisibility(View.VISIBLE);
        }
        if (view.findViewById(R.id.containerViewSearchCampanha) != null) {
            view.findViewById(R.id.containerViewSearchCampanha).setVisibility(View.GONE);
        }
        view.findViewById(R.id.empty_list).setVisibility(View.GONE);
        new MainSearchInstituicoesFragmentTask(this).execute();

    }

    /**
     * Auxiliar para mostrar lista de insituições e esconder demais fragmentos.
     */
    private void showListInstituicoes() {
        view.findViewById(R.id.no_internet_fragment).setVisibility(View.GONE);
        view.findViewById(R.id.loadingPanelMainSearchInst).setVisibility(View.GONE);
        view.findViewById(R.id.containerViewSearchInst).setVisibility(View.VISIBLE);
        view.findViewById(R.id.empty_list).setVisibility(View.GONE);
    }

    /**
     * @param newText
     * @return
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        final List<InstituicaoCaridade> filteredModelList = filter(instituicoes, newText);

        if (filteredModelList.size() < 1) {
            showListEmpty();
        } else {
            showListInstituicoes();
            instituicoesAdapter.setFilter(filteredModelList);

        }
        return true;
    }


    /**
     * Filtra na lista de instituição pelo nome digitado
     *
     * @param models
     * @param query
     * @return
     */
    private List<InstituicaoCaridade> filter(List<InstituicaoCaridade> models, String query) {
        query = query.toLowerCase();
        final List<InstituicaoCaridade> filteredModelList = new ArrayList<>();
        if (models != null) {
            for (InstituicaoCaridade model : models) {
                final String text = model.getNome().toLowerCase();

                if (text.contains(query)) {
                    filteredModelList.add(model);
                }
            }
        }
        return filteredModelList;
    }

    /**
     *
     */
    private class MainSearchInstituicoesFragmentTask extends AsyncTask<Void, Void, List<InstituicaoCaridade>> {

        private InstituicaoRemoteService instituicaoRemoteService;
        private String message = null;
        private List<InstituicaoCaridade> instituicoesResult;
        private RecyclerItemClickListener.OnItemClickListener clickListener;
        private SharedPrefManager sharedPrefManager;


        public MainSearchInstituicoesFragmentTask(RecyclerItemClickListener.OnItemClickListener clickListener) {
            instituicaoRemoteService = new InstituicaoRemoteService(getContext());
            this.clickListener = clickListener;
            sharedPrefManager = new SharedPrefManager(getContext());
        }

        /**
         * @param voids
         * @return
         */
        @Override
        protected List<InstituicaoCaridade> doInBackground(Void... voids) {

            try {
                if (androidUtil.isOnline()) {

                    latLng = sharedPrefManager.getLocation();

                    mLastLocation = getUpdateLocation();
                    if (mLastLocation != null) {
                        latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    }

                    if (latLng != null) {
                        instituicoesResult = instituicaoRemoteService.postInstituicoesForLocation(latLng);

                    } else {
                        instituicoesResult = instituicaoRemoteService.getInstituicoesAtivas();
                    }
                } else {
                    setVisibleNoConnection();
                }
            } catch (RestClientException e) {
                message = e.getMessage();
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return instituicoesResult;
        }

        /**
         * @param result
         */
        @Override
        protected void onPostExecute(List<InstituicaoCaridade> result) {
            if (result != null) {

                if (result.size() < 1) {
                    showListEmpty();

                } else {
                    instituicoes = result;
                    showListInstituicoes();

                    instituicoesAdapter = new InstituicoesAdapter(instituicoes, getActivity());
                    recyclerView.setAdapter(instituicoesAdapter);
                    recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), clickListener));
                    if (searchView != null) {
                        searchView.setOnQueryTextListener(MainSearchIntituituicoesFragment.this);
                    }
                }
            } else {
                showResult(message);
            }

            swipeRefreshLayout.setRefreshing(false);

        }

        /**
         * @param result
         */
        private void showResult(String result) {
            if (result != null) {
                if (getContext() != null) {
                    CustomToast.getInstance(getContext()).createSuperToastSimpleCustomSuperToast(result);
                }
            } else {
                if (getContext() != null) {
                    CustomToast.getInstance(getContext()).createSuperToastSimpleCustomSuperToast("Aconteceu algum erro no servidor!");
                }
            }
        }
    }
}
