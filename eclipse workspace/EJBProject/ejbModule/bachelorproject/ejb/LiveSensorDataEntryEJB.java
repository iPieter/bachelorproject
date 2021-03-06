package bachelorproject.ejb;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import bachelorproject.constraint_engine.ConstraintEngine;
import bachelorproject.constraint_engine.ConstraintEngineData;
import bachelorproject.constraint_engine.ConstraintEngineFactory;
import bachelorproject.constraint_engine.OutOfConstraintEngineException;
import bachelorproject.model.sensordata.LiveSensorData;
import bachelorproject.model.sensordata.LiveSensorDataEntry;

/**
 * Defines the Entity Java Bean for the LiveSensorDataEntry Entity.
 * <p>
 * This class allows for the controller to manipulate and fetch specific
 * LiveSensorDataEntry instances. It validates new objects and handles errors
 * when, for example, no entries in the database exist.
 * 
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

	@Inject
	private ConstraintEngineFactory cef;

	/**
	 * If the specified LiveSensorData object is valid ( the ride is live ),
	 * this method will add a new row to the database.
	 * 
	 * @param lsdID
	 *            The ID of the LiveSensorData object that this entry is related
	 *            to
	 * @param entry
	 *            The new entry to be added.
	 */
	public boolean addEntry( int lsdID, LiveSensorDataEntry entry )
	{
		EntityManager em = ems.getEntityManager();

		LiveSensorData lsd = lsdEJB.findLSDByID( lsdID );

		em.getTransaction().begin();

		if ( lsd != null && lsd.isLive() )
		{
			lsd.getEntries().add( entry );
			em.persist( entry );
			em.merge( lsd );

			em.getTransaction().commit();
			em.close();

			try
			{
				ConstraintEngine ce = cef.getConstraintEngine();
				ConstraintEngineData ceData = new ConstraintEngineData();
				ceData.setAccel( entry.getAccel() );
				ceData.setSpeed( entry.getSpeed() );
				ceData.setLat( entry.getLat() );
				ceData.setLng( entry.getLng() );
				ceData.setRoll( entry.getRoll() );
				ceData.setYaw( entry.getYaw() );

				ce.start( lsd );
				ce.addData( ceData );
				ce.stop();
			}
			catch ( OutOfConstraintEngineException e )
			{
				e.printStackTrace();
			}

			return true;
		}

		em.getTransaction().commit();
		em.close();
		return false;
	}

	/**
	 * Returns a list of all LiveSensorDataEntry objects after the specified
	 * date
	 * 
	 * @param lsdID
	 *            The ID of the LiveSensorData object to fetch
	 * @param The
	 *            date after which to fetch the entries
	 * @return A list of all entries after a date
	 */
	public List<LiveSensorDataEntry> getAllEntriesAfterDate( int lsdID, Date date )
	{
		EntityManager em = ems.getEntityManager();
		em.getTransaction().begin();

		TypedQuery<LiveSensorDataEntry> query = em.createNamedQuery( LiveSensorData.FIND_ALL_AFTER_DATE,
				LiveSensorDataEntry.class );
		query.setParameter( "id", lsdID );
		query.setParameter( "date", date, TemporalType.TIMESTAMP );

		List<LiveSensorDataEntry> result = query.getResultList();

		em.getTransaction().commit();
		em.close();

		return result;
	}
}
