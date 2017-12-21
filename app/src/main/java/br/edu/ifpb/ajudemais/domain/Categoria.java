package br.edu.ifpb.ajudemais.domain;


import java.io.Serializable;

/**
 * <p>
 * <b>{@link Categoria}</b>
 * </p>
 * <p>
 * <p>
 * Entidade que representa um categoria de item doável.
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */

public class Categoria implements Serializable{

    private Long id;
    private String nome;
    private String descricao;
    private Boolean ativo;
    private InstituicaoCaridade instituicaoCaridade;

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
    public String getDescricao() {
        return descricao;
    }

    /**
     *
     * @param descricao
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
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
    public InstituicaoCaridade getInstituicaoCaridade() {
        return instituicaoCaridade;
    }

    /**
     *
     * @param instituicaoCaridade
     */
    public void setInstituicaoCaridade(InstituicaoCaridade instituicaoCaridade) {
        this.instituicaoCaridade = instituicaoCaridade;
    }

    @Override
    public String toString() {
        return "Categoria{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", ativo=" + ativo +
                ", instituicaoCaridade=" + instituicaoCaridade +
                '}';
    }
}
