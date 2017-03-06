package bachelorproject.model;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint( validatedBy = PasswordValidator.class )
@Documented
@Retention( RetentionPolicy.RUNTIME )
public @interface Password
{
	String message() default "{invalid password}";

	// No idea what this is but it fixes everything
	Class<?>[] groups() default
	{};

	Class<? extends Payload>[] payload() default
	{};
}
