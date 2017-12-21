package br.edu.ifpb.ajudemais.fcm;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.Date;

import br.edu.ifpb.ajudemais.activities.LoginActivity;
import br.edu.ifpb.ajudemais.asycnTasks.LoadingDoadorTask;
import br.edu.ifpb.ajudemais.asycnTasks.UpdateDoadorTask;
import br.edu.ifpb.ajudemais.asyncTasks.AsyncResponse;
import br.edu.ifpb.ajudemais.domain.Conta;
import br.edu.ifpb.ajudemais.domain.Doador;
import br.edu.ifpb.ajudemais.storage.SharedPrefManager;


/**
 * <p>
 * <b>MyFirebaseInstanceIDService</b>
 * </p>
 * Classe responsável por obter token da API do Firebase.
 * <p>
 *
 * @author <a href="https://github.com/FranckAJ">Franck Aragão</a>
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private SharedPrefManager sharedPrefManager;

    private LoadingDoadorTask loadingDoadorTask;

    private UpdateDoadorTask updateDoadorTask;
    /**
     *
     */
    @Override
    public void onTokenRefresh() {
        sharedPrefManager = SharedPrefManager.getInstance(getApplicationContext());
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        storageToken(refreshedToken);
        updateDoador();
    }



    /**
     *
     */
    private void updateDoador() {
        Conta conta = sharedPrefManager.getUser();
        if(conta != null) {
            loadingDoadorTask = new LoadingDoadorTask(getApplicationContext(), conta.getUsername());
            loadingDoadorTask.delegate = new AsyncResponse<Doador>() {
                @Override
                public void processFinish(Doador output) {
                    output.getTokenFCM().setDate(new Date());
                    output.getTokenFCM().setToken(FirebaseInstanceId.getInstance().getToken());
                    updateDoadorTask = new UpdateDoadorTask(getApplicationContext(), output);
                    updateDoadorTask.setProgressAtivo(false);
                    updateDoadorTask.execute();
                }
            };

            loadingDoadorTask.execute();
        }
    }

    /**
     * Storage token refreshed in shared prefs
     *
     * @param token
     */
    private void storageToken(String token) {
        SharedPrefManager.getInstance(getApplicationContext()).storageFcmToken(token);
    }


}
