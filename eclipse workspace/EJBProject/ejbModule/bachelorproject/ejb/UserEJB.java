package bachelorproject.ejb;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import bachelorproject.model.User;

@Stateless
@Local
public class UserEJB
{

	public void createUser( User u )
	{
		EntityManagerFactory emf = Persistence.createEntityManagerFactory( "EJBProject" );
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		em.persist( u );

		em.getTransaction().commit();
		em.close();
		emf.close();
	}

	/*
	 * This function returns all users in the database. If there are none, an
	 * empty List object is returned. To provide at least some security, the
	 * results are limited to 20.
	 */
	public List<User> findAllUsers()
	{
		EntityManagerFactory emf = Persistence.createEntityManagerFactory( "EJBProject" );
		EntityManager em = emf.createEntityManager();

		TypedQuery<User> q = em.createNamedQuery( User.FIND_ALL, User.class );

		q.setMaxResults( 20 );

		List<User> results = q.getResultList();

		em.close();
		emf.close();

		return results;
	}

	/*
	 * This function finds the user by it's email address and returns it as a
	 * User object. If no user is found, it will return null.
	 * 
	 * @param email A string with the email address of the user.
	 * @return User object if the user is found, null otherwise
	 */
	public User findUserByEmail( String email )
	{

		EntityManagerFactory emf = Persistence.createEntityManagerFactory( "EJBProject" );
		EntityManager em = emf.createEntityManager();

		TypedQuery<User> q = em.createNamedQuery( User.FIND_BY_EMAIL, User.class );
		q.setParameter( "email", email );

		User result;
		try
		{
			result = q.getSingleResult();

		}
		catch ( NoResultException e )
		{
			em.close();
			emf.close();

			return null;
		}

		em.close();
		emf.close();

		return result;
	}
	
	/*
	 * Deletes the user from the database by the provided id. Note that 
	 * a user is not really deleted, but detached. This means it will still
	 * exist in the heap, but not on the persistence.
	 * 
	 * @param userId The id of the user to be deleted.
	 */
	public void deleteUserById(int userId)
	{

		EntityManagerFactory emf = Persistence.createEntityManagerFactory( "EJBProject" );
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		User u = em.find(User.class, userId);
		
		//If the above find function returns a valid user (not null), we will remove (detach) it.
		if (u != null)
		{
			em.remove(u);
		}
		
		em.getTransaction().commit();
		em.close();
		emf.close();
	}

	/*
	 * Searches for the user by an id. If no user is found, null is returned.
	 * 
	 * @param userId The id of the user the function is to return
	 * @return An User object or null if not found
	 */
	public User findUserById(int userId)
	{
		EntityManagerFactory emf = Persistence.createEntityManagerFactory( "EJBProject" );
		EntityManager em = emf.createEntityManager();
		User u = em.find(User.class, userId);
	
		em.close();
		emf.close();
		
		return u;
	}
}
