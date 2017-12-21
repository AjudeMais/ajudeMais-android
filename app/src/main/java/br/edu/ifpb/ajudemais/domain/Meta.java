package br.edu.ifpb.ajudemais.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import br.edu.ifpb.ajudemais.enumarations.UnidadeMedida;

/**
 * <p>
 * <b>{@link Meta}</b>
 * </p>
 * <p>
 * <p>
 * Entidade que representa uma meta de uma campanha
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */
public class Meta implements Serializable{

    /**
     *
     */
    private Long id;


    /**
     *
     */
    private Categoria categoria;

    /**
     *
     */
    private UnidadeMedida unidadeMedida;

    /**
     *
     */
    private BigDecimal quantidade;

    /**
     *
     */
    private Float percentualAtingido;


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
    public Categoria getCategoria() {
        return categoria;
    }

    /**
     *
     * @param categoria
     */
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    /**
     *
     * @return
     */
    public UnidadeMedida getUnidadeMedida() {
        return unidadeMedida;
    }

    /**
     *
     * @param unidadeMedida
     */
    public void setUnidadeMedida(UnidadeMedida unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    /**
     *
     * @return
     */
    public BigDecimal getQuantidade() {
        return quantidade;
    }

    /**
     *
     * @param quantidade
     */
    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    /**
     *
     * @return
     */
    public Float getPercentualAtingido() {
        return percentualAtingido;
    }

    /**
     *
     * @param percentualAtingido
     */
    public void setPercentualAtingido(Float percentualAtingido) {
        this.percentualAtingido = percentualAtingido;
    }
}
