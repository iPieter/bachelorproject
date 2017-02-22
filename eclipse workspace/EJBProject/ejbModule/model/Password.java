package model;

import java.lang.annotation.*;

import javax.validation.Constraint;

@Constraint(validatedBy = PasswordValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface Password
{

}
