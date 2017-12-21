package br.edu.ifpb.ajudemais.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import br.edu.ifpb.ajudemais.R;
import br.edu.ifpb.ajudemais.asyncTasks.AsyncResponse;
import br.edu.ifpb.ajudemais.asyncTasks.GetImageTask;
import br.edu.ifpb.ajudemais.domain.Donativo;
import br.edu.ifpb.ajudemais.domain.EstadoDoacao;
import br.edu.ifpb.ajudemais.enumarations.Estado;
import br.edu.ifpb.ajudemais.fragments.DetailSolicitacoesFragment;
import br.edu.ifpb.ajudemais.permissionsPolyce.CallPhoneDevicePermission;

import static br.edu.ifpb.ajudemais.permissionsPolyce.CallPhoneDevicePermission.MY_PERMISSIONS_REQUEST_CALL_PHONE_PERMISSION;

public class DetailSolicitacaoActivity extends BaseActivity implements View.OnClickListener {

    private Donativo donativo;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private FloatingActionButton fab;
    private Toolbar mToolbar;
    private GetImageTask getImageTask;
    private ImageView imageHeader;
    private ProgressBar progressBar;
    private Boolean notification;
    private CallPhoneDevicePermission callPhoneDevicePermission;
    private byte[] image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        donativo = (Donativo) getIntent().getSerializableExtra("Donativo");

        notification = (Boolean) getIntent().getSerializableExtra("notification");

        if (validateDonativo()) {

            setContentView(R.layout.fragment_solicitacao_not_disponivel);
            Toolbar mToolbar = (Toolbar) findViewById(R.id.nav_action);
            mToolbar.setTitle(getString(R.string.invalide_coleta));

            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        } else {
            setContentView(R.layout.activity_detail_solicitacao);
            init();

            if (savedInstanceState == null) {
                if (getFragmentManager().findFragmentByTag("detailSolcitacoes") == null) {
                    DetailSolicitacoesFragment fragment = new DetailSolicitacoesFragment();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.add(R.id.donativo_detail_fragment, fragment, "detailSolcitacoes");
                    fragmentTransaction.commit();
                }
            }
        }

    }

    /**
     * @return
     */
    private boolean validateDonativo() {
        if (notification != null && getEstadoAtivo() != null) {
            if (getEstadoAtivo().getEstadoDoacao().equals(Estado.CANCELADO) ||
                    getEstadoAtivo().getEstadoDoacao().equals(Estado.NAO_ACEITO)) {
                return true;
            }
        }

        return false;
    }

    private EstadoDoacao getEstadoAtivo() {
        for (EstadoDoacao estadoDoacao : donativo.getEstadosDaDoacao()) {
            if (estadoDoacao.getAtivo()) {
                return estadoDoacao;
            }
        }

        return null;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void init() {
        initProperties();
        callPhoneDevicePermission = new CallPhoneDevicePermission(this);
        mToolbar = (Toolbar) findViewById(R.id.nav_action);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progressBar = (ProgressBar) findViewById(R.id.progress_presentation);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#FFFFFF"),
                android.graphics.PorterDuff.Mode.MULTIPLY);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(donativo.getNome());
        imageHeader = (ImageView) findViewById(R.id.header_logo);

        if (donativo.getFotosDonativo() != null && donativo.getFotosDonativo().size() > 0) {
            executeLoadingPhotoTask(donativo.getFotosDonativo().get(0).getNome());
        } else {
            progressBar.setVisibility(View.GONE);
            imageHeader.setVisibility(View.VISIBLE);
        }

        setActionCollapsingTootlbar();
    }

    private void setActionCollapsingTootlbar() {
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    isShow = true;
                    imageHeader.setVisibility(View.GONE);
                } else if (isShow) {
                    imageHeader.setVisibility(View.VISIBLE);
                    isShow = false;
                }
            }
        });
    }

    /**
     * Executa task para carrega foto do donativo
     *
     * @param imageName
     */
    private void executeLoadingPhotoTask(final String imageName) {
        getImageTask = new GetImageTask(this, imageName);
        getImageTask.setProgressAtivo(false);
        getImageTask.delegate = new AsyncResponse<byte[]>() {
            @Override
            public void processFinish(byte[] output) {
                image = output;
                Bitmap bitmap = androidUtil.convertBytesInBitmap(output);
                imageHeader.setVisibility(View.VISIBLE);
                imageHeader.setImageBitmap(bitmap);
                progressBar.setVisibility(View.GONE);
            }
        };

        getImageTask.execute();
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
                Intent intent = new Intent();
                intent.setClass(DetailSolicitacaoActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
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
            case MY_PERMISSIONS_REQUEST_CALL_PHONE_PERMISSION: {
                if (callPhoneDevicePermission.isCallPhonePermissionGranted()) {
                    callPhoneDevicePermission.callPhone(donativo.getDoador().getTelefone());
                }
                break;
            }
        }
    }


    /**
     * @param outState
     */
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("Donativo", donativo);
        outState.putByteArray("Imagem", image);
    }

    protected void onRestoreInstanceState(Bundle savedState) {
        donativo = (Donativo) savedState.getSerializable("Donativo");
        image = savedState.getByteArray("Imagem");

        if (image != null) {
            Bitmap bitmap = androidUtil.convertBytesInBitmap(image);
            imageHeader.setVisibility(View.VISIBLE);
            imageHeader.setImageBitmap(bitmap);
            progressBar.setVisibility(View.GONE);
        }
    }
}
