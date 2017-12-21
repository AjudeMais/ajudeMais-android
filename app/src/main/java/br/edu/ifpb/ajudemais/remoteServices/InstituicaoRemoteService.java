package br.edu.ifpb.ajudemais.remoteServices;

import android.content.Context;

import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import br.edu.ifpb.ajudemais.domain.Conta;
import br.edu.ifpb.ajudemais.domain.Doador;
import br.edu.ifpb.ajudemais.domain.InstituicaoCaridade;
import br.edu.ifpb.ajudemais.dto.LatLng;

/**
 * <p>
 * <b>InstituicaoRemoteService</b>
 * </p>
 * <p>
 *     Provê serviços Relacionados a instituições de caridade
 * <p>
 *
 * </p>
 *
 * @author <a href="https://github.com/FranckAJ">Franck Aragão</a>
 */
public class InstituicaoRemoteService extends AbstractRemoteService{


    /**
     * @param context
     */
    public InstituicaoRemoteService(Context context) {

        super(context);
    }

    /**
     * Recupera todas instituições cadastradas.
     * @return
     */
    public List<InstituicaoCaridade> getInstituicoesAtivas() {

        ResponseEntity<InstituicaoCaridade[]> responseEntity = restTemplate.getForEntity(API+"/instituicao/ativas", InstituicaoCaridade[].class);

        return Arrays.asList(responseEntity.getBody());
    }

    /**
     * Recupera as instituições com base na localização do device passada.
     * @param latLng
     * @return
     */
    public List<InstituicaoCaridade> postInstituicoesForLocation(LatLng latLng) {

        ResponseEntity<InstituicaoCaridade[]> responseEntity = restTemplate.postForEntity(API+"/instituicao/filterGeoCoordinates", latLng, InstituicaoCaridade[].class);

        return Arrays.asList(responseEntity.getBody());
    }




    }