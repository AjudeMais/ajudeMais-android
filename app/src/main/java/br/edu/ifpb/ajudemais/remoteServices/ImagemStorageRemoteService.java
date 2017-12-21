package br.edu.ifpb.ajudemais.remoteServices;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Base64;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;

import java.io.File;

import br.edu.ifpb.ajudemais.domain.Imagem;

/**
 * <p>
 * <b>br.edu.ifpb.ajudemais.remoteServices</b>
 * </p>
 * <p>
 * <p>
 * Entidade que representa um foto.
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */

public class ImagemStorageRemoteService extends AbstractRemoteService {

    /**
     * construtor
     *
     * @param context
     */
    public ImagemStorageRemoteService(Context context) {
        super(context);
    }

    /**
     * Faz o upload de image no servidor para um local temporario.
     * @param array
     * @return
     */
    public Imagem uploadImage(byte [] array) {

        return restTemplate.postForObject(API + "/upload/imagem", converteBitmapForMultPart(array), Imagem.class);

    }

    /**
     * Recupera a image salva por meio do seu nome.
     * @param name
     * @return
     */
    public byte [] getImage(String name){
        return restTemplate.getForObject(API + "/upload/imagem/{name}", byte[].class, name);
    }


    /**
     * Recupera a image salva em Temp. por meio do seu nome.
     * @param name
     * @return
     */
    public byte [] getTmpImage(String name){
        return restTemplate.getForObject(API + "/upload/imagem/tmp/{name}", byte[].class, name);
    }


    /**
     * Recupera a image salva em Temp. por meio do seu nome.
     * @param name
     * @return
     */
    public void removeTmpImage(String name){
         restTemplate.delete(API + "/upload/imagem/tmp/{name}",  name);
    }


    /**
     * Transforma o objeto o array de bytes para Multipart para upload de image.
     * @param array
     * @return
     */
    private LinkedMultiValueMap<String, Object> converteBitmapForMultPart(byte [] array){

        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        Resource res = new ByteArrayResource(array) {
            @Override
            public String getFilename() throws IllegalStateException {
                return "profilephoto";
            }
        };
        HttpHeaders imageHeaders = new HttpHeaders();
        imageHeaders.setContentType(MediaType.IMAGE_JPEG);
        HttpEntity<Resource> imageEntity = new HttpEntity<Resource>(res, imageHeaders);
        map.add("file", imageEntity);

        return map;

    }
}
