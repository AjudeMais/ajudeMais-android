package br.edu.ifpb.ajudemais.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * <b>{@link Conta}</b>
 * </p>
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */
public class Conta implements Serializable{

    private Long id;
    private String username;
    private String senha;
    private boolean ativo;
    private String email;
    private List<String> grupos;
    private Date resetSenha;


    /**
     *
     */
    public Conta(){}

    /**
     *
     * @param email
     */
    public Conta(String email) {
        this.email = email;
    }

    /**
     *
     * @param username
     * @param senha
     */
    public Conta(String username, String senha) {
        this.username = username;
        this.senha = senha;
    }

    /**
     *
     * @param username
     * @param email
     * @param grupos
     */
    public Conta(String username, String email, List<String> grupos) {
        this.username = username;
        this.email = email;
        this.grupos = grupos;
    }

    /**
     *
     * @param username
     * @param senha
     * @param ativo
     * @param email
     * @param grupos
     */
    public Conta(String username, String senha, boolean ativo, String email, List<String> grupos) {
        this.username = username;
        this.senha = senha;
        this.ativo = ativo;
        this.email = email;
        this.grupos = grupos;
    }

    /**
     *
     * @return Long
     */
    public Long getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *
     * @return String
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *
     * @return String
     */
    public String getSenha() {
        return senha;
    }

    /**
     *
     * @param senha
     */
    public void setSenha(String senha) {
        this.senha = senha;
    }


    /**
     *
     * @return List
     */
    public List<String> getGrupos() {
        return grupos;
    }

    /**
     *
     * @param grupos
     */
    public void setGrupos(List<String> grupos) {
        this.grupos = grupos;
    }

    /**
     *
     * @return boolean
     */
    public boolean isAtivo() {
        return ativo;
    }

    /**
     *
     * @param ativo
     */
    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    /**
     *
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     * @return
     */
    public Date getResetSenha() {
        return resetSenha;
    }

    /**
     *
     * @param resetSenha
     */
    public void setResetSenha(Date resetSenha) {
        this.resetSenha = resetSenha;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "Conta{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", senha='" + senha + '\'' +
                ", ativo=" + ativo +
                ", email='" + email + '\'' +
                ", grupos=" + grupos +
                ", resetSenha=" + resetSenha +
                '}';
    }
}