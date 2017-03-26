package controllers;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;

import bachelorproject.ejb.UserEJB;
import bachelorproject.model.UserRole;
import bachelorproject.services.UserService;

/**
 * Controller for the xhtml page where a user can manage his/her account.
 * 
 * At the moment, there's only one function: changing the user image via a 
 * REST-api. So for this, we need a hidden input with the email.
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

	
}
