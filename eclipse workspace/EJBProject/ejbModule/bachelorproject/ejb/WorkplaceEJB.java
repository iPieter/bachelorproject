package bachelorproject.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import bachelorproject.model.Workplace;
import bachelorproject.model.user.User;

/**
 * Defines the Entity Java Bean for the Workplace Entity.
 * <p>
 * This class allows for the controller to manipulate and fetch specific
 * Workplace instances. It validates new objects and handles errors when, for
 * example, no entries in the database exist.
 * 
 * @author Anton Danneels
 * @version 1.0.0
 */
@Named
@Stateless
public class WorkplaceEJB
{
	@Inject
	private EntityManagerSingleton ems;

	/**
	 * Retrieves a list of all workplaces.
	 * 
	 * @return A List of all workplaces
	 */
	public List<Workplace> getAllWorkplaces()
	{
		EntityManager em = ems.getEntityManager();

		em.getTransaction().begin();

		TypedQuery<Workplace> query = em.createNamedQuery( Workplace.FIND_ALL, Workplace.class );
		List<Workplace> result = query.getResultList();

		em.getTransaction().commit();

		em.close();

		return result;
	}

	/**
	 * Finds a workplace based on it's ID.
	 * 
	 * @param id
	 *            The ID of the workplace to fetch
	 * @return A Workplace if found, null otherwise
	 */
	public Workplace findWorkplaceByWorkplaceId( int id )
	{
		EntityManager em = ems.getEntityManager();
		em.getTransaction().begin();

		Workplace result = em.find( Workplace.class, id );

		em.getTransaction().commit();

		em.close();

		return result;
	}

	/**
	 * Finds a workplace based on a traincoach ID. This method will return a
	 * list which may be empty.
	 * 
	 * @param id
	 *            The traincoach id who's workplace to fetch
	 * @return A List of workplaces based on the traincoach ID
	 */
	public List<Workplace> findWorkplaceByTraincoachID( int id )
	{
		EntityManager em = ems.getEntityManager();
		em.getTransaction().begin();

		TypedQuery<Workplace> query = em.createNamedQuery( Workplace.FIND_BY_TRAINCOACH_ID, Workplace.class );
		query.setParameter( "id", id );
		List<Workplace> result = query.getResultList();

		em.getTransaction().commit();

		em.close();
		return result;
	}

	/**
	 * Searches for all workplaces by mechanic id in the persistence context. If
	 * not a single workplace is found, an empty list is returned.
	 * 
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @param id
	 *            The user id
	 * @return
	 */
	public List<Workplace> findWorkplaceByUserId( int id )
	{
		EntityManager em = ems.getEntityManager();

		TypedQuery<Workplace> query = em.createNamedQuery( Workplace.FIND_BY_USER_ID, Workplace.class );
		query.setParameter( "id", id );
		List<Workplace> result = query.getResultList();

		em.close();
		return result;
	}

	/**
	 * Searches for all mechanics by workplace id in the persistence context.
	 * 
	 * @version 1.0.0
	 * @param workplaceId
	 *            The id of the workplace.
	 * @return A List<User> with all the mechanics of that workplace.
	 * @see User
	 */
	public List<User> findMechanicsByWorkplaceId( int workplaceId )
	{
		EntityManager em = ems.getEntityManager();
		em.getTransaction().begin();

		TypedQuery<User> query = em.createNamedQuery( Workplace.FIND_BY_WORKPLACE_ID, User.class );
		query.setParameter( "id", workplaceId );
		List<User> result = query.getResultList();

		em.getTransaction().commit();

		em.close();

		return result;
	}

	/**
	 * When (for some reason) a workplace became detached, you can merge it to
	 * the persistence context with this function.
	 * 
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @param wp
	 *            The workplace to update in the database
	 */
	public void updateWorkplace( Workplace wp )
	{
		EntityManager em = ems.getEntityManager();
		em.getTransaction().begin();

		em.merge( wp );

		em.getTransaction().commit();

		em.close();

	}
}
