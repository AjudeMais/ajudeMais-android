package br.edu.ifpb.ajudemais.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import br.edu.ifpb.ajudemais.R;
import br.edu.ifpb.ajudemais.fragments.TabFragment;

/**
 * <p>
 * <b>MainActivity</b>
 * </p>
 * <p>
 * <p>
 * Activity inicial para Mensageiro nela existe três tabs para mensageiro gerenciar suas coletas
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */
public class MainActivity extends DrawerMenuActivity {

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        setUpAccount();
        setUpToggle();
        setupNavDrawer();

            mFragmentManager = getSupportFragmentManager();
            mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.replace(R.id.containerView, new TabFragment()).commit();


    }


}
