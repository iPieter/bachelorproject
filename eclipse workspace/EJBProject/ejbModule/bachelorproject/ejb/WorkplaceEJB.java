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

@Named
@Stateless
public class WorkplaceEJB
{
	@Inject
	private EntityManagerSingleton ems;
	
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

	public Workplace findWorkplaceByWorkplaceId( int id )
	{
		EntityManager em = ems.getEntityManager();
		em.getTransaction().begin();

		Workplace result = em.find( Workplace.class, id );

		em.getTransaction().commit();
		
		em.close();

		return result;
	}

	public List<Workplace> findWorkplaceByTraincoachID( int id )
	{
		EntityManager em = ems.getEntityManager();
		em.getTransaction().begin();

		TypedQuery<Workplace> query = em.createNamedQuery( "Workplace.findWorkplaceByTraincoachID", Workplace.class );
		query.setParameter( "id", id );
		List<Workplace> result = query.getResultList();

		em.getTransaction().commit();

		em.close();
		return result;
	}

	/**
	 * Searches for all mechanics by workplace id in the persistence context.
	 * 
	 * @version 1.0.0
	 * @param workplaceId The id of the workplace.
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
	 * When (for some reason) a workplace became detached, you can merge 
	 * it to the persistence context with this function.
	 * 
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @param wp The workplace to update in the database
	 */
	public void updateWorkplace(Workplace wp)
	{
		EntityManager em = ems.getEntityManager();
		em.getTransaction().begin();

		em.merge(wp);

		em.getTransaction().commit();
		
		em.close();

	}
}
