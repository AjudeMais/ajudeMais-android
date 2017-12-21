package br.edu.ifpb.ajudemais.remoteServices;

import android.content.Context;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

import br.edu.ifpb.ajudemais.domain.Conta;
import br.edu.ifpb.ajudemais.enumarations.Grupo;
import br.edu.ifpb.ajudemais.domain.JwtToken;
import br.edu.ifpb.ajudemais.storage.SharedPrefManager;

/**
 * <p>
 * <b>{@link AuthRemoteService}</b>
 * </p>
 * <p>
 * <p>
 * Provê os serviços para conta de usuário.
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 * @author <a href="https://github.com/franckaj">Franck Aragão</a>
 */

public class AuthRemoteService extends AbstractRemoteService {


    /**
     * @param context
     */
    public AuthRemoteService(Context context) {
        super(context);
    }

    /**
     * @param conta
     * @return
     */
    public Conta createAuthenticationToken(Conta conta, Grupo grupo) {
        JwtToken token = restTemplate.postForObject(API + "/auth/login", conta, JwtToken.class);
        if (token != null) {
            storageToken(token.getToken());
        }
        return getUser(grupo);
    }

    /**
     * recupera a conta do usuário com base no grupo e token de acesso.
     *
     * @return
     */
    public Conta getUser(Grupo grupo) {
        final ResponseEntity<Conta> responseEntity = restTemplate.getForEntity(API + "/auth/user", Conta.class);
        Conta conta = responseEntity.getBody();
        if (conta != null) {
            verifyUserModule(conta, grupo);
            SharedPrefManager.getInstance(context).storeUser(conta);
        }
        return conta;

    }

    /**
     * Verifica se conta está autorizada.
     *
     * @return
     */
    public Boolean isAuth() {
        ResponseEntity<Boolean> responseEntity = restTemplate.getForEntity(API + "/auth/valida", Boolean.class);
        Boolean isValid = responseEntity.getBody();
        if (isValid == null) {
            return false;
        }
        return isValid;
    }

    /**
     * Verificar Se usuário já está logado
     *
     * @return
     */
    public boolean logged() {
        if (SharedPrefManager.getInstance(context).getToken() == null) {
            return false;
        } else if (SharedPrefManager.getInstance(context).getUser() == null) {
            return false;

        } else if (!isAuth()) {
            return false;
        }
        return true;
    }

    /**
     * Guada token de acesso.
     *
     * @param token
     */
    private void storageToken(String token) {
        SharedPrefManager.getInstance(context).storeToken(token);
    }

    /**
     * Valida se usário possível acesso ao app.
     *
     * @param conta
     * @param grupo
     */
    private void verifyUserModule(Conta conta, final Grupo grupo) {

        for (String p : conta.getGrupos()) {
            if (!p.contains(grupo.name())) {
                SharedPrefManager.getInstance(context).clearSharedPrefs();
                throw new RestClientException("Usuário não autorizado");
            }
        }

    }
}
