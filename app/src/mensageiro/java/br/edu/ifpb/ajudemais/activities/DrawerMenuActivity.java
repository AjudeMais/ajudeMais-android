package br.edu.ifpb.ajudemais.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import br.edu.ifpb.ajudemais.R;
import br.edu.ifpb.ajudemais.domain.Conta;
import br.edu.ifpb.ajudemais.permissionsPolyce.WriteStoreDevicePermission;
import br.edu.ifpb.ajudemais.storage.SharedPrefManager;

import static br.edu.ifpb.ajudemais.permissionsPolyce.WriteStoreDevicePermission.MY_PERMISSIONS_REQUEST_STORE_PERMISSION;


/**
 * <p>
 * <b>{@link DrawerMenuActivity}</b>
 * </p>
 * <p>
 * <p>
 * Activity para lidar com drawer menu de mensageiro
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */


public class DrawerMenuActivity extends BaseActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {


    protected NavigationView mNavigationView;
    protected DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;

    protected Conta conta;
    protected TextView tvUserName;
    protected TextView tvEmail;
    protected Bitmap bitmap;
    protected ImageView profilePhoto;
    protected RelativeLayout componentHeader;
    protected WriteStoreDevicePermission writeStoreDevicePermission;

    /**
     * Set as informações do usuário logado no app
     */
    protected void setUpAccount() {
        View hView = mNavigationView.getHeaderView(0);
        componentHeader = (RelativeLayout) hView.findViewById(R.id.background_header);
        profilePhoto = (ImageView) hView.findViewById(R.id.photoProfile);
        tvUserName = (TextView) hView.findViewById(R.id.tvUserNameProfile);
        tvEmail = (TextView) hView.findViewById(R.id.tvEmailProfile);

        conta = (Conta) getIntent().getSerializableExtra("Conta");

        if (conta == null) {
            conta = SharedPrefManager.getInstance(this).getUser();
        }

        if (conta != null) {
            tvUserName.setText(conta.getUsername());
            tvEmail.setText(conta.getEmail());
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

        componentHeader.setOnClickListener(this);
    }


    /**
     * @param menuItem
     */
    private void onNavDrawerItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_my_addresses:
                Intent intent = new Intent();
                intent.setClass(DrawerMenuActivity.this, MyEnderecosActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.nav_sair:
                SharedPrefManager.getInstance(this).clearSharedPrefs();
                capturePhotoUtils.deleteImageProfile();
                Intent intent2 = new Intent();
                intent2.setClass(DrawerMenuActivity.this, LoginActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(intent2);
                break;

        }

    }

    /**
     * Set Configuração para Navegation Drawer
     */
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
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return mToggle.onOptionsItemSelected(item);

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
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


    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setClass(DrawerMenuActivity.this, ProfileSettingsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void init() {
        initProperties();

        writeStoreDevicePermission = new WriteStoreDevicePermission(this);
        mToolbar = (Toolbar) findViewById(R.id.nav_action);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        mNavigationView = (NavigationView) findViewById(R.id.menuNav);

        View hView = mNavigationView.getHeaderView(0);
        profilePhoto = (ImageView) hView.findViewById(R.id.photoProfile);
        componentHeader = (RelativeLayout) hView.findViewById(R.id.background_header);
        tvUserName = (TextView) hView.findViewById(R.id.tvUserNameProfile);
        tvEmail = (TextView) hView.findViewById(R.id.tvEmailProfile);

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
            case MY_PERMISSIONS_REQUEST_STORE_PERMISSION: {
                if (getIntent().hasExtra("ImageByteArray") && getIntent().getByteArrayExtra("ImageByteArray") != null) {
                    bitmap = androidUtil.convertBytesInBitmap(getIntent().getByteArrayExtra("ImageByteArray"));
                    capturePhotoUtils.saveToInternalStorage(bitmap);
                    profilePhoto.setImageBitmap(bitmap);
                }
                break;
            }
        }
    }

}


