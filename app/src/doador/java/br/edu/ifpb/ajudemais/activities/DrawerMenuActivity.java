package br.edu.ifpb.ajudemais.activities;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import br.edu.ifpb.ajudemais.R;
import br.edu.ifpb.ajudemais.asycnTasks.LoadingDoadorTask;
import br.edu.ifpb.ajudemais.asycnTasks.UpdateDoadorTask;
import br.edu.ifpb.ajudemais.asyncTasks.AsyncResponse;
import br.edu.ifpb.ajudemais.asyncTasks.FacebookProfilePictureTask;
import br.edu.ifpb.ajudemais.asyncTasks.UploadImageTask;
import br.edu.ifpb.ajudemais.domain.Conta;
import br.edu.ifpb.ajudemais.domain.Doador;
import br.edu.ifpb.ajudemais.domain.Imagem;
import br.edu.ifpb.ajudemais.permissionsPolyce.WriteStoreDevicePermission;
import br.edu.ifpb.ajudemais.storage.SharedPrefManager;

import static br.edu.ifpb.ajudemais.permissionsPolyce.WriteStoreDevicePermission.MY_PERMISSIONS_REQUEST_STORE_PERMISSION;


/**
 * <p>
 * <b>{@link DrawerMenuActivity}</b>
 * </p>
 * <p>
 * <p>
 * Activity Fornece alguns métodos padrões
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */


