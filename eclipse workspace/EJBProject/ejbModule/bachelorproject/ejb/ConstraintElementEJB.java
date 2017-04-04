package bachelorproject.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import bachelorproject.model.constraint_engine.ConstraintElement;

/**
 * Defines the Entity Java Bean for the ConstraintElement Entity.
 * <p>
 * This class allows for the controller to manipulate and fetch specific ConstraintElement
 * instances. It validates new objects and handles errors when, for example, no
 * entries in the database exist.
 * @author Anton Danneels
 */
@Named
@Stateless
public class ConstraintElementEJB
{
	@Inject
	private EntityManagerSingleton ems;
	
	/**
	 * 	Persists a ConstraintElement object to the database
	 *  @param ce A valid constraintelement object
	 * */
	public void createConstraintElement( ConstraintElement ce )
	{
		EntityManager em = ems.getEntityManager();
		em.getTransaction().begin();
		
		em.persist( ce );
		
		em.getTransaction().commit();
		em.close();
	}
	
	/**
	 * Persisting lists of ConstraintElements
	 * 
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @param ces
	 */
	public void createConstraintElement(List<ConstraintElement> ces)
	{
		EntityManager em = ems.getEntityManager();
		em.getTransaction().begin();
		
		for (ConstraintElement ce : ces)
		{
			em.persist( ce );
		}
		
		em.getTransaction().commit();
		em.close();
	}
}
