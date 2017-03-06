package bachelorproject.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import bachelorproject.model.TrainCoach;

@Named
@Stateless
public class TrainCoachEJB
{
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
}
