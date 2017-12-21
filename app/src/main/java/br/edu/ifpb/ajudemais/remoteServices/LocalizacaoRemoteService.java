package br.edu.ifpb.ajudemais.remoteServices;

import android.content.Context;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;

import br.edu.ifpb.ajudemais.domain.Endereco;
import br.edu.ifpb.ajudemais.dto.LatLng;

/**
 * <p>
 * <b>{@link LocalizacaoRemoteService}</b>
 * </p>
 * <p>
 * <p>
 * Entidade que representa um foto.
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */


public class LocalizacaoRemoteService extends AbstractRemoteService{

    /**
     * construtor default
     *
     * @param context
     */
    public LocalizacaoRemoteService(Context context) {
        super(context);
    }

    /**
     * Transforma a latitude e logintude informados no Objeto Endereço com Rua, Cep, Bairro e etc.
     * @param latLng
     * @return
     */
    public Endereco findByEnderecoActual(LatLng latLng){

        return restTemplate.postForObject(API + "/localizacao", latLng,Endereco.class);
    }
}
