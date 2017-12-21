package br.edu.ifpb.ajudemais.remoteServices;

import android.content.Context;

import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import br.edu.ifpb.ajudemais.domain.Campanha;
import br.edu.ifpb.ajudemais.dto.LatLng;

/**
 * <p>
 * <b>CampanhaRemoteService</b>
 * </p>
 * <p>
 * Remote service para serviços relacionados a campanha
 * </p>
 *
 * @author <a href="https://github.com/FranckAJ">Franck Aragão</a>
 */
public class CampanhaRemoteService extends AbstractRemoteService {

    /**
     * construtor
     *
     * @param context
     */
    public CampanhaRemoteService(Context context) {
        super(context);
    }

    /**
     * Recupera as campanhas com base na localização do device passada.
     *
     * @param latLng
     * @return
     */
    public List<Campanha> filterByInstituicaoLocal(LatLng latLng) {

        ResponseEntity<Campanha[]> responseEntity = restTemplate.postForEntity(API + "/campanha/filter/local",
                latLng, Campanha[].class);
        return Arrays.asList(responseEntity.getBody());
    }

    /**
     * Busca campanha na API por status ativo.
     *
     * @return
     */
    public List<Campanha> findByStatusAtivo() {
        Boolean status = true;
        ResponseEntity<Campanha[]> responseEntity = restTemplate.getForEntity(API + "/campanha/filter/status?status={status}",
                Campanha[].class, status);

        return Arrays.asList(responseEntity.getBody());
    }

    public Campanha findById(Long id){
        ResponseEntity<Campanha> responseEntity = restTemplate.getForEntity(API+"/campanha/{id}", Campanha.class, id);
        return responseEntity.getBody();
    }
}
