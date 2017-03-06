package bachelorproject.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import bachelorproject.model.*;

@Named
@Stateless
public class InternalDatafetchService
{

	public List<Workplace> getAllWorkplaces()
	{
		EntityManagerFactory emf = Persistence.createEntityManagerFactory( "EJBProject" );
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		TypedQuery<Workplace> query = em.createNamedQuery( "Workplace.findAll", Workplace.class );
		List<Workplace> result = query.getResultList();

		em.getTransaction().commit();
		em.close();
		emf.close();

		return result;
	}

	public List<TrainCoach> getAllTraincoaches()
	{
		EntityManagerFactory emf = Persistence.createEntityManagerFactory( "EJBProject" );
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		TypedQuery<TrainCoach> query = em.createNamedQuery( TrainCoach.FIND_ALL, TrainCoach.class );
		List<TrainCoach> result = query.getResultList();

		em.getTransaction().commit();
		em.close();
		emf.close();

		return result;
	}

	public Workplace findWorkplaceByWorkplaceId( int id )
	{
		EntityManagerFactory emf = Persistence.createEntityManagerFactory( "EJBProject" );
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		Workplace result = em.find( Workplace.class, id );

		em.getTransaction().commit();
		em.close();
		emf.close();

		return result;
	}

	public TrainCoach findTrainCoachByTraincoachId( int id )
	{
		EntityManagerFactory emf = Persistence.createEntityManagerFactory( "EJBProject" );
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		TrainCoach result = em.find( TrainCoach.class, id );

		em.getTransaction().commit();
		em.close();
		emf.close();

		return result;
	}
	
	public List<Workplace> findWorkplaceByTraincoachID( int id )
	{
		EntityManagerFactory emf = Persistence.createEntityManagerFactory( "EJBProject" );
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		TypedQuery<Workplace> query = em.createNamedQuery( "Workplace.findWorkplaceByTraincoachID", Workplace.class );
		query.setParameter( "id", id );
		List<Workplace> result = query.getResultList();
		
		em.getTransaction().commit();
		em.close();
		emf.close();

		return result;
	}
	
	public List<User> getWorkplaceMechanics( int id )
	{
		EntityManagerFactory emf = Persistence.createEntityManagerFactory( "EJBProject" );
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		TypedQuery<User> query = em.createNamedQuery( "Workplace.findWorkers", User.class );
		query.setParameter( "id", id );
		List<User> result = query.getResultList();
		
		em.getTransaction().commit();
		em.close();
		emf.close();

		return result;
	}

	public ProcessedSensorData getProcessedSensorDataByID( int id )
	{
		EntityManagerFactory emf = Persistence.createEntityManagerFactory( "EJBProject" );
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		TypedQuery<ProcessedSensorData> query = em.createNamedQuery( "ProcessedSensorData.findByID", ProcessedSensorData.class );
		query.setParameter( "id", id );
		
		ProcessedSensorData data = query.getSingleResult();
		
		em.getTransaction().commit();
		em.close();
		emf.close();

		return data;
	}
}
