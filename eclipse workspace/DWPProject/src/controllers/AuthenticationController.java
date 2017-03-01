package controllers;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import be.kuleuven.cs.gent.projectweek.model.Password;
import be.kuleuven.cs.gent.projectweek.model.User;
import be.kuleuven.cs.gent.projectweek.services.AuthenticationService;

@ManagedBean
@RequestScoped
public class AuthenticationController implements Serializable
{

	/**
	 * The AuthenticationController is used by the authentication.(x)html page.
	 */
	
	private static final long serialVersionUID = -8396156684143995442L;
	
	@Inject
	private AuthenticationService authService; 
	
	@NotNull
	private String email;
	
	@NotNull @Password
	private String password;
	
	public String doLogin()
	{
		//TODO: this is only for testing!!!!
		System.out.println(email + " and pass='" + password + "'");

		// ---------------- REMOVE IT WHEN DONE WITH TESTING THIS CODE  ---------------- 
		
		if (authService.login(email, password))
		{
			return "index.xhtml";
		} else 
		{
			return null;
		}
		
		
	}
	
	public void setEmail(String email)
	{
		this.email = email;
	}
	
	public String getEmail()
	{
		return this.email;
	}
	
	public void setPassword(String password)
	{
		this.password = password;
	}
	
	public String getPassword()
	{
		return this.password;
	}
}
