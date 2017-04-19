package controllers;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;

import bachelorproject.ejb.UserEJB;
import bachelorproject.model.user.UserRole;
import bachelorproject.services.UserService;

/**
 * Controller for the xhtml page where a user can manage his/her account.
 * <p>
 * At the moment, there's only one function: changing the user image via a 
 * REST-api. So for this, we need a hidden input with the email.
 * <p>
 * When starting up, 
 * 
 * @author Pieter Delobelle
 * @version 0.1.0
 */
@ManagedBean
@RequestScoped
public class AccountController implements Serializable
{
	
	private static final long serialVersionUID = 1194692178654520754L;
	
	@Inject
	private UserService userService;
	@Inject
	private AuthenticationController authC;
	
	@EJB
	private UserEJB userEJB;
	
	/**
	 * Dirty hack to make sure the updates on the database by the REST api are
	 * represented on the client's view.
	 * 
	 * @author Pieter Delobelle
	 * @version 0.1.0
	 */
	@PostConstruct
	public void updateUser()
	{
		userService.setUser(userEJB.findUserById(userService.getUser().getId()));
	}
	
	/**
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @return Emailaddress of the logged in user.
	 * @see UserService
	 */
	public String getUserEmail()
	{
		return userService.getUser().getEmail();
	}
	
	/**
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @return Image hash of the user
	 */
	public String getAvatar()
	{
		return userService.getUser().getImageHash();
	}
	
	public String getToken() 
	{
		return userService.getToken().getToken();
	}
	
	/**
	 * False setter so JSF doesn't complain.
	 * 
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 */
	public void setToken()
	{
		//Empty
	}

	
}
