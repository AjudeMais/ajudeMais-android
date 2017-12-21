package br.edu.ifpb.ajudemais.remoteServices;

import android.content.Context;
import android.util.Log;

import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import br.edu.ifpb.ajudemais.domain.Categoria;
import br.edu.ifpb.ajudemais.domain.InstituicaoCaridade;

/**
 * <p>
 * <b>{@link CategoriaRemoteService}</b>
 * </p>
 * <p>
 * <p>
 * Oferece acesso a recursos providos pela API Rest Relacionados a Categoria de itens doáveis.
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */

public class CategoriaRemoteService extends AbstractRemoteService {

    /**
     * construtor
     *
     * @param context
     */
    public CategoriaRemoteService(Context context) {
        super(context);
    }


    /**
     * Lista todas as categorias ativas do id da instituição passada.
     * @param id
     * @return
     */
    public List<Categoria> listCategoriasAtivasToInstituicao (Long id){
        ResponseEntity<Categoria[]> responseEntity = restTemplate.getForEntity(API+"/categoria/ativas/{id}", Categoria[].class, id);

        return Arrays.asList(responseEntity.getBody());
    }
}
