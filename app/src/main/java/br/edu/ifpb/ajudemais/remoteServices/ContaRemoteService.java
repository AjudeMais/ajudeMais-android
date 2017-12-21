package br.edu.ifpb.ajudemais.remoteServices;

import android.content.Context;

import br.edu.ifpb.ajudemais.dto.ChangePasswordDTO;
import br.edu.ifpb.ajudemais.storage.SharedPrefManager;

/**
 * <p>
 * <b>{@link ContaRemoteService}</b>
 * </p>
 * <p>
 * <p>
 * Fornece os serviços relacionados a conta do usuário.
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */
public class ContaRemoteService extends AbstractRemoteService{


    /**
     * construtor
     *
     * @param context
     */
    public ContaRemoteService(Context context) {
        super(context);
    }

    /**
     * Altera a senha de acesso do usuário.
     */
    public void changePassword(ChangePasswordDTO changePasswordDTO){
        restTemplate.postForObject(API+"/conta/changePassword",changePasswordDTO, Object.class);
        SharedPrefManager.getInstance(context).clearSharedPrefs();
    }
}
