package service;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import model.User;

@Stateless
public class UserService
{
	@PersistenceContext(unitName="EJBProject")
	private EntityManager em;
	
	//create a test user
	User u = new User();
	
}
