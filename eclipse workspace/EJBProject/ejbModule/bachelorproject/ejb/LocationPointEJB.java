/**
 * 
 */
package bachelorproject.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import bachelorproject.model.constraint_engine.LocationPoint;

/**
 * EJB for managing LocationPoints
 * 
 * @author Pieter Delobelle
 * @version 1.0.0
 */
@Named
@Stateless
public class LocationPointEJB
{
	@Inject
	private EntityManagerSingleton ems;

	LocationPointEJB()
	{

	}

	/**
	 * Persist a single locationPoint to the database
	 * 
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @param lp
	 *            The locationPoint to persist
	 * @see LocationPoint
	 */
	public void persistLocationPoint( LocationPoint lp )
	{
		EntityManager em = ems.getEntityManager();
		em.getTransaction().begin();

		em.persist( lp );

		em.getTransaction().commit();
		em.close();
	}

	/**
	 * Persist a list of locationPoints to the database
	 * 
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @param lp
	 *            The locationPoint list to persist
	 * @see LocationPoint
	 */
	public void persistLocationPoint( List<LocationPoint> lps )
	{
		EntityManager em = ems.getEntityManager();
		em.getTransaction().begin();

		for ( LocationPoint lp : lps )
		{
			em.persist( lp );
			em.flush();
		}

		em.getTransaction().commit();
		em.close();
	}
}
