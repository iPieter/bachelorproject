package be.kuleuven.cs.gent.projectweek.services;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import be.kuleuven.cs.gent.projectweek.model.User;
import be.kuleuven.cs.gent.projectweek.model.UserRole;

@Singleton
@Startup
public class UserService
{	
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
		u.setPass("pass".getBytes());
		u.setSalt("salt".getBytes());
		u.setRole(UserRole.OPERATOR);
		
		em.persist(u);
		em.getTransaction().commit();
		em.close();
		emf.close();
		
		System.out.println("Saved user ..." + u);
	}
	
}
