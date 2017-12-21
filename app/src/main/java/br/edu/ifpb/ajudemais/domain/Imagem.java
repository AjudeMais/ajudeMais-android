package br.edu.ifpb.ajudemais.domain;

import java.io.Serializable;

/**
 * <p>
 * <b>{@link Imagem}</b>
 * </p>
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */
public class Imagem implements Serializable{

    /**
     *
     */
    private Long id;

    /**
     *
     */
    private String nome;

    /**
     *
     */
    private String contentType;



    public Imagem(String nome, String contentType) {
        this.nome = nome;
        this.contentType = contentType;
    }

    public Imagem(){}

    /**
     *
     * @return
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
     * @return
     */
    public String getNome() {
        return nome;
    }

    /**
     *
     * @param nome
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     *
     * @return
     */
    public String getContentType() {
        return contentType;
    }

    /**
     *
     * @param contentType
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "Imagem{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", contentType='" + contentType + '\'' +
                '}';
    }
}
