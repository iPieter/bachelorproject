package controllers;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import bachelorproject.model.Password;
import bachelorproject.model.User;
import bachelorproject.model.UserRole;
import bachelorproject.services.UserService;

/**
 * @author Pieter
 *
 */
/**
 * @author Pieter
 *
 */
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

	@NotNull
	@Password
	private String password = "";

	/**
	 * Checks if the email and password set by JSF match. This check is done by
	 * the UserService.
	 * 
	 * @author Pieter Delobelle
	 * @version 0.1.0
	 * @return The url where JSF should redirect to. If the user is an admin,
	 *         this will be admin.xhtml.
	 * @see UserService
	 */
	public String doLogin()
	{
		if (userService.verificateLogin(email, password))
		{
			return userService.hasCurrentUserRequiredRole(UserRole.ADMIN) ? "admin.xhtml?faces-redirect=true"
					: "index.xhtml?faces-redirect=true";
		} else
		{
			return null;
		}
	}

	/**
	 * Returns if the current user has the provided role.
	 * 
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @param ur
	 *            UserRole enum to check
	 * @return True if the user has the same role, false otherwise.
	 */
	public boolean hasAccess(UserRole ur)
	{

		return userService.hasCurrentUserRequiredRole(ur);
	}

	/**
	 * Provides a easy check for one UserRole type: OPERATOR
	 * 
	 * @author Pieter Delobelle
	 * @version 0.1.0
	 * @return True if the user has the OPERATOR role, false otherwise
	 * @deprecated
	 */
	public boolean hasOperatorAccess()
	{
		return hasAccess(UserRole.OPERATOR);
	}

	/**
	 * Provides a easy check for one UserRole type: OPERATOR
	 * 
	 * @author Pieter Delobelle
	 * @version 0.1.0
	 * @return False if the user has the OPERATOR role, true otherwise
	 * @deprecated
	 */
	public boolean hasNoOperatorAccess()
	{
		return !hasAccess(UserRole.OPERATOR);
	}

	/**
	 * Provides a easy check for one UserRole type: MECHANIC
	 * 
	 * @author Pieter Delobelle
	 * @version 0.1.0
	 * @return True if the user has the MECHANIC role, false otherwise
	 * @deprecated
	 */
	public boolean hasMechanicAccess()
	{
		return hasAccess(UserRole.MECHANIC);
	}

	/**
	 * Provides a easy check for one UserRole type: MECHANIC
	 * 
	 * @author Pieter Delobelle
	 * @version 0.1.0
	 * @return True if the user has the MECHANIC role, false otherwise
	 * @deprecated
	 */
	public boolean hasAdminAccess()
	{
		return hasAccess(UserRole.ADMIN);
	}

	/**
	 * When called, this will log out the current user in the UserService.
	 * 
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @see UserService
	 */
	public void logout()
	{
		userService.tryLogout();
	}

	/**
	 * Returns the
	 * 
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @return
	 */
	public String getName()
	{
		User u = userService.getUser();
		if (u != null)
		{
			return u.getName();
		}
		
		return "";
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
