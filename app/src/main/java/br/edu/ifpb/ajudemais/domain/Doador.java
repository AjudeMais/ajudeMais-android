package br.edu.ifpb.ajudemais.domain;

import java.io.Serializable;
import java.util.List;

import br.edu.ifpb.ajudemais.dto.LatLng;

/**
 * <p>
 * <b>{@link Doador}</b>
 * </p>
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */
public class Doador implements Serializable {

    private Long id;
    private String nome;
    private String telefone;
    private String facebookID;
    private FcmToken tokenFCM;
    private Conta conta;
    private List<Campanha> campanhas;
    private Imagem foto;
    private Endereco enderecoAtual;

    /**
     *
     */
    public Doador() {
        conta = new Conta();
    }

    /**
     * @param nome
     * @param telefone
     * @param tokenFCM
     * @param conta
     */
    public Doador(String nome, String telefone, FcmToken tokenFCM, Conta conta) {
        this.nome = nome;
        this.telefone = telefone;
        this.tokenFCM = tokenFCM;
        this.conta = conta;
    }

    /**
     * @param nome
     * @param telefone
     * @param facebookID
     * @param conta
     */
    public Doador(String nome, String telefone, String facebookID, Conta conta) {
        this.nome = nome;
        this.telefone = telefone;
        this.facebookID = facebookID;
        this.conta = conta;
    }

    /**
     * @param nome
     * @param telefone
     * @param conta
     */
    public Doador(String nome, String telefone, Conta conta) {
        this.nome = nome;
        this.telefone = telefone;
        this.conta = conta;
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
    public void setNomeUsuario(String nome) {
        this.nome = nome;
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
     * @param nome
     */
    public void setNome(String nome) {
        this.nome = nome;
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

    /**
     * @return String
     */
    public String getFacebookID() {
        return facebookID;
    }

    /**
     * @param facebookID
     */
    public void setFacebookID(String facebookID) {
        this.facebookID = facebookID;
    }

    public FcmToken getTokenFCM() {
        return tokenFCM;
    }

    public void setTokenFCM(FcmToken tokenFCM) {
        this.tokenFCM = tokenFCM;
    }

    /**
     * @return campanhas
     */
    public List<Campanha> getCampanhas() {
        return campanhas;
    }

    /**
     * @param campanhas
     */
    public void setCampanhas(List<Campanha> campanhas) {
        this.campanhas = campanhas;
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

    public Endereco getEnderecoAtual() {
        return enderecoAtual;
    }

    public void setEnderecoAtual(Endereco enderecoAtual) {
        this.enderecoAtual = enderecoAtual;
    }

    @Override
    public String toString() {
        return "Doador{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", telefone='" + telefone + '\'' +
                ", facebookID='" + facebookID + '\'' +
                ", tokenFCM='" + tokenFCM + '\'' +
                ", conta=" + conta +
                ", foto=" + foto +
                '}';
    }
}
