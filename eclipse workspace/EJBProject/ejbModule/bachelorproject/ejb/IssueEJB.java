package bachelorproject.ejb;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import bachelorproject.model.issue.Issue;
import bachelorproject.model.issue.IssueAsset;
import bachelorproject.model.issue.IssueStatus;
import bachelorproject.model.user.User;

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
public class IssueEJB // TODO: when changing issue status, timestamp must be
						// taken.
{
	@Inject
	private EntityManagerSingleton ems;

	@Inject
	private TrainCoachEJB traincoachEJB;

	/**
	 * Creates a correct Issue object and persists it to the database
	 * 
	 * @author Anton
	 * @param issue
	 *            The Issue object that should be persisted to the database.
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

	/**
	 * Retrieves a list of issues that are in progress based on the specified
	 * mechanic ID.
	 * 
	 * @param mechanicId
	 *            The ID of the mechanic who's in progress issues to fetch
	 * @return A List of issues for the target mechanic
	 */
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

	/**
	 * Retrieves a list of issues that are in assigned based on the specified
	 * mechanic ID.
	 * 
	 * @param mechanicId
	 *            The ID of the mechanic who's assigned issues to fetch
	 * @return A List of issues for the target mechanic
	 */
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

	/**
	 * Generates a list of all the know issues for a provided sensor data id.
	 * This can be from either a live or a processes sensor data object.
	 * <p>
	 * If no issues are found, an empty list is returned.
	 * 
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @param dataId
	 *            The id of the sensor data (processed or live, doesn't matter).
	 * @return A list object with 0 or more issue objects.
	 * @see Issue
	 */
	public List<Issue> findIssuesByDataId( int dataId )
	{
		EntityManager em = ems.getEntityManager();

		TypedQuery<Issue> query = em.createNamedQuery( Issue.FIND_BY_SENSOR_ID, Issue.class );
		query.setParameter( "id", dataId );
		List<Issue> result = query.getResultList();

		em.close();

		return result;
	}

	/**
	 * Returns a List of Issue objects for a given traincoachId. Therefore it
	 * queries Issues with issueStates IN_PROGRESS or ASSIGNED.
	 *
	 * @author Matthias De Lange
	 * @version 0.0.1
	 * @param traincoachId
	 * @return List of Issue objects
	 * @see findInProgressIssuesByTraincoachId(),
	 *      findAssignedIssuesByTraincoachId()
	 */
	public List<Issue> findActiveIssuesByTraincoachId( int traincoachId )
	{
		List<Issue> result = new ArrayList<>();
		result.addAll( findInProgressIssuesByTraincoachId( traincoachId ) );
		result.addAll( findAssignedIssuesByTraincoachId( traincoachId ) );
		return result;
	}

	/**
	 * Retrieves a list of issues that are in progress based on the specified
	 * traincoach ID.
	 * 
	 * @param traincoachId
	 *            The ID of the traincoach who's in progress issues to fetch
	 * @return A List of issues for the target traincoach
	 */
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

	/**
	 * Retrieves a list of issues that are assigned based on the specified
	 * traincoach ID.
	 * 
	 * @param traincoachId
	 *            The ID of the traincoach who's in progress issues to fetch
	 * @return A List of issues for the target traincoach
	 */
	public List<Issue> findAssignedIssuesByTraincoachId( int traincoachId )
	{
		EntityManager em = ems.getEntityManager();

		TypedQuery<Issue> query = em.createNamedQuery( Issue.FIND_BY_TRAINCOACH_ID, Issue.class )
				.setParameter( "status", IssueStatus.ASSIGNED ).setParameter( "traincoachId", traincoachId );
		List<Issue> result = query.getResultList();

		em.close();

		return result;
	}

	/**
	 * Retrieves a list of issues that are closed based on the specified
	 * mechanic ID.
	 * 
	 * @param traincoachId
	 *            The ID of the mechanic who's closed issues to fetch
	 * @return A List of issues for the target traincaoch
	 */
	public List<Issue> findClosedIssuesByTraincoachId( int traincoachId )
	{
		EntityManager em = ems.getEntityManager();
		em.getTransaction().begin();

		TypedQuery<Issue> query = em.createNamedQuery( Issue.FIND_BY_TRAINCOACH_ID, Issue.class )
				.setParameter( "status", IssueStatus.CLOSED ).setParameter( "traincoachId", traincoachId );
		List<Issue> result = query.getResultList();

		em.getTransaction().commit();
		em.close();

		return result;
	}

	/**
	 * Adds an issue asset to the Issue specified with the ID.
	 * 
	 * @param asset
	 *            A valid, already persisted, IssueAsset object.
	 * @param id
	 *            The ID of the Issue this asset should be linked to.
	 */
	public void addAsset( IssueAsset asset, int issueID )
	{
		// TODO proper exception handling
		EntityManager em = ems.getEntityManager();
		em.getTransaction().begin();

		Issue issue = em.find( Issue.class, issueID );

		if ( issue != null ) issue.getAssets().add( asset );
		else System.out.println( "ERROR(issueEJB): no issue found" );

		em.merge( issue );

		em.getTransaction().commit();
		em.close();
	}

	/**
	 * Returns a List of Issue objects for the current User(UserRole=OPERATOR).
	 * The list contains an overview of the Issues that the Operator assigned,
	 * that are recently closed by a Mechanic.
	 * <p>
	 * The method uses a query where the userId is used to find the closed
	 * issues.
	 * 
	 * @author Matthias De Lange
	 * @version 0.0.1
	 * @param userId
	 *            the id of operator to find closed issues from
	 * @param issueStatus
	 *            status of the queried issue
	 * @return List of Issue objects
	 * @see IndexController
	 */
	public List<Issue> findOperatorIssues( int userId, IssueStatus issueStatus )
	{
		EntityManager em = ems.getEntityManager();
		em.getTransaction().begin();

		TypedQuery<Issue> query = em.createNamedQuery( Issue.FIND_BY_OPERATOR_ID, Issue.class )
				.setParameter( "status", issueStatus ).setParameter( "operator_id", userId );
		List<Issue> result = query.getResultList();

		em.getTransaction().commit();

		return result;
	}

	/**
	 * Returns the amount of issues from now until backTime days from the
	 * selected operator.
	 * 
	 * @author Matthias De Lange
	 * @version 0.0.1
	 * @param userId
	 *            the id of operator to find issues from
	 * @param backTime
	 *            the amount of days from now to search on
	 * @return Integer
	 * @see IndexController
	 */
	public int countOperatorIssues( int userId, IssueStatus issueStatus, int backTime )
	{
		EntityManager em = ems.getEntityManager();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime( new Date() );
		calendar.add( Calendar.DAY_OF_WEEK, -backTime ); // Subtracting
															// 'backTime' days
															// from now
		Timestamp sqldate = new Timestamp( calendar.getTimeInMillis() );
		System.out.println( "DONUT: Thirtydaysago = " + sqldate );

		TypedQuery<Long> query = em.createNamedQuery( Issue.COUNT_BY_OPERATOR_ID, Long.class )
				.setParameter( "backTime", sqldate ).setParameter( "operatorId", userId )
				.setParameter( "status", issueStatus );
		int result = ( (Integer) query.getSingleResult().intValue() );

		return result;
	}

	/**
	 * Find all active issues. An active issue has IssueStatus=IN_PROGRESS or
	 * IssueStatus=ASSIGNED.
	 * 
	 * @author Matthias De Lange
	 * @version 0.0.1
	 * @return List<Issue>
	 * @see HeatmapRestService
	 */
	public List<Issue> findAllActiveIssues()
	{
		EntityManager em = ems.getEntityManager();

		TypedQuery<Issue> query = em.createNamedQuery( Issue.FIND_ALL_ACTIVE, Issue.class )
				.setParameter( "status1", IssueStatus.IN_PROGRESS ).setParameter( "status2", IssueStatus.ASSIGNED );
		List<Issue> result = query.getResultList();
		return result;
	}

	/**
	 * Returns the issue specified by the ID.
	 * 
	 * @param issueID
	 * @return An Issue if found
	 */
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
	 * Closes an issue and sets a traincoach to closed if needed.
	 * 
	 * @param issueID
	 *            The issue to be closed.
	 * @param currentTrainCoachID
	 *            The associated traincoach ID.
	 * @return This method will return true if the traincoach has no more active
	 *         issues.
	 */
	public boolean closeIssue( int issueID, int currentTrainCoachID )
	{
		EntityManager em = ems.getEntityManager();
		em.getTransaction().begin();

		boolean success = false;
		Issue i = em.find( Issue.class, issueID );
		if ( i != null )
		{
			i.setClosedTime( new Date() );
			i.setStatus( IssueStatus.CLOSED );
			em.merge( i );

			List<Issue> issues = findAssignedIssuesByTraincoachId( currentTrainCoachID );
			issues.addAll( findInProgressIssuesByTraincoachId( currentTrainCoachID ) );

			Iterator<Issue> iterator = issues.iterator();
			while ( iterator.hasNext() )
			{
				Issue ii = iterator.next();
				if ( ii.getId() == i.getId() ) iterator.remove();
			}

			if ( issues.size() == 0 )
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

	/**
	 * Sensor data can be coupled to different types: live & processed. When
	 * switching from live to processed, these must be transfer to the new
	 * object. This method allows to fetch those issues by sensordata ID.
	 * 
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @param sensorDataId
	 *            The id of the sensordata id.
	 * @return A list with issues for the provided sensordata id;
	 */
	public List<Issue> getIssuesBySensorDataID( int sensorDataId )
	{
		EntityManager em = ems.getEntityManager();
		em.getTransaction().begin();

		TypedQuery<Issue> query = em.createNamedQuery( Issue.FIND_BY_SENSOR_ID, Issue.class );

		query.setParameter( "id", sensorDataId );

		List<Issue> result = query.getResultList();

		em.getTransaction().commit();
		em.close();

		return result;
	}

	/**
	 * Returns active issues (IssueStatus=IN_PROGRESS or IssueStatus=ASSIGNED)
	 * by workplaceId
	 * 
	 * @author Matthias De Lange
	 * @version 0.0.1
	 * @param workplaceId
	 *            the id of the workplace
	 * @return List<Issue> a list of issues
	 * @see IndexController
	 */
	public List<Issue> findByWorkplaceId( int workplaceId )
	{
		EntityManager em = ems.getEntityManager();

		TypedQuery<Issue> query = em.createNamedQuery( Issue.FIND_BY_WORKPLACE_ID, Issue.class )
				.setParameter( "workplaceId", workplaceId );
		List<Issue> result = query.getResultList();

		return result;
	}

	/**
	 * If for some reason your Issue object became detached from the persistense
	 * context, you can merge it here.
	 * 
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @param issue
	 *            The issue object to merge.
	 */
	public void updateIssue( Issue issue )
	{
		EntityManager em = ems.getEntityManager();
		em.getTransaction().begin();

		em.merge( issue );

		em.getTransaction().commit();

		em.close();
	}
}
