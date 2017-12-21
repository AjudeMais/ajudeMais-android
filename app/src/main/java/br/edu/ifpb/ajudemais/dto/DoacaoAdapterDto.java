package br.edu.ifpb.ajudemais.dto;

import br.edu.ifpb.ajudemais.domain.Donativo;

/**
 * <p>
 * <b>{@link DoacaoAdapterDto}</b>
 * </p>
 * <p>
 * <p>
 * Classe auxiliar para exibir informações de doação no card;
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */


public class DoacaoAdapterDto {

    private Donativo donativo;
    private byte[] photo;

    /**
     * @return
     */
    public Donativo getDonativo() {
        return donativo;
    }

    /**
     * @param donativo
     */
    public void setDonativo(Donativo donativo) {
        this.donativo = donativo;
    }

    /**
     * @return
     */
    public byte[] getPhoto() {
        return photo;
    }

    /**
     * @param photo
     */
    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "DoacaoAdapterDto{" +
                "donativo=" + donativo +
                '}';
    }
}
