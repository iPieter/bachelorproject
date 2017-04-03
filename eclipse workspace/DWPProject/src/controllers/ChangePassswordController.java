package controllers;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;

import bachelorproject.ejb.UserEJB;
import bachelorproject.model.user.UserRole;
import bachelorproject.services.UserService;

/**
 * When a user wants to change his/her/(f)aer/eir/per/their/vis/xyr/hir password,
 * this controller is used. 
 * <p>
 * The UserService is used extensively in this controller. 
 * 
 * @author Pieter Delobelle
 * @version 1.0.0
 * @see UserEJB
 * @see UserService
 */
@ManagedBean
@SessionScoped
public class ChangePassswordController implements Serializable
{
	
	private static final long serialVersionUID = 705694004649777190L;

	@EJB
	private UserEJB userEJB;
	
	@Inject
	private UserService userService;
	
	private String oldPassword;
	private String newPassword;
	
	/**
	 * The function that's called by the JSF when the user confirms he wants to change his 
	 * password.
	 * <p> 
	 * Notice there's no safeguard to check if a password is what the user intended to type, 
	 * this happens on the client side. Likely this is implemented with javascript. 
	 * 
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @see UserService
	 */
	public String changePassword()
	{
		//First check if old password is correct
		if (!oldPassword.equals(newPassword)&& userService.veficatePassword(oldPassword))
		{
			UserService.populateUser(userService.getUser(), this.newPassword, "qwertyui");
			
			//For some reason, user is likely moved out of persistence context
			userEJB.updateUser(userService.getUser());
			
			return userService.hasCurrentUserRequiredRole(UserRole.ADMIN) ? "admin.xhtml?faces-redirect=true"
					: "index.xhtml?faces-redirect=true";		
		}
		
		return "";
	}
	
	
	/**
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @return the oldPassword
	 */
	public String getOldPassword()
	{
		return oldPassword;
	}
	/**
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @param oldPassword the oldPassword to set
	 */
	public void setOldPassword(String oldPassword)
	{
		this.oldPassword = oldPassword;
	}
	/**
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @return the newPassword
	 */
	public String getNewPassword()
	{
		return newPassword;
	}
	/**
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @param newPassword the newPassword to set
	 */
	public void setNewPassword(String newPassword)
	{
		this.newPassword = newPassword;
	}

	
}
