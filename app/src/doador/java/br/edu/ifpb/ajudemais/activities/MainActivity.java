package br.edu.ifpb.ajudemais.activities;

import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import br.edu.ifpb.ajudemais.R;
import br.edu.ifpb.ajudemais.asycnTasks.LoadingDoadorTask;
import br.edu.ifpb.ajudemais.asycnTasks.UpdateLocationDoadorTask;
import br.edu.ifpb.ajudemais.asyncTasks.AsyncResponse;
import br.edu.ifpb.ajudemais.domain.Campanha;
import br.edu.ifpb.ajudemais.domain.Conta;
import br.edu.ifpb.ajudemais.domain.Doador;
import br.edu.ifpb.ajudemais.dto.LatLng;
import br.edu.ifpb.ajudemais.fragments.TabFragmentMain;
import br.edu.ifpb.ajudemais.permissionsPolyce.WriteStoreDevicePermission;
import br.edu.ifpb.ajudemais.storage.SharedPrefManager;


/**
 * <p>
 * <b>{@link MainActivity}</b>
 * </p>
 * <p>
 * Resposável pelas operações da tela inicial do app
 * <p>
 * <p>
 * </p>
 *
 * @author <a href="https://github.com/FranckAJ">Franck Aragão</a>
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */
public class MainActivity extends DrawerMenuActivity implements View.OnClickListener {

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private FloatingActionButton fab;
    private SharedPrefManager sharedPrefManager;
    private LoadingDoadorTask loadingDoadorTask;
    private UpdateLocationDoadorTask updateDoadorTask;


    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initGoogleAPIClient();
        checkPermissions();

        init();
        setUpAccount();
        setUpToggle();
        setupNavDrawer();

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView, new TabFragmentMain()).commit();
        writeStoreDevicePermission = new WriteStoreDevicePermission(getApplicationContext());

        fab.setOnClickListener(this);

        sharedPrefManager = SharedPrefManager.getInstance(getApplicationContext());
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * Modelo default inicial para onRequestPermissionsResult.
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case ACCESS_FINE_LOCATION_INTENT_ID: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (mGoogleApiClient == null) {
                        initGoogleAPIClient();
                        showTurnOnGpsDialog();
                    } else
                        showTurnOnGpsDialog();


                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.permissionDenied), Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    @Override
    public void init() {
        super.init();

        fab = (FloatingActionButton) findViewById(R.id.fab);
    }


    @Override
    public void onClick(View v) {

        updateLocation();

        Intent intent = new Intent();
        intent.setClass(MainActivity.this, MainSearchActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }

    private void updateLocationDoador() {
        Conta conta = sharedPrefManager.getUser();
        loadingDoadorTask = new LoadingDoadorTask(this, conta.getUsername());
        loadingDoadorTask.delegate = new AsyncResponse<Doador>() {
            @Override
            public void processFinish(Doador output) {
                updateLocation();
                LatLng latLng = sharedPrefManager.getLocation();
                updateDoadorTask = new UpdateLocationDoadorTask(MainActivity.this, latLng, output.getId());
                updateDoadorTask.setProgressAtivo(false);
                updateDoadorTask.execute();
            }
        };

        loadingDoadorTask.execute();
    }

    /**
     *
     */
    private void updateLocation() {
        if (mLastLocation == null) {
            startLocationUpdates();
        }
        if (mLastLocation != null) {
            sharedPrefManager.storeLatLng(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
        }
    }



    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                onLocationChanged(getLocation());
                updateLocationDoador();
                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                try {
                    status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);

                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
                break;

            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                break;
        }
    }
}
