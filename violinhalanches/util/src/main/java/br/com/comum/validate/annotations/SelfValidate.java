package br.com.comum.validate.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import br.com.comum.validate.constraints.SelfValidatingValidator;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SelfValidatingValidator.class)
@Documented
public @interface SelfValidate {

	String message() default "{ Error SelfValidate: Default Message }";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
	
}
