package br.edu.ifpb.ajudemais.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * <b>{@link DisponibilidadeHorario}</b>
 * </p>
 * <p>
 * <p>
 * Representa Disponibilidade de horário para agendamento para a coleta de uma doação
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */


public class DisponibilidadeHorario implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    private Long id;

    /**
     *
     */
    private Date horaInicio;

    /**
     *
     */
    private Date horaFim;

    /**
     *
     */
    private Boolean ativo;


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
     * @return the horaInicio
     */
    public Date getHoraInicio() {
        return horaInicio;
    }

    /**
     * @param horaInicio the horaInicio to set
     */
    public void setHoraInicio(Date horaInicio) {
        this.horaInicio = horaInicio;
    }

    /**
     * @return the horaFim
     */
    public Date getHoraFim() {
        return horaFim;
    }

    /**
     * @param horaFim the horaFim to set
     */
    public void setHoraFim(Date horaFim) {
        this.horaFim = horaFim;
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

    @Override
    public String toString() {
        return "DisponibilidadeHorario{" +
                "id=" + id +
                ", horaInicio=" + horaInicio +
                ", horaFim=" + horaFim +
                ", ativo=" + ativo +
                '}';
    }
}
