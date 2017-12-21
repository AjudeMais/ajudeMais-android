package br.edu.ifpb.ajudemais.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import br.edu.ifpb.ajudemais.R;
import br.edu.ifpb.ajudemais.adapters.EnderecoAdapter;
import br.edu.ifpb.ajudemais.asycnTasks.LoadingMensageiroTask;
import br.edu.ifpb.ajudemais.asycnTasks.UpdateMensageiroTask;
import br.edu.ifpb.ajudemais.asyncTasks.AsyncResponse;
import br.edu.ifpb.ajudemais.asyncTasks.FindByMyLocationActualTask;
import br.edu.ifpb.ajudemais.domain.Endereco;
import br.edu.ifpb.ajudemais.domain.Mensageiro;
import br.edu.ifpb.ajudemais.dto.LatLng;
import br.edu.ifpb.ajudemais.listeners.RecyclerItemClickListener;
import br.edu.ifpb.ajudemais.storage.SharedPrefManager;
import br.edu.ifpb.ajudemais.utils.CustomToast;

/**
 * <p>
 * <b>{@link MyEnderecosActivity}</b>
 * </p>
 * <p>
 * <p>
 * Activity para gerenciar endereços de mensageiro.
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */
public class MyEnderecosActivity extends LocationActivity implements RecyclerItemClickListener.OnItemClickListener {

    private Toolbar mToolbar;
    private RecyclerView recyclerView;
    private Mensageiro mensageiro;
    private Integer position;
    private FloatingActionButton fab;
    private FindByMyLocationActualTask findByMyLocationActualTask;
    private LoadingMensageiroTask loadingMensageiroTask;
    private UpdateMensageiroTask updateMensageiroTask;
    private RelativeLayout componentNoInternet;
    private FrameLayout componentListEmpty;
    private RelativeLayout componentView;

