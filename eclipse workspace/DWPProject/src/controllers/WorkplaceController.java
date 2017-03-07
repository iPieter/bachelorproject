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
	private TrainCoach currentTrainCoach = new TrainCoach();
	private Workplace currentWorkplace = new Workplace();
	private List<User> mechanics = new ArrayList<User>();
	
	@NotNull
	@Size( min=10, max= 1000 )
	private String description = "";
	
	@NotNull
	private String mechanicID = "";

	public void findTrainCoachByTraincoachId()
	{
		currentTrainCoach = traincoachEJB.findTrainCoachByTraincoachId( currentTrainCoach.getId() );
		List<Workplace> result = workplaceEJB.findWorkplaceByTraincoachID( currentTrainCoach.getId() );
		if ( result.size() > 0 )
		{
			currentWorkplace = result.get( 0 );
		}
		currentSensorData = psdEJB.getProcessedSensorDataByTrainCoachID( currentTrainCoach.getId() );
		
		mechanics.clear();
		for ( User u : workplaceEJB.getWorkplaceMechanics( currentWorkplace.getId() ) )
			mechanics.add( u );
	}

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

	public List<String> findSolvedTraincoachProblemsById( int traincoachId )
	{
		// TODO EJB model side!
		List<String> result = new ArrayList<>();
		result.add( "Solved Problemmethod TODO in model" );
		return result;
	}

	public List<String> findActiveIssuesByMechanicId( int mechanicId )
	{
		// TODO EJB model side!
		List<String> result = new ArrayList<>();
		result.add( "Solved Problemmethod TODO in model" );
		return result;
	}

	public List<TrainCoach> getAllTraincoaches()
	{
		return traincoachEJB.getAllTraincoachesNeedReview( currentWorkplace.getId() );
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
