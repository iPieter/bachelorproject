package controllers;

import javax.ejb.EJB;

import bachelorproject.ejb.UserEJB;

/**
 * When a user wants to change his/her/(f)aer/eir/per/their/vis/xyr/hir password,
 * this controller is used. 
 * <p>
 * The UserEJB is used extensively in this controller. 
 * 
 * @author Pieter Delobelle
 * @version 1.0.0
 * @see UserEJB
 */
public class ChangePassswordController
{
	
	@EJB
	private UserEJB userEJB;
	
	private String oldPassword;
	private String newPassword;
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
