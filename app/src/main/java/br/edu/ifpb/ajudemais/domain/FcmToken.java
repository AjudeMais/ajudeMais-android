package br.edu.ifpb.ajudemais.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by millanium on 6/11/17.
 */

public class FcmToken implements Serializable{

    /**
     *
     */
    private Long id;

    /**
     *
     */
    private String token;

    /**
     *
     */
    private Date date = new Date();

    /**
     *
     * @param token
     */
    public FcmToken(String token) {
        this.token = token;
    }

    /**
     *
     */
    public FcmToken() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
