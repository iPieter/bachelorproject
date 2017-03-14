package bachelorproject.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import bachelorproject.model.User;
import bachelorproject.model.Workplace;

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

		TypedQuery<Workplace> query = em.createNamedQuery( "Workplace.findAll", Workplace.class );
		List<Workplace> result = query.getResultList();

		em.getTransaction().commit();

		return result;
	}

	public Workplace findWorkplaceByWorkplaceId( int id )
	{
		EntityManager em = ems.getEntityManager();
		em.getTransaction().begin();

		Workplace result = em.find( Workplace.class, id );

		em.getTransaction().commit();

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

		return result;
	}

	public List<User> findMechanicsByWorkplaceId( int workplaceId )
	{
		EntityManager em = ems.getEntityManager();
		em.getTransaction().begin();

		TypedQuery<User> query = em.createNamedQuery( Workplace.FIND_BY_WORKPLACE_ID, User.class );
		query.setParameter( "id", workplaceId );
		List<User> result = query.getResultList();

		em.getTransaction().commit();

		return result;
	}

	/**
	 * 
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @param currentWorkplace The workplace where the user should be added.
	 * @param userId The id of the user (mechanic) to be added to the workplace
	 */
	public void insertMechanicIntoWorkplace(Workplace currentWorkplace, int userId)
	{

		EntityManager em = ems.getEntityManager();
		em.getTransaction().begin();

		

		em.getTransaction().commit();;

	}
}
