package bachelorproject.ejb;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import bachelorproject.model.LiveSensorData;

/**
 * Defines the Entity Java Bean for the LiveSensorData Entity.
 * <p>
 * This class allows for the controller to manipulate and fetch specific LiveSensorData
 * instances. It validates new objects and handles errors when, for example, no
 * entries in the database exist.
 * @author Anton Danneels
 * @see LiveSensorData
 */
@Named
@Stateless
public class LiveSensorDataEJB
{
	@Inject
	private EntityManagerSingleton ems;
	
	/**
	 * 	Persists a LiveSensorData object to the database if it is correct
	 *  @param lsd The LiveSensorDataObject to be persisted
	 * */
	public int createLiveSensorData( LiveSensorData lsd )
	{
		EntityManager em = ems.getEntityManager();
		em.getTransaction().begin();
		
		em.persist( lsd );
		
		em.getTransaction().commit();
		em.close();
		
		return lsd.getId();
	}
	
	/**
	 * 	Closes the LiveSensorData specified by lsdID.
	 *  <p>
	 *  When this method gets called, it is a signal that the ride has ended.
	 *  This function closes the live sensor data object, generates a new JSON 
	 *  file from this and adds a new ProcessedSensorData object and deletes all
	 *  entries from this ride.
	 *  @param lsdID The LiveSensorDataObject
	 * */
	public void closeLiveSensorData( int lsdID )
	{
		EntityManager em = ems.getEntityManager();
		em.getTransaction().begin();
		
		LiveSensorData sensordata = em.find( LiveSensorData.class, lsdID );
		
		if( sensordata != null )
		{
			sensordata.setLive( false );
			
			//TODO: all the other shit
		}
		
		em.getTransaction().commit();
		em.close();
	}
	
	/**
	 * 	Finds a LiveSensorDataEntry and returns it if it exists
	 *  @param lsdID The object that needs to be found
	 * */
	public LiveSensorData findLSDByID( int lsdID )
	{
		EntityManager em = ems.getEntityManager();
		em.getTransaction().begin();
		
		LiveSensorData sensordata = em.find( LiveSensorData.class, lsdID );
		
		em.getTransaction().commit();
		em.close();
	
		return sensordata;
	}
	
	/**
	 * 	Finds a LiveSensorDataEntry and returns it if it exists
	 *  @param lsdID The object that needs to be found
	 * */
	public LiveSensorData findLSDByDate( Date date )
	{
		EntityManager em = ems.getEntityManager();
		em.getTransaction().begin();
		
		TypedQuery<LiveSensorData> query = em.createNamedQuery( LiveSensorData.FIND_BY_DATE, LiveSensorData.class );
		query.setParameter( "date", date, TemporalType.TIMESTAMP );
		
		List<LiveSensorData> result = query.getResultList();
		
		em.getTransaction().commit();
		em.close();
	
		if( result.size() == 0 )
			return null;
		return result.get( 0 );
	}
	
	/**
	 * 	Returns a list of all active sensors.
	 *  @return The list of all active sensors
	 * */
	public List<LiveSensorData> getAllActiveSensorObjects()
	{
		EntityManager em = ems.getEntityManager();
		em.getTransaction().begin();
		
		TypedQuery<LiveSensorData> query = em.createNamedQuery( LiveSensorData.FIND_ALL_ACTIVE, LiveSensorData.class );
		
		List<LiveSensorData> result = query.getResultList();
		
		em.getTransaction().commit();
		em.close();
	
		return result;
	}
}
