package br.edu.ifpb.ajudemais.fcm;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import br.edu.ifpb.ajudemais.asycnTasks.LoadingMensageiroTask;
import br.edu.ifpb.ajudemais.asycnTasks.UpdateMensageiroTask;
import br.edu.ifpb.ajudemais.asyncTasks.AsyncResponse;
import br.edu.ifpb.ajudemais.domain.Conta;
import br.edu.ifpb.ajudemais.domain.FcmToken;
import br.edu.ifpb.ajudemais.domain.Mensageiro;
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

    private LoadingMensageiroTask loadingMensageiroTask;

    private UpdateMensageiroTask updateMensageiroTask;

    private String token;
    /**
     *
     */
    @Override
    public void onTokenRefresh() {
        sharedPrefManager = SharedPrefManager.getInstance(getApplicationContext());
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        storageToken(refreshedToken);
        this.token = refreshedToken;
        updateMensageiro();
    }

    /**
     *
     */
    private void updateMensageiro() {
        Conta conta = sharedPrefManager.getUser();
        if(conta != null) {
            loadingMensageiroTask = new LoadingMensageiroTask(getApplicationContext(), conta.getUsername());
            loadingMensageiroTask.delegate = new AsyncResponse<Mensageiro>() {
                @Override
                public void processFinish(Mensageiro output) {
                    FcmToken fcmToken = new FcmToken(FirebaseInstanceId.getInstance().getToken());
                    if(output.getTokenFCM() != null) {
                        output.getTokenFCM().setToken(FirebaseInstanceId.getInstance().getToken());
                    }else {
                        output.setTokenFCM(fcmToken);
                    }
                    updateMensageiroTask = new UpdateMensageiroTask(getApplicationContext(), output);
                    updateMensageiroTask.setProgressAtivo(false);
                    updateMensageiroTask.execute();
                }
            };

            loadingMensageiroTask.execute();
        }
    }

    /**
     *
     * @param token
     */
    private void storageToken(String token) {
        SharedPrefManager.getInstance(getApplicationContext()).storageFcmToken(token);
    }

}
