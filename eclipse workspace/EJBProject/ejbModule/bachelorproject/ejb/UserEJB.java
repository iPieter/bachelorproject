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

}
