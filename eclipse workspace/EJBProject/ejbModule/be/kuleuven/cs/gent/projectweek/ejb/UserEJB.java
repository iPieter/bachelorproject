package be.kuleuven.cs.gent.projectweek.ejb;

import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import be.kuleuven.cs.gent.projectweek.model.User;
import be.kuleuven.cs.gent.projectweek.model.UserRole;

@Stateless
@Local
public class UserEJB
{
	
	public void createUser(User u)
	{
		EntityManagerFactory emf =  Persistence.createEntityManagerFactory("EJBProject");
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		em.persist(u);
		
		em.getTransaction().commit();
		em.close();
		emf.close();
	}
	
	
	/*
	 * This function finds the user by it's email adress and returns it as a 
	 * User object. If no user is found, it will return null.
	 */
	public User findUserByEmail(String email)
	{
		
		System.out.println("Finding email: " + email);

		EntityManagerFactory emf =  Persistence.createEntityManagerFactory("EJBProject");
		EntityManager em = emf.createEntityManager();
		
		TypedQuery<User> q = em.createNamedQuery(User.FIND_BY_EMAIL, User.class);
		q.setParameter("email", email);
		
		
		User result;
		try
		{
			result = q.getSingleResult();
			
		} catch (NoResultException e)
		{
			em.close();
			emf.close();
			
			return null;
		}
		
		em.close();
		emf.close();
		
		return result;
	}

}
