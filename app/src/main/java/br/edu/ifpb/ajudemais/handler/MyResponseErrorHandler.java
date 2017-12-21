package br.edu.ifpb.ajudemais.handler;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClientException;
import java.io.IOException;
import br.edu.ifpb.ajudemais.dto.MessageErrorDTO;

/**
 * <p>
 * <b>{@link MyResponseErrorHandler}</b>
 * </p>
 * <p>
 * <p>
 * Handler ReponseError Spring
 * </p>
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 * @author <a href="https://github.com/franckaj">Franck Aragão</a>
 */
public class MyResponseErrorHandler implements ResponseErrorHandler {


    private ResponseErrorHandler myErrorHandler = new DefaultResponseErrorHandler();

    /**
     *
     * @param response
     * @return
     * @throws IOException
     */
    @Override
   public boolean hasError(ClientHttpResponse response) throws IOException {
        return myErrorHandler.hasError(response);
    }

    /**
     *
     * @param response
     * @throws IOException
     */
    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        String body = IOUtils.toString(response.getBody()).replace("[", "").replace("]", "");

        MessageErrorDTO result = new MessageErrorDTO();
        if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            result.setMsg("Nome de usuário ou senha inválido");

        } else if(response.getRawStatusCode() >= 500) {
            result.setMsg("Ocorreu um erro no servidor, tente novamente mais tarde.");

        }else {
            ObjectMapper mapper = new ObjectMapper();
            result = mapper.readValue(body, MessageErrorDTO.class);
        }

        Log.e("HADLER", response.getStatusCode().toString());
        throw new RestClientException(result.getMsg());
    }


}
