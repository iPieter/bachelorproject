package bachelorproject.ejb;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import bachelorproject.model.User;
import bachelorproject.model.UserRole;

@Stateless
@Local
public class UserEJB
{
	@Inject
	private EntityManagerSingleton ems;

	public void createUser( User u )
	{
		EntityManager em = ems.getEntityManager();
		em.getTransaction().begin();

		em.persist( u );

		em.getTransaction().commit();
		em.close();
	}

	/**
	 * This function returns all users in the database. If there are none, an
	 * empty List object is returned. To provide at least some security, the
	 * results are limited to 20.
	 */
	public List<User> findAllUsers()
	{
		EntityManager em = ems.getEntityManager();

		TypedQuery<User> q = em.createNamedQuery( User.FIND_ALL, User.class );

		q.setMaxResults( 20 );

		List<User> results = q.getResultList();
		
		em.close();

		return results;
	}

	/**
	 * This function finds the user by it's email address and returns it as a
	 * User object. If no user is found, it will return null.
	 * 
	 * @author Pieter Delobelle
	 * @param email A string with the email address of the user.
	 * @return User object if the user is found, null otherwise
	 */
	public User findUserByEmail( String email )
	{
		EntityManager em = ems.getEntityManager();
		TypedQuery<User> q = em.createNamedQuery( User.FIND_BY_EMAIL, User.class );
		q.setParameter( "email", email );

		User result;
		try
		{
			result = q.getSingleResult();

		}
		catch ( NoResultException e )
		{
			return null;
		}
		
		em.close();

		return result;
	}
	
	/**
	 * Deletes the user from the database by the provided id. Note that 
	 * a user is not really deleted, but detached. This means it will still
	 * exist in the heap, but not on the persistence.
	 * 
	 * @author Pieter Delobelle
	 * @param userId The id of the user to be deleted.
	 */
	public void deleteUserById(int userId)
	{
		EntityManager em = ems.getEntityManager();
		em.getTransaction().begin();

		User u = em.find(User.class, userId);
		
		//If the above find function returns a valid user (not null), we will remove (detach) it.
		if (u != null)
		{
			em.remove(u);
		}
		
		em.close();
		
		em.getTransaction().commit();
	}

	/**
	 * Searches for the user by an id. If no user is found, null is returned.
	 * 
	 * @author Pieter Delobelle
	 * @param userId The id of the user the function is to return
	 * @return An User object or null if not found
	 */
	public User findUserById(int userId)
	{
		EntityManager em = ems.getEntityManager();
		User u = em.find(User.class, userId);
		
		em.close();
	
		return u;
	}
	
	/**
	 * Finds the user provided by it's id and update the other field if needed.
	 * 
	 * @author Pieter Delobelle
	 * @param user The User object to be updated in persistence.
	 */
	public void updateUser(User user)
	{
		EntityManager em = ems.getEntityManager();
		em.getTransaction().begin();

		//User u = em.find(User.class, user.getId());
		
		//If the above find function returns a valid user (not null), we will remove (detach) it.
		//if (u != null)
		//{
			em.merge(user);
		//}
		
		em.getTransaction().commit();
		
		em.close();
	}
	
	/**
	 * Find all the users with a provided UserType
	 * 
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @param type
	 * @return
	 * @see UserRole
	 */
	public List<User> findAllUsersByRole(UserRole role)
	{
		List<User> ls = findAllUsers().stream()
					.filter(user -> user.getRole().equals(role))
					.collect(Collectors.toList());
		return ls;	
		
	}
}
