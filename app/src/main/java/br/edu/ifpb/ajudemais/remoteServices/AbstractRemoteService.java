package br.edu.ifpb.ajudemais.remoteServices;

import android.content.Context;

import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifpb.ajudemais.filter.RequestResponseInterceptor;
import br.edu.ifpb.ajudemais.handler.MyResponseErrorHandler;

/**
 * <p>
 * <b>{@link AbstractRemoteService}</b>
 * </p>
 * <p>
 * Remote service base para os demais serviços providos pela API REST.
 * </p>
 *
 * @author <a href="https://github.com/FranckAJ">Franck Aragão</a>
 */
public abstract class AbstractRemoteService {

    protected static final String API = "https://ajudemaisws.herokuapp.com/";

    protected RestTemplate restTemplate;

    protected Context context;

    /**
     * construtor
     *
     * @param context
     */
    public AbstractRemoteService(Context context) {
        this.context = context;
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.setErrorHandler(new MyResponseErrorHandler());
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
        interceptors.add(new RequestResponseInterceptor(context));
        restTemplate.setInterceptors(interceptors);
    }
}
