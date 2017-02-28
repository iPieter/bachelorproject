package be.kuleuven.cs.gent.projectweek.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import be.kuleuven.cs.gent.projectweek.model.*;

@Named
@Stateless
public class InternalDatafetchService
{

	public List<Workplace> getAllTraincoachDepots()
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

		TypedQuery<TrainCoach> query = em.createNamedQuery( "TrainCoach.findAll", TrainCoach.class );
		List<TrainCoach> result = query.getResultList();

		em.getTransaction().commit();
		em.close();
		emf.close();

		return result;
	}

	public Workplace doFindTrainCoachDepotById( int id )
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

	public TrainCoach doFindTrainCoachById( int id )
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
}
