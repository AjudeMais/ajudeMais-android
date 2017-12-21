package br.edu.ifpb.ajudemais.domain;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * <b>{@link Donativo}</b>
 * </p>
 * <p>
 * <p>
 * Entidade que representa um Donativo
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */


public class Donativo implements Serializable{

    /**
     *
     */

    /**
     *
     */
    private Long id;

    /**
     *
     */
    private Integer quantidade;

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
    private List<EstadoDoacao> estadosDaDoacao;

    /**
     *
     */
    private List<DisponibilidadeHorario> horariosDisponiveis;

    /**
     *
     */
    private Doador doador;

    /**
     *
     */
    private Endereco endereco;

    /**
     *
     */
    private List<Imagem> fotosDonativo;

    /**
     *
     */
    private Categoria categoria;

    /**
     *
     */
    private Mensageiro mensageiro;

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
     * @return the quantidade
     */
    public Integer getQuantidade() {
        return quantidade;
    }

    /**
     * @param quantidade the quantidade to set
     */
    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the descricao
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * @param descricao the descricao to set
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * @return the estadosDaDoacao
     */
    public List<EstadoDoacao> getEstadosDaDoacao() {
        return estadosDaDoacao;
    }

    /**
     * @param estadosDaDoacao the estadosDaDoacao to set
     */
    public void setEstadosDaDoacao(List<EstadoDoacao> estadosDaDoacao) {
        this.estadosDaDoacao = estadosDaDoacao;
    }

    /**
     * @return the horariosDisponiveis
     */    public List<DisponibilidadeHorario> getHorariosDisponiveis() {
        return horariosDisponiveis;
    }

    /**
     * @param horariosDisponiveis the horariosDisponiveis to set
     */
    public void setHorariosDisponiveis(List<DisponibilidadeHorario> horariosDisponiveis) {
        this.horariosDisponiveis = horariosDisponiveis;
    }

    /**
     * @return the doador
     */
    public Doador getDoador() {
        return doador;
    }

    /**
     * @param doador the doador to set
     */
    public void setDoador(Doador doador) {
        this.doador = doador;
    }

    /**
     * @return the endereco
     */
    public Endereco getEndereco() {
        return endereco;
    }

    /**
     * @param endereco the endereco to set
     */
    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    /**
     * @return the fotosDonativo
     */
    public List<Imagem> getFotosDonativo() {
        return fotosDonativo;
    }

    /**
     * @param fotosDonativo the fotosDonativo to set
     */
    public void setFotosDonativo(List<Imagem> fotosDonativo) {
        this.fotosDonativo = fotosDonativo;
    }

    /**
     * @return the categoria
     */
    public Categoria getCategoria() {
        return categoria;
    }

    /**
     * @param categoria the categoria to set
     */
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    /**
     *
     * @return
     */
    public Mensageiro getMensageiro() {
        return mensageiro;
    }

    /**
     *
     * @param mensageiro
     */
    public void setMensageiro(Mensageiro mensageiro) {
        this.mensageiro = mensageiro;
    }

    @Override
    public String toString() {
        return "Donativo{" +
                "id=" + id +
                ", quantidade=" + quantidade +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", estadosDaDoacao=" + estadosDaDoacao +
                ", horariosDisponiveis=" + horariosDisponiveis +
                ", doador=" + doador +
                ", endereco=" + endereco +
                ", fotosDonativo=" + fotosDonativo +
                ", categoria=" + categoria +
                '}';
    }
}
