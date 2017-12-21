package br.edu.ifpb.ajudemais.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;

import org.json.JSONObject;

import java.util.Collections;

import br.edu.ifpb.ajudemais.activities.CreateAccountHelperActivity;
import br.edu.ifpb.ajudemais.asyncTasks.AsyncResponse;
import br.edu.ifpb.ajudemais.asyncTasks.FacebookProfilePictureTask;
import br.edu.ifpb.ajudemais.domain.Conta;
import br.edu.ifpb.ajudemais.domain.Doador;
import br.edu.ifpb.ajudemais.utils.CapturePhotoUtils;

/**
 * Created by amsv on 26/04/17.
 */

public class FacebookAccount {


    private static Doador doador;

    /**
     * Método responsável por obter dados de acesso de um usuário do facebook.
     * Dados como: Nome, e-mail e username
     */
    public static void userFacebookData(final Context context, LoginResult loginResult, final Activity activity) {
        doador = new Doador();
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        if (response != null) {
                            try {
                                doador.setNome(Profile.getCurrentProfile().getName());
                                doador.setFacebookID(Profile.getCurrentProfile().getId());
                                doador.setCampanhas(null);
                                doador.setFacebookID(Profile.getCurrentProfile().getId());
                                doador.setConta(new Conta());
                                String email = object.optString("email");
                                if (email != null) {
                                    doador.getConta().setEmail(email);
                                }
                                doador.getConta().setUsername(Profile.getCurrentProfile().getId());
                                doador.getConta().setSenha(Profile.getCurrentProfile().getId());
                                doador.getConta().setGrupos(Collections.singletonList("ROLE_DOADOR"));
                                doador.getConta().setAtivo(true);
                                if (doador != null) {
                                    goToFacebookAccountHelperActivity(activity, doador);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private static void goToFacebookAccountHelperActivity(Activity activity, Doador doador) {
        Intent intent = new Intent(activity, CreateAccountHelperActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Doador", doador);
        activity.startActivity(intent);
        activity.finish();
    }


}
