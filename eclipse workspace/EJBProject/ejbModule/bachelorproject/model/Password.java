package bachelorproject.model;

import java.lang.annotation.*;
import javax.validation.Payload;
import javax.validation.Constraint;

@Constraint(validatedBy = PasswordValidator.class)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Password
{
	String message() default "{invalid password}";
	
	//No idea what this is but it fixes everything
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
