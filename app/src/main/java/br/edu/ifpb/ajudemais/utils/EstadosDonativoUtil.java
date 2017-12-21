package br.edu.ifpb.ajudemais.utils;

import android.graphics.Color;
import android.widget.TextView;

import br.edu.ifpb.ajudemais.R;
import br.edu.ifpb.ajudemais.enumarations.Estado;

/**
 * <p>
 * <b>{@link EstadosDonativoUtil}</b>
 * </p>
 * <p>
 * <p>
 * Classe auxiliar para lidar com estados da doação.
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */


public class EstadosDonativoUtil {


    /**
     * Set background in label de acordo com estado.
     * @param lbEstado
     * @param estado
     */
    public void setCustomLabelEstadoDoacao(TextView lbEstado, Estado estado) {
        if (estado.name().equals(Estado.CANCELADO.name()) || estado.name().equals(Estado.CANCELADO_POR_MENSAGEIRO.name())) {
            lbEstado.setBackgroundResource(R.drawable.screen_border_cancelado);
            lbEstado.setTextColor(Color.WHITE);

        } else if (estado.name().equals(Estado.DISPONIBILIZADO.name())) {
            lbEstado.setBackgroundResource(R.drawable.screen_border_disponibilizado);
            lbEstado.setTextColor(Color.parseColor("#665e5e"));

        } else if (estado.name().equals(Estado.NAO_ACEITO.name())) {
            lbEstado.setBackgroundResource(R.drawable.screen_border_nao_aceito);
            lbEstado.setTextColor(Color.WHITE);

        } else if (estado.name().equals(Estado.ACEITO.name())) {
            lbEstado.setBackgroundResource(R.drawable.screen_border_aceito);
            lbEstado.setTextColor(Color.WHITE);

        } else if (estado.name().equals(Estado.RECOLHIDO.name())) {
            lbEstado.setBackgroundResource(R.drawable.screen_border_recolhido);
            lbEstado.setTextColor(Color.WHITE);

        } else if (estado.name().equals(Estado.ENTREGUE.name())) {
            lbEstado.setBackgroundResource(R.drawable.screen_border_entregue);
            lbEstado.setTextColor(Color.WHITE);

        } else if (estado.name().equals(Estado.RECEBIDO.name())) {
            lbEstado.setBackgroundResource(R.drawable.screen_border_recebido);
            lbEstado.setTextColor(Color.WHITE);
        }

    }
}
