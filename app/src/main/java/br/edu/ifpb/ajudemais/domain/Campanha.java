package br.edu.ifpb.ajudemais.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * <b>{@link Campanha}</b>
 * </p>
 * <p>
 * <p>
 * Entidade que representa uma campanha de uma instituição de caridade.
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */

public class Campanha implements Serializable {

    private Long id;

    /**
     *
     */
    private String nome;

    /**
     *
     */
    private String descricao;

    /**
     *
     */
    private Date dataInicio;

    /**
     *
     */
    private boolean status;

    /**
     *
     */
    private Date dataFim;

    /**
     *
     */
    private InstituicaoCaridade instituicaoCaridade;

    /**
     *
     */
    private List<Meta> metas;

    /**
     *
     */
    private boolean notificada;

    /**
     *
     */
    private Date dataCriacao;

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public boolean isNotificada() {
        return notificada;
    }

    public void setNotificada(boolean notificada) {
        this.notificada = notificada;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public InstituicaoCaridade getInstituicaoCaridade() {
        return instituicaoCaridade;
    }

    public void setInstituicaoCaridade(InstituicaoCaridade instituicaoCaridade) {
        this.instituicaoCaridade = instituicaoCaridade;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Meta> getMetas() {
        return metas;
    }

    public void setMetas(List<Meta> metas) {
        this.metas = metas;
    }

    @Override
    public String toString() {
        return "Campanha{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", dataInicio=" + dataInicio +
                ", status=" + status +
                ", dataFim=" + dataFim +
                ", instituicaoCaridade=" + instituicaoCaridade +
                ", metas=" + metas +
                '}';
    }
}
