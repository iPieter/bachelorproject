package controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import bachelorproject.ejb.IssueEJB;
import bachelorproject.ejb.ProcessedSensorDataEJB;
import bachelorproject.ejb.TrainCoachEJB;
import bachelorproject.ejb.WorkplaceEJB;
import bachelorproject.model.Issue;
import bachelorproject.model.IssueStatus;
import bachelorproject.model.ProcessedSensorData;
import bachelorproject.model.TrainCoach;
import bachelorproject.model.User;
import bachelorproject.model.Workplace;
import bachelorproject.services.UserService;

@Named
//@RequestScoped
@SessionScoped
public class WorkplaceController implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Inject
	private WorkplaceEJB workplaceEJB;
	@Inject
	private TrainCoachEJB traincoachEJB;
	@Inject
	private ProcessedSensorDataEJB psdEJB;
	@Inject
	private IssueEJB issueEJB;
	@Inject
	private UserService userService;

	private ProcessedSensorData currentSensorData = new ProcessedSensorData();

	//TODO zijn attr nodig? ==>rechtstreeks in methoden OF via init methode die alles aanroept
	private TrainCoach currentTrainCoach = new TrainCoach();
	private Workplace currentWorkplace = new Workplace();
	private List<User> mechanics = new ArrayList<User>();
	
	@NotNull
	@Size( min=10, max= 1000 )
	private String description = "";
	
	@NotNull
	private String mechanicID = "";

	//TODO using attr? or returning list?
	public void findTrainCoachByTraincoachId()
	{
		currentTrainCoach = traincoachEJB.findTrainCoachByTraincoachId( currentTrainCoach.getId() );
		if( currentTrainCoach == null )
			currentTrainCoach = new TrainCoach();
		List<Workplace> result = workplaceEJB.findWorkplaceByTraincoachID( currentTrainCoach.getId() );
		if ( result.size() > 0 )
		{
			currentWorkplace = result.get( 0 );
		}
		currentSensorData = psdEJB.getProcessedSensorDataByTrainCoachID( currentTrainCoach.getId() );
		if( currentSensorData == null )
			System.out.println( "Failed to locate sensordata" );
		
		mechanics.clear();
		for ( User u : workplaceEJB.findMechanicsByWorkplaceId( currentWorkplace.getId() ) )
			mechanics.add( u );
	}

	//TODO using attr? or returning list?
	public void findWorkplaceByWorkplaceId()
	{
		currentWorkplace = workplaceEJB.findWorkplaceByWorkplaceId( currentWorkplace.getId() );
	}
	
	/**
	 * 	Calls into the TrainCoachEJB object to set this TrainCoach as reviewed.
	 *  @return If this method succeeds, it will redirect to index
	 * */
	public String setTrainCoachReviewed()
	{
		if( currentTrainCoach != null)
		{
			traincoachEJB.setTrainCoachReviewed( currentTrainCoach.getId() );
			return "index.xhtml";
		}
		return null;
	}
	
	/**
	 * 	Returns a list of all the current active TrainCoaches of the currentWorkplace object.
	 * 	@return A List of TrainCoach objects stored in the currentWorkplace.
	 * */
	public List<TrainCoach> findActiveTrainCoaches( )
	{
		return traincoachEJB.getAllTraincoachesNeedReview( currentWorkplace.getId() );
	}
	
	/**
	 * 	Calls into the IssueEJB to create new issues.
	 * */
	public String createIssue()
	{
		currentSensorData = psdEJB.getProcessedSensorDataByTrainCoachID( currentTrainCoach.getId() );
		
		Issue issue = new Issue();
		issue.setData( currentSensorData );
		issue.setDescr( description );
		
		User m = new User();
		for( User mm : mechanics )
		{
			if( mm.getId() == Integer.parseInt( mechanicID ) )
				m = mm;
		}
		
		issue.setMechanic( m );
		issue.setOperator( userService.getUser() );
		issue.setStatus( IssueStatus.ASSIGNED );
		
		issueEJB.createIssue( issue );
		
		return "index.html";
	}

	public List<String> findActiveTraincoachProblemsById( int traincoachId )
	{
		// TODO EJB model side!
		List<String> result = new ArrayList<>();
		result.add( "Active Problemmethod TODO in model" );
		return result;
  }
	//MECHANICS OF CURRENT WORKPLACE
	/**
	 * Returns a List of User objects(UserRole=MECHANIC) for the current Workplace.
	 *
	 * @author Matthias De Lange
	 * @version 0.0.1
	 * @return List of User objects
	 * @see workplaceEJB
	 */
	public List<User> findMechanicsOfCurrentWorkplace(){
		return workplaceEJB.findMechanicsByWorkplaceId(currentWorkplace.getId());
	}

	//ISSUES BY TRAINCOACH_ID
	/**
	 * Returns a List of Issue objects for a given traincoachId.
	 * Therefore it queries Issues with issueStates IN_PROGRESS or ASSIGNED.
	 *
	 * @author Matthias De Lange
	 * @version 0.0.1
	 * @param traincoachId
	 * @return List of Issue objects
	 * @see findActiveIssuesByTraincoachId(),findInProgressIssuesByTraincoachId()
	 */
	public List<Issue> findActiveIssuesByTraincoachId( int traincoachId )
	{
		// TODO ERROR CHECKING
		List<Issue> result = new ArrayList<>();
		result.addAll( findInProgressIssuesByTraincoachId( traincoachId ));
		result.addAll( findAssignedIssuesByTraincoachId( traincoachId ));
		return result;
	}
	
	/**
	 * Returns a List of Issue objects for a given traincoachId.
	 * All queried issues have an IssueState=IN_PROGRESS
	 * Therefore it calls the IssueEJB
	 *
	 * @author Matthias De Lange
	 * @version 0.0.1
	 * @param traincoachId
	 * @return List of Issue objects
	 * @see Issue, IssueEJB
	 */
	public List<Issue> findInProgressIssuesByTraincoachId( int traincoachId ){
		return issueEJB.findInProgressIssuesByTraincoachId(traincoachId);
	}
	
	/**
	 * Returns a List of Issue objects for a given traincoachId.
	 * All queried issues have an IssueState=ASSIGNED
	 * Therefore it calls the IssueEJB
	 *
	 * @author Matthias De Lange
	 * @version 0.0.1
	 * @param traincoachId
	 * @return List of Issue objects
	 * @see Issue,IssueEJB
	 */
	public List<Issue> findAssignedIssuesByTraincoachId( int traincoachId ){
		return issueEJB.findAssignedIssuesByTraincoachId(traincoachId);
	}
	
	/**
	 * Returns a List of Issue objects for a given traincoachId.
	 * All queried issues have an IssueState=CLOSED
	 * Therefore it calls the IssueEJB
	 *
	 * @author Matthias De Lange
	 * @version 0.0.1
	 * @param traincoachId
	 * @return List of Issue objects
	 * @see Issue, IssueEJB
	 */
	public List<Issue> findClosedIssuesByTraincoachId( int traincoachId ){
		return issueEJB.findClosedIssuesByTraincoachId(traincoachId);
	}

	//ISSUES BY MECHANIC_ID
	/**
	 * Returns a List of Issue objects for a given mechanicId.
	 * All queried issues have an IssueState IN_PROGRESS or ASSIGNED.
	 * Therefore it calls the IssueEJB
	 *
	 * @author Matthias De Lange
	 * @version 0.0.1
	 * @param traincoachId
	 * @return List of Issue objects
	 * @see findInProgressIssuesByMechanicId(), findAssignedIssuesByMechanicId()
	 */
	public List<Issue> findActiveIssuesByMechanicId( int mechanicId )
	{
		// TODO ERRORCHECKING
		List<Issue> result = new ArrayList();
		result.addAll(findInProgressIssuesByMechanicId(mechanicId));
		result.addAll(findAssignedIssuesByMechanicId(mechanicId));
		return result;
	}

	public List<TrainCoach> getAllTraincoaches()
	{
		return traincoachEJB.getAllTraincoachesNeedReview( currentWorkplace.getId() );
	}
	/**
	 * Returns a List of Issue objects for a given mechanicId.
	 * All queried issues have an IssueState IN_PROGRESS.
	 * Therefore it calls the IssueEJB
	 *
	 * @author Matthias De Lange
	 * @version 0.0.1
	 * @param traincoachId
	 * @return List of Issue objects
	 * @see Issue, IssueEJB
	 */
	public List<Issue> findInProgressIssuesByMechanicId( int mechanicId ){
		return issueEJB.findInProgressIssuesByMechanicId(mechanicId);
	}
	
	/**
	 * Returns a List of Issue objects for a given mechanicId.
	 * All queried issues have an IssueState ASSIGNED.
	 * Therefore it calls the IssueEJB
	 *
	 * @author Matthias De Lange
	 * @version 0.0.1
	 * @param traincoachId
	 * @return List of Issue objects
	 * @see Issue, IssueEJB
	 */
	public List<Issue> findAssignedIssuesByMechanicId( int mechanicId ){
		return issueEJB.findAssignedIssuesByMechanicId(mechanicId);
	}

	// GETTERS & SETTERS
	public Workplace getCurrentWorkplace()
	{
		return currentWorkplace;
	}

	public List<Workplace> getAllWorkplaces()
	{
		return workplaceEJB.getAllWorkplaces();
	}

	public TrainCoach getCurrentTrainCoach()
	{
		return currentTrainCoach;
	}

	public List<User> getMechanics()
	{
		return mechanics;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public void setDescription( String description )
	{
		this.description = description;
	}

	public String getMechanicID()
	{
		return mechanicID;
	}

	public void setMechanicID( String mechanicID )
	{
		this.mechanicID = mechanicID;
	}

	public ProcessedSensorData getCurrentSensorData()
	{
		return currentSensorData;
	}

	public void setCurrentSensorData( ProcessedSensorData currentSensorData )
	{
		this.currentSensorData = currentSensorData;
	}
	
}
