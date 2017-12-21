package br.edu.ifpb.ajudemais.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * <b>{@link Mensageiro}</b>
 * </p>
 * <p>
 * <p>
 * Entidade Um mensageiro no sistema.
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */

public class Mensageiro implements Serializable {

    private Long id;
    private String nome;
    private String cpf;
    private String telefone;
    private FcmToken tokenFCM;
    private Conta conta;
    private Imagem foto;
    private List<Endereco> enderecos;

    public Mensageiro() {

    }

    /**
     * @param nome
     * @param cpf
     * @param telefone
     * @param conta
     */
    public Mensageiro(String nome, String cpf, String telefone, Conta conta, FcmToken tokenFCM) {
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.conta = conta;
        this.tokenFCM = tokenFCM;

        enderecos = new ArrayList<>();
    }

    /**
     * @return Long
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return String
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return String
     */
    public String getCpf() {
        return cpf;
    }

    /**
     * @param cpf
     */
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    /**
     * @return String
     */
    public String getTelefone() {
        return telefone;
    }

    /**
     * @param telefone
     */
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public FcmToken getTokenFCM() {
        return tokenFCM;
    }

    public void setTokenFCM(FcmToken tokenFCM) {
        this.tokenFCM = tokenFCM;
    }

    /**
     * @return Conta
     */
    public Conta getConta() {
        return conta;
    }

    /**
     * @param conta
     */
    public void setConta(Conta conta) {
        this.conta = conta;
    }

    /**
     * @return Imagem
     */
    public Imagem getFoto() {
        return foto;
    }

    /**
     * @param foto
     */
    public void setFoto(Imagem foto) {
        this.foto = foto;
    }

    /**
     * @return
     */
    public List<Endereco> getEnderecos() {
        return enderecos;
    }

    /**
     * @param enderecos
     */
    public void setEnderecos(List<Endereco> enderecos) {
        this.enderecos = enderecos;
    }

    @Override
    public String toString() {
        return "Mensageiro{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", cpf='" + cpf + '\'' +
                ", telefone='" + telefone + '\'' +
                ", tokenFCM='" + tokenFCM + '\'' +
                ", conta=" + conta +
                ", foto=" + foto +
                ", enderecos=" + enderecos +
                '}';
    }
}
