package bachelorproject.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import bachelorproject.model.Issue;
import bachelorproject.model.IssueAsset;
import bachelorproject.model.IssueStatus;

/**
 * Defines the Entity Java Bean for the Issue Entity.
 * <p>
 * This class allows for the controller to manipulate and fetch specific Issue
 * instances. It validates new objects and handles errors when, for example, no
 * entries in the database exist.
 * 
 */
@Named
@Stateless
public class IssueEJB //TODO: when changing issue status, timestamp must be taken.
{
	@Inject
	private EntityManagerSingleton ems;
	
	/**
	 * Creates a correct Issue object and persists it to the database
	 * @author Anton
	 * @param issue The Issue object that should be persisted to the database.
	 */
	public void createIssue( Issue issue )
	{
		EntityManager em = ems.getEntityManager();
		em.getTransaction().begin();

		try
		{
			em.persist( issue );
		}
		catch ( Exception e )
		{
			System.out.println( "FAILED TO CREATE ISSUE:" + e.getLocalizedMessage() );
		}

		em.getTransaction().commit();
	}

	public List<Issue> findInProgressIssuesByMechanicId( int mechanicId )
	{
		EntityManager em = ems.getEntityManager();
		em.getTransaction().begin();
		TypedQuery<Issue> query = em.createNamedQuery( Issue.FIND_BY_MECHANIC_ID, Issue.class )
				.setParameter( "status", IssueStatus.IN_PROGRESS ).setParameter( "mechanic_id", mechanicId );
		List<Issue> result = query.getResultList();

		em.getTransaction().commit();

		return result;
	}

	public List<Issue> findAssignedIssuesByMechanicId( int mechanicId )
	{
		EntityManager em = ems.getEntityManager();
		em.getTransaction().begin();

		TypedQuery<Issue> query = em.createNamedQuery( Issue.FIND_BY_MECHANIC_ID, Issue.class )
				.setParameter( "status", IssueStatus.ASSIGNED ).setParameter( "mechanic_id", mechanicId );
		List<Issue> result = query.getResultList();

		em.getTransaction().commit();

		return result;
	}

	public List<Issue> findInProgressIssuesByTraincoachId( int traincoachId )
	{
		EntityManager em = ems.getEntityManager();
		em.getTransaction().begin();

		TypedQuery<Issue> query = em.createNamedQuery( Issue.FIND_BY_TRAINCOACH_ID, Issue.class )
				.setParameter( "status", IssueStatus.IN_PROGRESS ).setParameter( "traincoachId", traincoachId );
		List<Issue> result = query.getResultList();

		em.getTransaction().commit();

		return result;
	}

	public List<Issue> findAssignedIssuesByTraincoachId( int traincoachId )
	{
		EntityManager em = ems.getEntityManager();
		em.getTransaction().begin();

		TypedQuery<Issue> query = em.createNamedQuery( Issue.FIND_BY_TRAINCOACH_ID, Issue.class )
				.setParameter( "status", IssueStatus.ASSIGNED ).setParameter( "traincoachId", traincoachId );
		List<Issue> result = query.getResultList();
		
		em.getTransaction().commit();

		return result;
	}

	public List<Issue> findClosedIssuesByTraincoachId(int traincoachId)
	{
		EntityManager em = ems.getEntityManager();
		em.getTransaction().begin();
		
		TypedQuery<Issue> query = em.createNamedQuery( Issue.FIND_BY_TRAINCOACH_ID, Issue.class )
									.setParameter("status", IssueStatus.CLOSED)
									.setParameter("traincoachId", traincoachId);
		List<Issue> result = query.getResultList();
		
		em.getTransaction().commit();

		return result;
	}

	/**
	 * 	Adds an issue asset to the Issue specified with the ID.
	 *  @param asset A valid, already persisted, IssueAsset object.
	 *  @param id The ID of the Issue this asset should be linked to.
	 * */
	public void addAsset( IssueAsset asset, int issueID )
	{
		//TODO proper exception handling
		EntityManager em = ems.getEntityManager();
		em.getTransaction().begin();
		
		Issue issue = em.find( Issue.class, issueID );
		
		if( issue != null )
			issue.getAssets().add( asset );
		else
			System.out.println( "ERROR(issueEJB): no issue found" );
		
		em.persist( issue );
		
		em.getTransaction().commit();
	}
	
	/**
	 * Returns a List of Issue objects for the current User(UserRole=OPERATOR).
	 * The list contains an overview of the Issues that the Operator assigned, 
	 * that are recently closed by a Mechanic. Thus IssueStatus=CLOSED for all returned issues.
	 * <p>
	 * The method uses a query where the userId is used to find the closed issues.
	 * 
	 * @author Matthias De Lange
	 * @version 0.0.1
	 * @param userId the id of operator to find closed issues from
	 * @return List of Issue objects
	 * @see IndexController
	 */
	public List<Issue> findOperatorClosedIssues( int userId ){
		EntityManager em = ems.getEntityManager();
		em.getTransaction().begin();
		
		TypedQuery<Issue> query = em.createNamedQuery( Issue.FIND_BY_OPERATOR_ID, Issue.class )
									.setParameter("status", IssueStatus.CLOSED)
									.setParameter("operator_id", userId );
		List<Issue> result = query.getResultList();
		
		em.getTransaction().commit();

		return result;
	}
	
	/**
	 * Returns a List of Issue objects for the current User(UserRole=OPERATOR).
	 * The list contains an overview of the Issues that the Operator assigned, 
	 * that are in progress by a Mechanic. Thus IssueStatus=IN_PROGRESS for all returned issues.
	 * <p>
	 * The method uses a query where the userId is used to find the issues in progress.
	 * 
	 * @author Matthias De Lange
	 * @version 0.0.1
	 * @param userId the id of operator to find issues in progress from
	 * @return List of Issue objects
	 * @see IndexController
	 */
	public List<Issue> findOperatorInProgressIssues( int userId ){
		EntityManager em = ems.getEntityManager();
		em.getTransaction().begin();
		
		TypedQuery<Issue> query = em.createNamedQuery( Issue.FIND_BY_OPERATOR_ID, Issue.class )
									.setParameter("status", IssueStatus.IN_PROGRESS)
									.setParameter("operator_id", userId );
		List<Issue> result = query.getResultList();
		
		em.getTransaction().commit();

		return result;
	}
}