public class DrawerMenuActivity extends LocationActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    protected NavigationView mNavigationView;
    private Toolbar mToolbar;
    protected TextView tvUserName;
    protected TextView tvEmail;
    protected ImageView profilePhoto;
    private RelativeLayout componentHeader;
    protected Conta conta;
    protected Bitmap bitmap;
    protected WriteStoreDevicePermission writeStoreDevicePermission;

    /**
     *
     */
    @Override
    public void init() {
        super.init();
        writeStoreDevicePermission = new WriteStoreDevicePermission(this);

        mToolbar = (Toolbar) findViewById(R.id.nav_action);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        mNavigationView = (NavigationView) findViewById(R.id.menuNav);

        View hView = mNavigationView.getHeaderView(0);
        profilePhoto = (ImageView) hView.findViewById(R.id.photoProfile);
        componentHeader = (RelativeLayout) hView.findViewById(R.id.background_header);
        tvUserName = (TextView) hView.findViewById(R.id.tvUserNameProfile);
        tvEmail = (TextView) hView.findViewById(R.id.tvEmailProfile);
        executeLoadingImageProfileTask(this);

        setUpAccount();
        setUpToggle();
        setupNavDrawer();
    }


    /**
     * Set as informações do usuário logado no app
     */
    protected void setUpAccount() {
        conta = (Conta) getIntent().getSerializableExtra("Conta");
        if (conta == null) {
            conta = SharedPrefManager.getInstance(this).getUser();
        }
        if (conta != null) {
            tvUserName.setText(conta.getUsername());
            tvEmail.setText(conta.getEmail());
        }
        if (AccessToken.getCurrentAccessToken() != null) {
            tvUserName.setText(Profile.getCurrentProfile().getName());
        }

        if (getIntent().hasExtra("ImageByteArray") && getIntent().getByteArrayExtra("ImageByteArray") != null) {
            if (writeStoreDevicePermission.isStoragePermissionGranted()) {
                bitmap = androidUtil.convertBytesInBitmap(getIntent().getByteArrayExtra("ImageByteArray"));
                capturePhotoUtils.saveToInternalStorage(bitmap);
            }
        } else {
            bitmap = capturePhotoUtils.loadImageFromStorage();
        }
        if (bitmap != null) {
            profilePhoto.setImageBitmap(bitmap);
        }
        callActivityEditProfile();
    }


    /**
     * Chama a activity edit profile ao clicar no header no menu drawer.
     */
    private void callActivityEditProfile() {
        componentHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(DrawerMenuActivity.this, ProfileSettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

    }


    /**
     * Recupera a localização do device
     *
     * @return
     */
    protected Location getLocation() {

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (mLastLocation == null) {
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                String provider = locationManager.getBestProvider(criteria, false);
                mLastLocation = locationManager.getLastKnownLocation(provider);

            } else {
                Log.e("LOCATION", "lt:" + mLastLocation.getLatitude());

            }
        }

        return mLastLocation;
    }


    /**
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return mToggle.onOptionsItemSelected(item);

    }

    /**
     * Set Action Bar na tela de exibição.
     */
    protected void setUpToggle() {
        setSupportActionBar(mToolbar);

        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    /**
     * Inicia serviço Google API client
     */
    protected void initGoogleAPIClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
    }


    /*** Set Configuração para Navegation Drawer
     * */
    protected void setupNavDrawer() {
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            if (mNavigationView != null && mDrawerLayout != null) {

                mNavigationView.setNavigationItemSelectedListener(
                        new NavigationView.OnNavigationItemSelectedListener() {
                            @Override
                            public boolean onNavigationItemSelected(MenuItem menuItem) {
                                menuItem.setChecked(true);
                                mDrawerLayout.closeDrawers();
                                onNavDrawerItemSelected(menuItem);
                                return true;
                            }
                        });
            }
        }
    }


    /**
     * @param menuItem
     */
    private void onNavDrawerItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_config:
                break;
            case R.id.nav_notificacoes:
                break;
            case R.id.nav_sair:
                if (AccessToken.getCurrentAccessToken() != null) {
                    LoginManager.getInstance().logOut();
                }
                Intent intent = new Intent();
                intent.setClass(DrawerMenuActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                SharedPrefManager.getInstance(this).clearSharedPrefs();
                capturePhotoUtils.deleteImageProfile();
                break;

        }

    }


    /**
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }


    /**
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
                    Toast.makeText(DrawerMenuActivity.this, getString(R.string.permissionDenied), Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case MY_PERMISSIONS_REQUEST_STORE_PERMISSION: {
                if (getIntent().hasExtra("ImageByteArray") && getIntent().getByteArrayExtra("ImageByteArray") != null) {
                    bitmap = androidUtil.convertBytesInBitmap(getIntent().getByteArrayExtra("ImageByteArray"));
                    capturePhotoUtils.saveToInternalStorage(bitmap);
                    profilePhoto.setImageBitmap(bitmap);
                } else if (getIntent().hasExtra("ProfilePic") && getIntent().getExtras().get("ProfilePic") != null) {
                    bitmap = (Bitmap) getIntent().getExtras().get("ProfilePic");
                    capturePhotoUtils.saveToInternalStorage(bitmap);
                    profilePhoto.setImageBitmap(bitmap);
                }
                break;
            }
        }
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
                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                try {
                    status.startResolutionForResult(DrawerMenuActivity.this, REQUEST_CHECK_SETTINGS);

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

            }
        }
    }


    /**
     * Executa Asycn task para recuperar foto do facebook
     *
     * @param context
     */
    private void executeLoadingImageProfileTask(final Context context) {

        if (AccessToken.getCurrentAccessToken() != null && !getIntent().hasExtra("ImageByteArray")) {
            Bitmap bitmap = capturePhotoUtils.loadImageFromStorage();
            if (bitmap == null) {
                FacebookProfilePictureTask loadingImageFbTask = new FacebookProfilePictureTask();
                loadingImageFbTask.delegate = new AsyncResponse<Bitmap>() {
                    @Override
                    public void processFinish(Bitmap output) {
                        if (output != null) {
                            profilePhoto.setImageBitmap(output);
                            writeStoreDevicePermission = new WriteStoreDevicePermission(DrawerMenuActivity.this);
                            if (writeStoreDevicePermission.isStoragePermissionGranted()) {
                                capturePhotoUtils.saveToInternalStorage(output);
                            }
                            executeUpdateImageTask(androidUtil.converteBitmapInBytesArray(output));
                        }
                    }
                };
                loadingImageFbTask.execute();

            } else {
                profilePhoto.setImageBitmap(bitmap);
            }
        }
    }

    /**
     * Executa asycn task para atualizar imagem do doador
     *
     * @param imageBytes
     */
    private void executeUpdateImageTask(byte[] imageBytes) {
        UploadImageTask uploadImageTask = new UploadImageTask(this, imageBytes);
        uploadImageTask.setActiveProgress(false);
        uploadImageTask.delegate = new AsyncResponse<Imagem>() {
            @Override
            public void processFinish(Imagem output) {
                executeGetAndUpdateDoadorTask(output);
            }
        };

        uploadImageTask.execute();
    }

    /**
     * Executa Asycn Task para recuperar Doador
     */
    private void executeGetAndUpdateDoadorTask(final Imagem imagem) {
        LoadingDoadorTask loadingDoadorTask = new LoadingDoadorTask(this, SharedPrefManager.getInstance(this).getUser().getUsername());
        loadingDoadorTask.setProgressAtivo(false);
        loadingDoadorTask.delegate = new AsyncResponse<Doador>() {
            @Override
            public void processFinish(final Doador output) {
                if (output != null) {
                    if (output.getFoto() != null) {
                        output.getFoto().setNome(imagem.getNome());

                    } else {
                        output.setFoto(imagem);
                    }

                    UpdateDoadorTask updateDoadorTask = new UpdateDoadorTask(DrawerMenuActivity.this, output);
                    updateDoadorTask.setProgressAtivo(false);
                    updateDoadorTask.delegate = new AsyncResponse<Doador>() {
                        @Override
                        public void processFinish(Doador output) {
                        }
                    };
                    updateDoadorTask.execute();
                }
            }
        };

        loadingDoadorTask.execute();
    }
}
