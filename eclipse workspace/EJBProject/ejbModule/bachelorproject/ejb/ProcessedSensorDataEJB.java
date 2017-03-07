package bachelorproject.ejb;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import bachelorproject.model.ProcessedSensorData;

/**
 * 	Defines the Entity Java Bean for the ProcessedSensorData Entity.
 *  <p>
 *  This class allows for the controller to manipulate and fetch specific
 *  ProcessedSensorData instances. It validates new objects and handles 
 *  errors when, for example, no entries in the database exist.
 *  
 * */
@Named
@Stateless
public class ProcessedSensorDataEJB
{
	/**
	 * 	Fetches a ProcessedSensorData from the database if it exists.
	 *  <p>
	 *  Uses an EntityManager to fetch the specified object from the database.
	 *  This method will return null if no object is found or if another error occures.
	 *  @param id The ID of the ProcessedSensorData object thats needed.
	 *  @return A ProcessedSensorData object if found, null otherwise.
	 * */
	public ProcessedSensorData getProcessedSensorDataByID( int id )
	{
		EntityManagerFactory emf = Persistence.createEntityManagerFactory( "EJBProject" );
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		TypedQuery<ProcessedSensorData> query = em.createNamedQuery( "ProcessedSensorData.findByID",
				ProcessedSensorData.class );
		query.setParameter( "id", id );

		ProcessedSensorData data = query.getSingleResult();

		em.getTransaction().commit();
		em.close();
		emf.close();

		return data;
	}
}
