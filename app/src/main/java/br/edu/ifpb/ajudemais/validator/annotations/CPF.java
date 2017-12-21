package br.edu.ifpb.ajudemais.validator.annotations;

import android.support.annotation.StringRes;

import com.mobsandgeeks.saripaar.annotation.ValidateUsing;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.edu.ifpb.ajudemais.validator.rules.CpfRule;

/**
 * <p>
 * <b>{@link CPF}</b>
 * </p>
 * <p>
 * <p>
 * Anotation para validar cpf em formulários
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */

@ValidateUsing(CpfRule.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CPF {

    @StringRes int messageResId()   default -1;
    String message()                default "CPF inválido";
    int sequence()                  default -1;
}
