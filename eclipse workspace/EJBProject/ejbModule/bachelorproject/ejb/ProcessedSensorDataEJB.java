package bachelorproject.ejb;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import bachelorproject.model.ProcessedSensorData;

@Named
@Stateless
public class ProcessedSensorDataEJB
{
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
