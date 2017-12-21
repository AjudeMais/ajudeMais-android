package br.edu.ifpb.ajudemais.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 * <b>br.edu.ifpb.ajudemais.utils</b>
 * </p>
 * <p>
 * <p>
 * Entidade que representa um foto.
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */


public class ConvertsDate {

    private static ConvertsDate instance;


    /**
     * Recupera a instância da classe.
     *
     * @return ConvertsDate
     */
    public static synchronized ConvertsDate getInstance() {
        if (instance == null)
            instance = new ConvertsDate();
        return instance;
    }

    /**
     * Recebe uma data e retorna uma string no Formato Dia/Mês/Ano
     *
     * @return
     */
    public String convertDateToStringFormat(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(date);
    }

    /**
     * Recebe horário no formato de Data e transforma em String.
     *
     * @param date
     * @return
     */
    public String convertHourToString(Date date) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        return timeFormat.format(date);
    }


    public Date convertStringWithDateAndHourToDate(String dateString) {
        Date data = null;
        try {

            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            data = formato.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return data;
    }
}
