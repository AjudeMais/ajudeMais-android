package br.edu.ifpb.ajudemais.util;

import android.content.Context;
import android.content.Intent;
import br.edu.ifpb.ajudemais.activities.CampanhaActivity;
import br.edu.ifpb.ajudemais.activities.DonativoActivity;
import br.edu.ifpb.ajudemais.asyncTasks.AsyncResponse;
import br.edu.ifpb.ajudemais.asyncTasks.GetCampanhaByIdTask;
import br.edu.ifpb.ajudemais.asyncTasks.GetDonativoByIdTask;
import br.edu.ifpb.ajudemais.domain.Campanha;
import br.edu.ifpb.ajudemais.domain.Donativo;

/**
 * <p>
 * <b>{@link NotificationRedirectUtil}</b>
 * </p>
 * <p>
 * <p>
 * Auxiliar para criação de regras de redirecionamento em notificações
 * </p>
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */
public class NotificationRedirectUtil {

    /**
     *
     */
    private Context context;

    /**
     *
     * @param context
     */
    public NotificationRedirectUtil(Context context){
        this.context = context;
    }

    /**
     *
     * @param id
     * @param tipoNotificacao
     */
    public void redirectNotification(Long id, String tipoNotificacao) {
        if (id != null && tipoNotificacao != null) {
            switch (tipoNotificacao) {
                case "CAMPANHA":
                    executeLoadingCampanhaByIdTask(id);
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
     * Recupera campanha de notificação.
     *
     * @param campanhaId
     */
    private void executeLoadingCampanhaByIdTask(Long campanhaId) {
        GetCampanhaByIdTask getCampanhaByIdTask = new GetCampanhaByIdTask(context, campanhaId);
        getCampanhaByIdTask.delegate = new AsyncResponse<Campanha>() {
            @Override
            public void processFinish(Campanha output) {
                Campanha campanha = output;
                Intent intent = new Intent(context, CampanhaActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("campanha", campanha);
                context.startActivity(intent);
            }
        };
        getCampanhaByIdTask.execute();
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
                Intent intent = new Intent(context, DonativoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("Donativo", donativo);
                context.startActivity(intent);
            }
        };
        getDonativoByIdTask.execute();
    }
}
