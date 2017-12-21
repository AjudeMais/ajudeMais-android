package br.edu.ifpb.ajudemais.filter;

import android.content.Context;
import android.util.Log;

import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;

import java.io.IOException;
import java.util.Arrays;

import br.edu.ifpb.ajudemais.storage.SharedPrefManager;

/**
 * <p>
 * <b>{@link RequestResponseInterceptor}</b>
 * </p>
 * <p>
 *   Interneceptador para tratar header da  requisição com adição de token de acesso.
 * </p>
 *
 * @author <a href="https://github.com/FranckAJ">Franck Aragão</a>
 */
public class RequestResponseInterceptor implements ClientHttpRequestInterceptor {

    private Context context;

    public RequestResponseInterceptor(Context context) {
        this.context = context;
    }

    /**
     *
     * @param request
     * @param body
     * @param execution
     * @return
     * @throws IOException
     */
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,

                                        ClientHttpRequestExecution execution) throws IOException {

        try {
            interceptRequest(request, body);

            ClientHttpResponse clientHttpResponse = execution.execute(request, body);

            interceptResponse(clientHttpResponse);

            return clientHttpResponse;
        }catch (java.net.ConnectException e){
            throw new RestClientException("Ocorreu um problema no servidor, tente novamente mais tarde.");
        }

    }

    /**
     *
     * @param response
     */
    private void interceptResponse(ClientHttpResponse response) {
        String currentToken = response.getHeaders().getAuthorization();

        if(currentToken != null || !(StringUtils.isEmpty(currentToken))) {
            SharedPrefManager.getInstance(context).storeToken(currentToken);
        }
    }

    /**
     *
     * @param request
     * @param body
     */
    private void interceptRequest(HttpRequest request, byte[] body) {

        String token = SharedPrefManager.getInstance(this.context).getToken();
        HttpRequest wrapper = new HttpRequestWrapper(request);

        if(token != null) {
            wrapper.getHeaders().set("Authorization", token);
        }


        if (request.getURI().getPath().equals("/upload/imagem")){
          // wrapper.getHeaders().setContentType(MediaType.MULTIPART_FORM_DATA);

        }else {
            wrapper.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        }
        wrapper.getHeaders().setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

    }
}
