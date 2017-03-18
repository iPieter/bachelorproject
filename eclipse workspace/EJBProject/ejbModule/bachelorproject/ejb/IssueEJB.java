package bachelorproject.ejb;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
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
	
	@Inject
	private TrainCoachEJB traincoachEJB;
	
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
		em.close();
	}

	public List<Issue> findInProgressIssuesByMechanicId( int mechanicId )
	{
		EntityManager em = ems.getEntityManager();
		em.getTransaction().begin();
		TypedQuery<Issue> query = em.createNamedQuery( Issue.FIND_BY_MECHANIC_ID, Issue.class )
				.setParameter( "status", IssueStatus.IN_PROGRESS ).setParameter( "mechanic_id", mechanicId );
		List<Issue> result = query.getResultList();

		em.getTransaction().commit();
		em.close();
		
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
		em.close();
		
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
		em.close();

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
		em.close();

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
		em.close();
		
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
		
		em.merge( issue );
		
		em.getTransaction().commit();
		em.close();
	}
	
	/**
	 * Returns a List of Issue objects for the current User(UserRole=OPERATOR).
	 * The list contains an overview of the Issues that the Operator assigned, 
	 * that are recently closed by a Mechanic. 
	 * <p>
	 * The method uses a query where the userId is used to find the closed issues.
	 * 
	 * @author Matthias De Lange
	 * @version 0.0.1
	 * @param userId the id of operator to find closed issues from
	 * @param issueStatus status of the queried issue 
	 * @return List of Issue objects
	 * @see IndexController
	 */
	public List<Issue> findOperatorIssues( int userId, IssueStatus issueStatus ){
		EntityManager em = ems.getEntityManager();
		em.getTransaction().begin();
		
		TypedQuery<Issue> query = em.createNamedQuery( Issue.FIND_BY_OPERATOR_ID, Issue.class )
									.setParameter("status", issueStatus)
									.setParameter("operator_id", userId );
		List<Issue> result = query.getResultList();
		
		em.getTransaction().commit();

		return result;
	}
	
	/**
	 * Returns the amount of issues from now until backTime days from the selected operator.
	 * 
	 * @author Matthias De Lange
	 * @version 0.0.1
	 * @param userId the id of operator to find issues from
	 * @param backTime the amount of days from now to search on
	 * @return Integer
	 * @see IndexController
	 */
	public int countOperatorIssues( int userId, IssueStatus issueStatus, int backTime ){
		EntityManager em = ems.getEntityManager();
		
		Date now = new Date();
		Timestamp thirtyDaysAgo = new Timestamp(now.getTime() - 86400000 * backTime);
		
		TypedQuery<Long> query = em.createNamedQuery( Issue.COUNT_BY_OPERATOR_ID, Long.class )
									.setParameter("backTime", thirtyDaysAgo);
		int result = query.getFirstResult();

		return result;
	}
	
	/**
	 * Find all active issues. An active issue has IssueStatus=IN_PROGRESS or IssueStatus=ASSIGNED.
	 * 
	 * @author Matthias De Lange
	 * @version 0.0.1
	 * @return List<Issue>
	 * @see HeatmapRestService
	 */
	public List<Issue> findAllActiveIssues(){
		EntityManager em = ems.getEntityManager();
		
		TypedQuery<Issue> query = em.createNamedQuery( Issue.FIND_ALL_ACTIVE, Issue.class )
				.setParameter("status1", IssueStatus.IN_PROGRESS)
				.setParameter("status2", IssueStatus.ASSIGNED );
		List<Issue> result = query.getResultList();
		return result;
	}

	/**
	 * 	Returns the issue specified by the ID.
	 * 	@param issueID 
	 *  @return An Issue if found
	 * */
	public Issue findByID( int issueID )
	{
		EntityManager em = ems.getEntityManager();
		em.getTransaction().begin();
		
		Issue issue = em.find( Issue.class, issueID );
		
		em.getTransaction().commit();
		em.close();
		
		return issue;
	}
	
	/**
	 * 	Closes an issue and sets a traincoach to closed if needed.
	 *  @param issueID The issue to be closed.
	 *  @param currentTrainCoachID The associated traincoach ID.
	 *  @return This method will return true if the traincoach has no more active issues.
	 * */
	public boolean closeIssue( int issueID, int currentTrainCoachID )
	{
		EntityManager em = ems.getEntityManager();
		em.getTransaction().begin();

		boolean success = false;
		Issue i = em.find( Issue.class, issueID );
		if( i != null )
		{
			i.setClosedTime( new Date() );
			i.setStatus( IssueStatus.CLOSED );
			em.merge( i );
			
			List<Issue> issues = findAssignedIssuesByTraincoachId( currentTrainCoachID );
			issues.addAll( findInProgressIssuesByTraincoachId( currentTrainCoachID ) );
			
			Iterator<Issue> iterator = issues.iterator();
			while( iterator.hasNext() )
			{
				Issue ii = iterator.next();
				if( ii.getId() == i.getId() )
					iterator.remove();
			}
				
			if( issues.size() == 0 )
			{
				System.out.println( "Closing traincoach" );
				traincoachEJB.setTrainCoachReviewed( currentTrainCoachID );
				success = true;
			}
		}
		
		em.getTransaction().commit();
		em.close();
		
		return success;
	}
}
