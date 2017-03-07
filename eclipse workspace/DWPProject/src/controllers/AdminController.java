package controllers;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import bachelorproject.model.User;
import bachelorproject.ejb.UserEJB;
import bachelorproject.services.UserService;

/*
 * The AdminController is intended for the admin.xhtml page, where 
 * users can be added, modified or deleted. 
 */

@ManagedBean
@RequestScoped
public class AdminController implements Serializable
{
	private static final long serialVersionUID = -8396156684143995442L;

	@EJB
	private UserEJB userEJB;

	private List<User> users;
	private User workingUser;
	
	@PostConstruct
	public void init()
	{
		users = userEJB.findAllUsers();
	}
	
	/*
	 * Deletes the user from the database by the provided id. Note that 
	 * a user is not really deleted, but detached. This means it will still
	 * exist in the heap, but not on the persistence.
	 * 
	 * @param userId The id of the user to be deleted.
	 */
	public void deleteUser(int userId)
	{
		userEJB.deleteUserById(userId);
	}
	
	/*
	 * When the admin wants to edit a user, a modal view is loaded. To save those 
	 * parameters, we need to set the workingUser variable to the current user
	 * the admin will edit.
	 * <p>
	 * If the id is zero, a new user object will be created for edit. 
	 * 
	 * @param userId The id of the user that will be set to workingUser
	 */
	public void updateWorkingUser(int userId)
	{
		if (userId == 0)
		{
			this.workingUser = new User();
			
		} else 
		{
			
			this.workingUser = userEJB.findUserById(userId);
			
		}
	}
	
	public List<User> getUsers()
	{
		return users;
	}

	public void setUsers(List<User> users)
	{
		this.users = users;
	}
}
