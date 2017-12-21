package br.edu.ifpb.ajudemais.validator.annotations;

import android.support.annotation.StringRes;

import com.mobsandgeeks.saripaar.annotation.ValidateUsing;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.edu.ifpb.ajudemais.validator.rules.CpfRule;
import br.edu.ifpb.ajudemais.validator.rules.ValidDatePtBrRule;

/**
 * <p>
 * <b>{@link ValidDatePtBr}</b>
 * </p>
 * <p>
 * <p>
 * Annotation para validar datas no formato brasileiro.
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */

@ValidateUsing(ValidDatePtBrRule.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ValidDatePtBr {

    @StringRes int messageResId()   default -1;
    String message()                default "Data inválida";
    int sequence()                  default -1;
}
