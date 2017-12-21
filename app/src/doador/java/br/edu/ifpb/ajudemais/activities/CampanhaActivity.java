package br.edu.ifpb.ajudemais.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import br.edu.ifpb.ajudemais.R;
import br.edu.ifpb.ajudemais.domain.Campanha;
import br.edu.ifpb.ajudemais.fragments.CampanhaDetailFragment;
import br.edu.ifpb.ajudemais.fragments.InstituicaoDetailFragment;

public class CampanhaActivity extends BaseActivity implements View.OnClickListener {

    private Campanha campanha;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private FloatingActionButton fab;
    private Toolbar mToolbar;

    @Override
    public void onClick(View view) {

    }

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campanha);

        init();

        if (!isDestroyed()) {
            CampanhaDetailFragment fragment = new CampanhaDetailFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.campanha_detail_fragment, fragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void init() {
        initProperties();
        mToolbar = (Toolbar) findViewById(R.id.nav_action);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        campanha = (Campanha) getIntent().getSerializableExtra("campanha");

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(campanha.getNome());

        fab = (FloatingActionButton) findViewById(R.id.fabCampanhaShare);
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
}
