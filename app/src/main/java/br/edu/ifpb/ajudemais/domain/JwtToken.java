package br.edu.ifpb.ajudemais.domain;

import java.io.Serializable;
import java.util.Date;


/**
 * <p>
 * <b>{@link JwtToken}</b>
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */
public class JwtToken implements Serializable{

    private String token;

    private Date date = new Date();

    /**
     *
     */
    public JwtToken() {

    }

    /**
     *
     * @param token
     */
    public JwtToken(String token) {
        this.token = token;
    }

    /**
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token
     *            the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date
     *            the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "JwtToken [token=" + token + ", date=" + date + "]";
    }
}
