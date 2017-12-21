package br.edu.ifpb.ajudemais.domain;

import java.io.Serializable;
import java.util.Date;

import br.edu.ifpb.ajudemais.enumarations.Estado;

/**
 * <p>
 * <b>{@link EstadoDoacao}</b>
 * </p>
 * <p>
 * <p>
 * Mapea os estados de doação
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */
public class EstadoDoacao implements Serializable{

    /**
     *
     */
    private static final long serialVersionUID = -7652424045014577650L;

    /**
     *
     */
    private Long id;

    /**
     *
     */
    private Date data;

    /**
     *
     */
    private Boolean notificado;

    /**
     *
     */
    private Boolean ativo;

    /**
     *
     */
    private Estado estadoDoacao;

    /**
     *
     */
    private String mensagem;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }
    /**
     * @return the data
     */
    public Date getData() {
        return data;
    }
    /**
     * @param data the data to set
     */
    public void setData(Date data) {
        this.data = data;
    }
    /**
     * @return the notificado
     */
    public Boolean getNotificado() {
        return notificado;
    }
    /**
     * @param notificado the notificado to set
     */
    public void setNotificado(Boolean notificado) {
        this.notificado = notificado;
    }

    /**
     * @return the estadoDoacao
     */
    public Estado getEstadoDoacao() {
        return estadoDoacao;
    }
    /**
     * @param estadoDoacao the estadoDoacao to set
     */
    public void setEstadoDoacao(Estado estadoDoacao) {
        this.estadoDoacao = estadoDoacao;
    }

    /**
     *
     * @return
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     *
     * @return
     */
    public Boolean getAtivo() {
        return ativo;
    }

    /**
     *
     * @param ativo
     */
    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    /**
     *
     * @return
     */
    public String getMensagem() {
        return mensagem;
    }

    /**
     *
     * @param mensagem
     */
    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    @Override
    public String toString() {
        return "EstadoDoacao{" +
                "id=" + id +
                ", data=" + data +
                ", notificado=" + notificado +
                ", ativo=" + ativo +
                ", estadoDoacao=" + estadoDoacao +
                '}';
    }
}
