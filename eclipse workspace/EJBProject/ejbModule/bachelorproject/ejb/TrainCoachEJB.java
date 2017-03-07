package bachelorproject.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import bachelorproject.model.TrainCoach;

/**
 * 	Defines the Entity Java Bean for the TrainCoach Entity.
 *  <p>
 *  This class allows for the controller to manipulate and fetch specific
 *  TrainCoach instances. It validates new objects and handles 
 *  errors when, for example, no entries in the database exist.
 *  
 * */
@Named
@Stateless
public class TrainCoachEJB
{
	/**
	 * 	Retrieves ALL TrainCoaches from the database
	 *  @return A List of TrainCoach objects
	 *  @see List
	 *  @see TrainCoach
	 * */
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
	
	/**
	 * 	Retrieves ALL TrainCoaches from the database that need a review
	 *  based on the workplaceID.
	 *  @param workplaceID The ID of the Workplace that needs the traincoaches.
	 *  @return A List of TrainCoach objects that need review.
	 *  @see List
	 *  @see TrainCoach
	 * */
	public List<TrainCoach> getAllTraincoachesNeedReview( int workplaceID )
	{
		EntityManagerFactory emf = Persistence.createEntityManagerFactory( "EJBProject" );
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		TypedQuery<TrainCoach> query = em.createNamedQuery( TrainCoach.FIND_ALL_NEEDS_REVIEW, TrainCoach.class );
		query.setParameter( "id", workplaceID );
		List<TrainCoach> result = query.getResultList();

		for( TrainCoach t : result )
		{
			System.out.println( t );
		}
		
		em.getTransaction().commit();
		em.close();
		emf.close();

		return result;
	}

	/**
	 * 	Finds a TrainCoach in the database based on the ID.
	 *  @return The found TrainCoach object or null if it does not exist
	 *  @see TrainCoach
	 * */
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
	
	/**
	 * 	Sets the needsReview flag to false in the database.
	 * 	<p>
	 * 	Based on the id parameter this method will find the TrainCoach
	 *  object in the database and set its "needsReview" flag on true.
	 *  @param id The ID of the TrainCoach object that needs to be changed.
	 *  @see TrainCoach
	 * */
	public void setTrainCoachReviewed( int id )
	{
		EntityManagerFactory emf = Persistence.createEntityManagerFactory( "EJBProject" );
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		TrainCoach result = em.find( TrainCoach.class, id );
		
		if( result != null )
			result.setNeedsReview( false );

		em.persist( result );
		em.getTransaction().commit();
		em.close();
		emf.close();
	}
}
