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
	
	@PostConstruct
	public void init()
	{
		users = userEJB.findAllUsers();
		System.out.println("Set users: " + users.get(0));

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
