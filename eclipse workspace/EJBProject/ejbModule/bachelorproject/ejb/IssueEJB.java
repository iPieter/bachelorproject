package bachelorproject.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import bachelorproject.model.Issue;
import bachelorproject.model.TrainCoach;

/**
 * 	Defines the Entity Java Bean for the Issue Entity.
 *  <p>
 *  This class allows for the controller to manipulate and fetch specific
 *  Issue instances. It validates new objects and handles 
 *  errors when, for example, no entries in the database exist.
 *  
 * */
@Named
@Stateless
public class IssueEJB
{
	/**
	 * 	Creates a correct Issue object and persists it to the database
	 * */
	public void createIssue( Issue issue )
	{
		EntityManagerFactory emf = Persistence.createEntityManagerFactory( "EJBProject" );
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		try
		{
			em.persist( issue );
		}
		catch( Exception e )
		{
			System.out.println( "FAILED TO CREATE ISSUE:" + e.getLocalizedMessage() );
		}

		em.getTransaction().commit();
		em.close();
		emf.close();
	}
	
}
