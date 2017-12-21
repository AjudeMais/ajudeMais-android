package br.edu.ifpb.ajudemais.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import br.edu.ifpb.ajudemais.R;
import br.edu.ifpb.ajudemais.domain.InstituicaoCaridade;
import br.edu.ifpb.ajudemais.fragments.InstituicaoDetailFragment;
import br.edu.ifpb.ajudemais.permissionsPolyce.CallPhoneDevicePermission;
import static br.edu.ifpb.ajudemais.permissionsPolyce.CallPhoneDevicePermission.MY_PERMISSIONS_REQUEST_CALL_PHONE_PERMISSION;

/**
 * <p>
 * <b>{@link InstituicaoActivity}</b>
 * </p>
 * <p>
 * Activity para controlar tela Instituicoes
 * <p>
 * <p>
 * </p>
 *
 * @author <a href="https://github.com/FranckAJ">Franck Aragão</a>
 */
public class InstituicaoActivity extends BaseActivity implements View.OnClickListener{

    private InstituicaoCaridade instituicao;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private FloatingActionButton fab;
    private Toolbar mToolbar;
    private CallPhoneDevicePermission callPhoneDevicePermission;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instituicao);

        init();

        if (!isDestroyed()) {
            InstituicaoDetailFragment fragment = new InstituicaoDetailFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.instituicao_detail_fragment, fragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void init() {
        initProperties();
        callPhoneDevicePermission = new CallPhoneDevicePermission(this);
        mToolbar = (Toolbar) findViewById(R.id.nav_action);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        instituicao = (InstituicaoCaridade) getIntent().getSerializableExtra("instituicao");

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(instituicao.getNome());

        fab = (FloatingActionButton) findViewById(R.id.fabInstituicaoTel);
        fab.setOnClickListener(this);

    }



    /**
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainSearchActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId()== R.id.fabInstituicaoTel){
           if (callPhoneDevicePermission.isCallPhonePermissionGranted()){
               callPhoneDevicePermission.callPhone(instituicao.getTelefone());
           }
        }
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
            case MY_PERMISSIONS_REQUEST_CALL_PHONE_PERMISSION : {
               if (callPhoneDevicePermission.isCallPhonePermissionGranted()){
                   callPhoneDevicePermission.callPhone(instituicao.getTelefone());
               }
                break;
            }
        }
    }

}
