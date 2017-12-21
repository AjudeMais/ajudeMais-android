package br.edu.ifpb.ajudemais.validator.rules;

import com.mobsandgeeks.saripaar.AnnotationRule;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import br.edu.ifpb.ajudemais.validator.annotations.ValidDatePtBr;

/**
 * <p>
 * <b>{@link ValidDatePtBrRule}</b>
 * </p>
 * <p>
 * <p>
 * Regra de validação para datas no formato brasileiro.
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */
public class ValidDatePtBrRule extends AnnotationRule<ValidDatePtBr, String> {

    final static String DATE_FORMAT = "dd/MM/yyyy";

    protected ValidDatePtBrRule(ValidDatePtBr validDatePtBr) {
        super(validDatePtBr);
    }

    @Override
    public boolean isValid(String s) {
        try {
            DateFormat df = new SimpleDateFormat(DATE_FORMAT);
            df.setLenient(false);
            df.parse(s);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