    private Toast toast;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_enderecos);

        init();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogNewAddress();
            }
        });
    }

    @Override
    public void init() {
        super.init();
        initGoogleAPIClient();

        mToolbar = (Toolbar) findViewById(R.id.nav_action);
        mToolbar.setTitle(getString(R.string.myaddress));

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.recycle_view_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, this));

        componentNoInternet = (RelativeLayout) findViewById(R.id.no_internet_fragment);
        componentListEmpty = (FrameLayout) findViewById(R.id.empty_list) ;
        componentView = (RelativeLayout) findViewById(R.id.loadingPanelMainSearchInst);

        componentNoInternet.setVisibility(View.GONE);

        recyclerView.setVisibility(View.VISIBLE);
        findViewById(R.id.empty_list).setVisibility(View.GONE);

        fab = (FloatingActionButton) findViewById(R.id.btnNewAddress);

    }

    /**
     *
     */
    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }

        if (androidUtil.isOnline()) {
            executeLoadingMensageiroTask();
        } else {
            setVisibleNoConnection();
        }
    }

    private void executeLoadingMensageiroTask() {
        this.loadingMensageiroTask = new LoadingMensageiroTask(this, SharedPrefManager.getInstance(this).getUser().getUsername());
        this.loadingMensageiroTask.delegate = new AsyncResponse<Mensageiro>() {
            @Override
            public void processFinish(Mensageiro output) {
                mensageiro = output;

                if (mensageiro.getEnderecos().size() > 0) {
                    recyclerView.setHasFixedSize(true);

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    EnderecoAdapter enderecoAdapter = new EnderecoAdapter(mensageiro.getEnderecos(), getApplicationContext());
                    recyclerView.setAdapter(enderecoAdapter);

                    showListEnderecos();

                } else {
                    showListEmpty();

                }
            }
        };

        loadingMensageiroTask.execute();

    }

    /**
     * Executa Task que atualiza o mensageiro e remove endereço
     */
    private void executeTaskRemoveEndereco(Mensageiro mensageiroUp) {
        updateMensageiroTask = new UpdateMensageiroTask(this, mensageiroUp);
        updateMensageiroTask.delegate = new AsyncResponse<Mensageiro>() {
            @Override
            public void processFinish(Mensageiro output) {
                mensageiro = output;
                EnderecoAdapter enderecoAdapter = new EnderecoAdapter(mensageiro.getEnderecos(), MyEnderecosActivity.this);
                recyclerView.setAdapter(enderecoAdapter);

                CustomToast.getInstance(MyEnderecosActivity.this).createSuperToastSimpleCustomSuperToast(getString(R.string.deletedAddress));

                if (mensageiro.getEnderecos().size() > 0) {
                    showListEnderecos();
                } else {
                    showListEmpty();
                }


            }
        };
        updateMensageiroTask.execute();

    }


    /**
     * Auxiliar para mostrar fragmento de sem conexão quando não houver internet no device.
     */
    public void setVisibleNoConnection() {
        componentNoInternet.setVisibility(View.VISIBLE);
        componentView.setVisibility(View.GONE);
        componentListEmpty.setVisibility(View.GONE);
    }


    /**
     * Auxiliar para mostrar lista de endereços e esconder demais fragmentos.
     */
    private void showListEnderecos() {
        componentNoInternet.setVisibility(View.GONE);
        componentView.setVisibility(View.GONE);
        componentListEmpty.setVisibility(View.GONE);
    }

    /**
     * Auxiliar para mostrar fragmento para lista vazia.
     */
    private void showListEmpty() {
        componentNoInternet.setVisibility(View.GONE);
        componentView.setVisibility(View.GONE);
        componentListEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(View childView, int position) {
        this.position = position;
        openDialogEditAddress();
    }

    @Override
    public void onItemLongPress(View childView, int position) {

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
                Intent intent = new Intent(MyEnderecosActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Dialog para seleção da opção para adicionar um novo endereço pela localização do device ou informando manualmente.
     */
    private void openDialogNewAddress() {
        final CharSequence[] items = {getString(R.string.tv_my_location), getString(R.string.tv_insert_manualy), getString(R.string.cancelar)};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.selectOptinForNewAddress));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals(getString(R.string.tv_my_location))) {
                    checkPermissions();
                    if (mLastLocation != null) {
                        runTaskLocation();
                    }
                } else if (items[item].equals(getString(R.string.tv_insert_manualy))) {
                    Intent intent = new Intent(getApplicationContext(), EnderecoActivity.class);
                    intent.putExtra("Mensageiro", mensageiro);
                    startActivity(intent);
                } else if (items[item].equals(getString(R.string.cancelar))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    /**
     * Seta propriedades e executa Task para get location
     */
    private void runTaskLocation() {
        startLocationUpdates();
        if (mLastLocation == null) {
            mLastLocation = getLocation();
        }

        if (mLastLocation != null) {
            findByMyLocationActualTask = new FindByMyLocationActualTask(MyEnderecosActivity.this, new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
            findByMyLocationActualTask.delegate = new AsyncResponse<Endereco>() {
                @Override
                public void processFinish(Endereco output) {
                    Intent intent = new Intent(getApplicationContext(), EnderecoActivity.class);
                    intent.putExtra("Mensageiro", mensageiro);
                    intent.putExtra("Endereco", output);
                    startActivity(intent);
                }
            };
            findByMyLocationActualTask.execute();
        }
    }

    /**
     * Dialog para seleção da opção de editar ou remover endereço.
     */
    private void openDialogEditAddress() {
        final CharSequence[] items = {getString(R.string.tv_edit), getString(R.string.tv_delete), getString(R.string.cancelar)};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.selectOption));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals(getString(R.string.tv_edit))) {
                    Intent intent = new Intent(getApplicationContext(), EnderecoActivity.class);
                    intent.putExtra("Mensageiro", mensageiro);
                    intent.putExtra("Index", position);
                    startActivity(intent);

                } else if (items[item].equals(getString(R.string.tv_delete))) {
                    Endereco endereco = mensageiro.getEnderecos().get(position);
                    mensageiro.getEnderecos().remove(endereco);
                    executeTaskRemoveEndereco(mensageiro);

                } else if (items[item].equals(getString(R.string.cancelar))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    /**
     * @param locationSettingsResult
     */
    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                onLocationChanged(getLocation());
                runTaskLocation();
                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                try {
                    status.startResolutionForResult(MyEnderecosActivity.this, REQUEST_CHECK_SETTINGS);

                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
                break;

            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                break;
        }
    }

    /**
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {

            if (resultCode == RESULT_OK) {
                mLastLocation = LocationServices.FusedLocationApi
                        .getLastLocation(mGoogleApiClient);

                runTaskLocation();

            }
        }
    }

}
