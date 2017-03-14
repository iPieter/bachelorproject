package bachelorproject.ejb;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import bachelorproject.model.LiveSensorData;
import bachelorproject.model.LiveSensorDataEntry;

/**
 * Defines the Entity Java Bean for the LiveSensorDataEntry Entity.
 * <p>
 * This class allows for the controller to manipulate and fetch specific LiveSensorDataEntry
 * instances. It validates new objects and handles errors when, for example, no
 * entries in the database exist.
 * @author Anton Danneels
 * @see LiveSensorDataEntryEJB
 */
@Named
@Stateless
public class LiveSensorDataEntryEJB
{
	@Inject
	private EntityManagerSingleton ems;
	
	@Inject
	private LiveSensorDataEJB lsdEJB;
	
	/**
	 * 	If the specified LiveSensorData object is valid ( the ride is live ),
	 *  this method will add a new row to the database.
	 *  @param lsdID The ID of the LiveSensorData object that this entry is related to
	 *  @param entry The new entry to be added.
	 * */
	public boolean addEntry( int lsdID, LiveSensorDataEntry entry )
	{
		EntityManager em = ems.getEntityManager();
		
		LiveSensorData lsd = lsdEJB.findLSDByID( lsdID );
		
		em.getTransaction().begin();
		
		if( lsd != null && lsd.isLive() )
		{
			lsd.getEntries().add( entry );
			em.persist( entry );
			em.persist( lsd );
			
			em.getTransaction().commit();
			
			return true;
		}
		
		em.getTransaction().commit();
		return false;
	}
}
