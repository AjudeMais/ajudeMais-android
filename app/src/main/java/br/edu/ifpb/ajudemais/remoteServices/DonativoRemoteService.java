package br.edu.ifpb.ajudemais.remoteServices;

import android.content.Context;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import br.edu.ifpb.ajudemais.domain.Donativo;
import br.edu.ifpb.ajudemais.domain.DonativoCampanha;
import br.edu.ifpb.ajudemais.dto.LatLng;
import br.edu.ifpb.ajudemais.enumarations.Estado;

/**
 * <p>
 * <b>{@link DonativoRemoteService}</b>
 * </p>
 * <p>
 * <p>
 * Acessa serviços Rest relacionados a Donativo
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */


public class DonativoRemoteService extends AbstractRemoteService {


    /**
     * construtor
     *
     * @param context
     */
    public DonativoRemoteService(Context context) {
        super(context);
    }

    /**
     * Salva via requisão post Http um novo Donativo no banco.
     *
     * @param donativo
     * @return
     */
    public Donativo saveDonativo(Donativo donativo) {
        donativo = restTemplate.postForObject(API + "/donativo", donativo, Donativo.class);
        return donativo;
    }

    /**
     * Salva DOnativo com campanha via requisão post Http um novo Donativo no banco.
     *
     * @param donativo
     * @return
     */
    public DonativoCampanha saveDonativoCampanha(DonativoCampanha donativo) {
        return restTemplate.postForObject(API + "/donativo/save/donativocampanha", donativo, DonativoCampanha.class);
    }

    /**
     * Atualiza informações do donativo
     *
     * @param donativo
     * @return
     */
    public Donativo updateDonativo(Donativo donativo) {
        HttpEntity<Donativo> requestUpdate = new HttpEntity<>(donativo);
        HttpEntity<Donativo> response = restTemplate.exchange(API + "/donativo", HttpMethod.PUT, requestUpdate, Donativo.class);
        return response.getBody();
    }


    /**
     * Busca donativos com base no nome do doador.
     *
     * @param doadorName
     * @return
     */
    public List<Donativo> findByDoadorNome(String doadorName) {
        ResponseEntity<Donativo[]> responseEntity = restTemplate.getForEntity(API + "/donativo/filter/doadorNome?doadorNome={doadorNome}", Donativo[].class, doadorName);

        return Arrays.asList(responseEntity.getBody());
    }

    /**
     * Busca donativos filtrando por mensageiro.
     *
     * @param username nome de usuário do mensageiro.
     * @return lista de donativos.
     */
    public List<Donativo> findByMensageiro(String username) {
        ResponseEntity<Donativo[]> responseEntity = restTemplate.getForEntity(API + "/donativo/filter/mensageiro?username={username}", Donativo[].class, username);

        return Arrays.asList(responseEntity.getBody());
    }

    /**
     * Busca donativos no WS filtrando por mensageiro e estado de um donativo.
     *
     * @param username
     * @param estado
     * @return
     */
    public List<Donativo> findByMensageiroAndEstado(String username, Estado estado) {
        String estadoString = estado.name();
        ResponseEntity<Donativo[]> responseEntity = restTemplate.getForEntity(API + "/donativo/filter/mensageiroEstado?username={username}&estado={estadoString}", Donativo[].class, username, estadoString);

        return Arrays.asList(responseEntity.getBody());
    }


    /**
     * Busca donativos com base no Id doador passado
     *
     * @param IdDoador
     * @return
     */
    public List<Donativo> findByDonativosToDoadorId(Long IdDoador) {
        ResponseEntity<Donativo[]> responseEntity = restTemplate.getForEntity(API + "/donativo/filter/{id}", Donativo[].class, IdDoador);

        return Arrays.asList(responseEntity.getBody());
    }

    /**
     * Busca donativo pelo id.
     *
     * @param id
     * @return
     */
    public Donativo findByDonativoId(Long id) {
        ResponseEntity<Donativo> responseEntity = restTemplate.getForEntity(API + "/donativo/filter/donativo/{id}", Donativo.class, id);
        return responseEntity.getBody();
    }

    /**
     * Busca DonativoCampanha com base no Id donativo passado
     *
     * @param idDonativo
     * @return
     */
    public DonativoCampanha findDonativoCampanhaByDonativoId(Long idDonativo) {

        ResponseEntity<DonativoCampanha> responseEntity = restTemplate.getForEntity(API + "/donativo/filter/donativocampanha/{id}", DonativoCampanha.class, idDonativo);

        return responseEntity.getBody();
    }

    /**
     * Busca os donativos com base na localização
     *
     * @param latLng
     * @return
     */
    public List<Donativo> findByDoadorLocal(LatLng latLng) {
        ResponseEntity<Donativo[]> responseEntity = restTemplate.postForEntity(API + "/donativo/filter/local", latLng, Donativo[].class);
        return Arrays.asList(responseEntity.getBody());
    }


    /**
     * Busca donativos diponibilizados próximo do mensageiro com ID passado .
     *
     * @param id
     * @return
     */
    public List<Donativo> filterByDonativosCloserMensageiroId(Long id) {
        ResponseEntity<Donativo[]> responseEntity = restTemplate.getForEntity(API + "/donativo/filter/closer/mensageiro/{id}", Donativo[].class, id);
        return Arrays.asList(responseEntity.getBody());
    }


    /**
     * Verifica se donativo está válido para ser coletado
     *
     * @param id
     * @return
     */
    public Boolean isValidColetaByDonativo(Long id) {
        ResponseEntity<Boolean> responseEntity = restTemplate.getForEntity(API + "/donativo/validatecoleta/{id}", Boolean.class, id);
        return responseEntity.getBody();
    }

    /**
     * Verifica se donativo está válido para ser recolhido
     *
     * @param id
     * @return
     */
    public Boolean isValidRecolhimentoByDonativo(Long id) {
        ResponseEntity<Boolean> responseEntity = restTemplate.getForEntity(API + "/donativo/validaterecolhimento/{id}", Boolean.class, id);
        return responseEntity.getBody();
    }

    /**
     * Verifica se donativo está válido para ser cancelado
     *
     * @param id
     * @return
     */
    public Boolean isValidCancelamentoByDonativo(Long id) {
        ResponseEntity<Boolean> responseEntity = restTemplate.getForEntity(API + "/donativo/validatecancelamento/{id}", Boolean.class, id);
        return responseEntity.getBody();
    }


}

