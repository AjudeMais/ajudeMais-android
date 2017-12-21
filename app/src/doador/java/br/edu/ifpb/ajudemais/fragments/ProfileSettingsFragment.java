package br.edu.ifpb.ajudemais.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.Field;

import br.edu.ifpb.ajudemais.R;
import br.edu.ifpb.ajudemais.domain.Doador;

/**
 * Fragmento de edite de conta Doador.
 */
public class ProfileSettingsFragment extends Fragment {

    private TextView tvNome;
    private TextView tvEmail;
    private TextView tvPhone;
    private View view;
    private Doador doador;

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_profile_settings, container, false);

        setHasOptionsMenu(true);

        return view;
    }

    /**
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvNome = (TextView) getView().findViewById(R.id.tv_edit_nome);
        tvEmail = (TextView) getView().findViewById(R.id.tv_edit_email);
        tvPhone = (TextView) getView().findViewById(R.id.tv_edit_celular);

        doador = (Doador) getArguments().getSerializable("doador");

        if (doador != null) {
            tvPhone.setText(doador.getTelefone());
            tvNome.setText(doador.getNome());
            tvEmail.setText(doador.getConta().getEmail());
        }
    }


    /**
     *
     */
    @Override
    public void onStart() {
        super.onStart();


    }


}
