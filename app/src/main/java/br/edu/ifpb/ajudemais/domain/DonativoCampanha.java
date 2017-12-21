package br.edu.ifpb.ajudemais.domain;

import java.io.Serializable;

/**
 * <p>
 * <b>{@link DonativoCampanha}</b>
 * </p>
 * <p>
 * <p>
 * Entidade donativo para identificar doações realizadas para uma campanha.
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */
public class DonativoCampanha extends Donativo implements Serializable{

    /**
     *
     */
    private Campanha campanha;

    /**
     *
     */
    private Donativo donativo;

    /**
     *
     * @return
     */
    public Campanha getCampanha() {
        return campanha;
    }

    /**
     *
     * @param campanha
     */
    public void setCampanha(Campanha campanha) {
        this.campanha = campanha;
    }

    /**
     *
     * @return
     */
    public Donativo getDonativo() {
        return donativo;
    }

    /**
     *
     * @param donativo
     */
    public void setDonativo(Donativo donativo) {
        this.donativo = donativo;
    }

    @Override
    public String toString() {
        return "DonativoCampanha{" +
                "campanha=" + campanha +
                ", donativo=" + donativo +
                '}';
    }
}
