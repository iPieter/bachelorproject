package controllers;

import java.io.Serializable;
import java.util.Base64;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import bachelorproject.model.User;
import bachelorproject.model.UserRole;
import bachelorproject.services.UserService;
import bachelorproject.ejb.UserEJB;

/**
 * The AdminController is intended for the admin.xhtml page, where users can be
 * added, modified or deleted.
 */

@Named
@SessionScoped
public class AdminController implements Serializable
{
	private static final long serialVersionUID = -8396156684143995442L;

	@EJB
	private UserEJB userEJB;

	private List<User> users;
	private User workingUser;
	private String workingUserPassword;
	
	@PostConstruct
	public void init()
	{
		users = userEJB.findAllUsers();
	}

	/**
	 * Deletes the user from the database by the provided id. Note that a user
	 * is not really deleted, but detached. This means it will still exist in
	 * the heap, but not on the persistence.
	 * 
	 * @author Pieter Delobelle
	 * @param userId
	 *            The id of the user to be deleted.
	 */
	public void deleteUser(int userId)
	{
		userEJB.deleteUserById(userId);
		this.users = userEJB.findAllUsers();

	}

	/**
	 * When the admin wants to edit a user, a modal view is loaded. To save
	 * those parameters, we need to set the workingUser variable to the current
	 * user the admin will edit.
	 * <p>
	 * If the id is zero, a new user object will be created for edit.
	 * 
	 * @author Pieter Delobelle
	 * @version 0.9.9
	 * @param userId The id of the user that will be set to workingUser
	 * @see User
	 */
	public void updateWorkingUser(int userId)
	{
		System.out.println("updated user " + userId);
		if (userId != 0)
		{
			this.workingUser = userEJB.findUserById(userId);

		}
		
		this.users = userEJB.findAllUsers();

	}

	/**
	 * Function called by JSF to update the user save in the workingUser
	 * variable to persistence. The List <code>users</code> is also updated
	 * Takes no inputs and returns nothing.
	 * 
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 */
	public void updateUser()
	{	
		userEJB.updateUser(this.workingUser);
		this.users = userEJB.findAllUsers();
	
	}
	
	
	/**
	 * Generate a new User object to be set as the workingUser. this will be
	 * populated by JSF.
	 * <p>
	 * It also generates a password to be used for the workingUser.
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 */
	public void newWorkingUser()
	{
		this.workingUser = new User();
		
		//Generate password
		this.workingUserPassword = Base64.getEncoder().encodeToString(UserService.salt(10)).substring(0, 9);
		System.out.println("user + '" + this.workingUserPassword + "'");		
		UserService.populateUser(this.workingUser, this.workingUserPassword, "qwertyui");
	}

	public User getWorkingUser()
	{
		return workingUser;
	}

	public void setWorkingUser(User workingUser)
	{
		this.workingUser = workingUser;
	}

	public List<User> getUsers()
	{
		return users;
	}

	public void setUsers(List<User> users)
	{
		this.users = users;
	}

	/**
	 * Provides a getter for a non-exisisting parameter. Intended for listing
	 * all the roles.
	 * 
	 * @return Array with all the values the UserRole enum can have.
	 */
	public UserRole[] getUserRoles()
	{
		return UserRole.values();
	}

	/**
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @return the workingUserPassword
	 */
	public String getWorkingUserPassword()
	{
		return workingUserPassword;
	}

	/**
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @param workingUserPassword the workingUserPassword to set
	 */
	public void setWorkingUserPassword(String workingUserPassword)
	{
		this.workingUserPassword = workingUserPassword;
	}
	
	
}
