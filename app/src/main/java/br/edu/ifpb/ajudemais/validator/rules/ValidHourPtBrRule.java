package br.edu.ifpb.ajudemais.validator.rules;

import com.mobsandgeeks.saripaar.AnnotationRule;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import br.edu.ifpb.ajudemais.validator.annotations.ValidHourPtBr;

/**
 * <p>
 * <b>{@link ValidHourPtBrRule}</b>
 * </p>
 * <p>
 * <p>
 * Regra para validação de horas no formato Brasileiro.
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */

public class ValidHourPtBrRule extends AnnotationRule<ValidHourPtBr, String> {

    private Pattern pattern;
    private Matcher matcher;

    private static final String TIME24HOURS_PATTERN = "([01]?[0-9]|2[0-3]):[0-5][0-9]";

    protected ValidHourPtBrRule(ValidHourPtBr validHourPtBr) {
        super(validHourPtBr);
    }

    /**
     * Valida se hora é válida considerando o formato de 24h.
     * @param s
     * @return
     */
    @Override
    public boolean isValid(String s) {
        pattern = Pattern.compile(TIME24HOURS_PATTERN);
        matcher = pattern.matcher(s);
        return matcher.matches();
    }
}
