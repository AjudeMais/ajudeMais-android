package br.edu.ifpb.ajudemais.validator.annotations;

import android.support.annotation.StringRes;

import com.mobsandgeeks.saripaar.annotation.ValidateUsing;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import br.edu.ifpb.ajudemais.validator.rules.ValidHourPtBrRule;

/**
 * <p>
 * <b>{@link ValidHourPtBr}</b>
 * </p>
 * <p>
 * <p>
 * Annotation para validação de horas no formato brasileiro considerando 24h.
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */

@ValidateUsing(ValidHourPtBrRule.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ValidHourPtBr {
    @StringRes int messageResId()   default -1;
    String message()                default "Hora invalida.";
    int sequence()                  default -1;
}
