package br.edu.ifpb.ajudemais.exceptions;

import org.springframework.http.HttpStatus;

import java.io.IOException;

/**
 * <p>
 * <b>{@link RemoteAccessErrorException}</b>
 * </p>
 * <p>
 * <p>
 * Exception para erros ao acessar serviços do WS ajude mais.
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */

public class RemoteAccessErrorException extends IOException {

    private HttpStatus statusCode;

    private String body;

    /**
     *
     * @param msg
     */
    public RemoteAccessErrorException(String msg) {
        super(msg);
    }

    /**
     *
     * @param statusCode
     * @param body
     * @param msg
     */
    public RemoteAccessErrorException(HttpStatus statusCode, String body, String msg) {
        super(msg);
        this.statusCode = statusCode;
        this.body = body;
    }

    /**
     *
     * @return
     */
    public HttpStatus getStatusCode() {
        return statusCode;
    }

    /**
     *
     * @param statusCode
     */
    public void setStatusCode(HttpStatus statusCode) {
        this.statusCode = statusCode;
    }

    /**
     *
     * @return
     */
    public String getBody() {
        return body;
    }

    /**
     *
     * @param body
     */
    public void setBody(String body) {
        this.body = body;
    }

}
