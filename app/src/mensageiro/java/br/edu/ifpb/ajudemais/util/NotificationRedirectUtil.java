package br.edu.ifpb.ajudemais.util;

import android.content.Context;
import android.content.Intent;

import br.edu.ifpb.ajudemais.activities.DetailSolicitacaoActivity;
import br.edu.ifpb.ajudemais.asyncTasks.AsyncResponse;
import br.edu.ifpb.ajudemais.asyncTasks.GetDonativoByIdTask;
import br.edu.ifpb.ajudemais.domain.Donativo;
import br.edu.ifpb.ajudemais.storage.SharedPrefManager;

/**
 * <p>
 * <b>{@link NotificationRedirectUtil}</b>
 * </p>
 * <p>
 * <p>
 * Auxiliar para criação de regras de redirecionamento em notificações
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */
public class NotificationRedirectUtil {

    /**
     *
     */
    private Context context;

    /**
     * @param context
     */
    public NotificationRedirectUtil(Context context) {
        this.context = context;
    }

    /**
     * @param id
     * @param tipoNotificacao
     */
    public void redirectNotification(Long id, String tipoNotificacao) {
        if (id != null && tipoNotificacao != null) {
            switch (tipoNotificacao) {
                case "CAMPANHA":
                    break;
                case "DOACAO":
                    executeLoadingDonativoByIdTask(id);
                    break;
                default:
                    break;
            }
        }
    }


    /**
     * Recupera donativo da notificação.
     *
     * @param donativoId
     */
    private void executeLoadingDonativoByIdTask(final Long donativoId) {
        GetDonativoByIdTask getDonativoByIdTask = new GetDonativoByIdTask(context, donativoId);
        getDonativoByIdTask.delegate = new AsyncResponse<Donativo>() {
            @Override
            public void processFinish(Donativo output) {
                Donativo donativo = output;

                Intent intent = new Intent(context, DetailSolicitacaoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("Donativo", donativo);
                if (donativo.getMensageiro() != null && (
                        donativo.getMensageiro().getConta().getUsername().equals(SharedPrefManager.getInstance(context).getUser().getUsername()))){

                }else {
                    intent.putExtra("notification", new Boolean(true));
                }
                context.startActivity(intent);
            }
        };
        getDonativoByIdTask.execute();
    }
}
