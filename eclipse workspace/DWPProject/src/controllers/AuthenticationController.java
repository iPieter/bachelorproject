package controllers;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import be.kuleuven.cs.gent.projectweek.model.Password;
import be.kuleuven.cs.gent.projectweek.model.UserRole;
import be.kuleuven.cs.gent.projectweek.services.UserService;

@ManagedBean
@SessionScoped
public class AuthenticationController implements Serializable
{

	/**
	 * The AuthenticationController is used by the authentication.(x)html page.
	 */
	
	private static final long serialVersionUID = -8396156684143995442L;
	
	@Inject
	private UserService userService; 
	
	@NotNull
	private String email = "";
	
	@NotNull @Password
	private String password = "";
	
	public String doLogin()
	{
		if (userService.verificateLogin(email, password))
		{
			return "index.xhtml";
		} else 
		{
			return null;
		}
	}
	
	public boolean hasAccess(UserRole ur)
	{

		return userService.hasCurrentUserRequiredRole(ur);
	}
	
	public boolean hasOperatorAccess()
	{
		System.out.println("op.access: " + UserRole.OPERATOR);
		return hasAccess(UserRole.OPERATOR);
	}

	public boolean hasNoOperatorAccess()
	{
		System.out.println("!op.access: " + UserRole.OPERATOR);
		return !hasAccess(UserRole.OPERATOR);
	}
	
	public boolean hasMechanicAccess()
	{
		return hasAccess(UserRole.MECHANIC);
	}
	
	public boolean hasAdminAccess()
	{
		return hasAccess(UserRole.ADMIN);
	}
	
	public void logout()
	{
		userService.tryLogout();
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
