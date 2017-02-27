package be.kuleuven.cs.gent.projectweek.services;

import java.security.SecureRandom;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.enterprise.context.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import be.kuleuven.cs.gent.projectweek.model.User;
import be.kuleuven.cs.gent.projectweek.model.UserRole;
import java.io.Serializable;

@SessionScoped
public class UserService implements Serializable
{	
	/*
	 * The purpose of the UserService is to provide a User object for the
	 * duration of the session. This will be provided mainly for the 
	 * AuthenticationService.
	 * When initialized, there'll be no User associated with this service, 
	 * but after login it'll be provided by the AuthenticationService.
	 * 
	 */
	private static final long serialVersionUID = -6239216717833742873L;


	@PostConstruct
	public void init()
	{
		EntityManagerFactory emf =  Persistence.createEntityManagerFactory("EJBProject");
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		//create a test user
		User u = new User();
		
		u.setName("John Doe");
		u.setEmail("john@test.be");
		u.setLastLogin(new Date());
		u.setPass("pass".getBytes());
		u.setSalt(salt(User.SALT_LENGTH));
		u.setRole(UserRole.OPERATOR);
		
		em.persist(u);
		em.getTransaction().commit();
		em.close();
		emf.close();
		
		System.out.println("Saved user ..." + u);
	}
	
	
	public byte[] salt(int length)
	{
	
		SecureRandom sRnd = new SecureRandom();
		byte[] salt = new byte[length];
		
		sRnd.nextBytes(salt);
		
		return salt;
		
	}
}
