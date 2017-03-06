package bachelorproject.model;

import java.security.SecureRandom;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String>
{

	@Override
	public void initialize(Password arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isValid(String pass, ConstraintValidatorContext cvc)
	{
		//We must return true if the password is 'null'
		//because it would overlap with @NotNull
		
		if (pass == null)
		{
			return true;
		}

		//Very advanced password validation ...
		return pass.length() > 8;
	}

}
