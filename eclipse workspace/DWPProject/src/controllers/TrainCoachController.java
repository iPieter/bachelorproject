package controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.ocpsoft.prettytime.PrettyTime;

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
@SessionScoped
public class TrainCoachController implements Serializable
{
	private static final long serialVersionUID = 4104521573413611708L;
	
	@EJB
	private WorkplaceEJB workplaceEJB;
	@EJB
	private TrainCoachEJB traincoachEJB;
	@EJB
	private ProcessedSensorDataEJB psdEJB;
	@EJB
	private IssueEJB issueEJB;
	@Inject
	private UserService userService;

	private ProcessedSensorData currentSensorData = new ProcessedSensorData();
	private TrainCoach currentTrainCoach = new TrainCoach();
	private Workplace currentWorkplace = new Workplace();
	private List<User> mechanics = new ArrayList<User>();
	private int currentTrainCoachID;
	private int currentpsdID;

	@NotNull
	@Size( min=10, max= 1000, message="De beschrijving van het probleem moet minimaal {min} tekens bevatten en maximaal {max}." )
	private String description = "";
	
	@NotNull
	private String mechanicID = "";

	@NotNull
	private String lon;
	@NotNull
	private String lat;
	/**
	 * 	This method gets called when the page first loads.
	 *  <p>
	 *  It extracts the id parameter from the URL requests and uses this
	 *  to load the correct data from the webpage.
	 * */
	public void loadPage()
	{
		currentTrainCoach = traincoachEJB.findTrainCoachByTraincoachId( currentTrainCoachID );
		if( currentTrainCoach == null )
			currentTrainCoach = new TrainCoach();
		List<Workplace> result = workplaceEJB.findWorkplaceByTraincoachID( currentTrainCoach.getId() );
		if ( result.size() > 0 )
		{
			currentWorkplace = result.get( 0 );
		}
		if( currentpsdID == 0 )
			currentSensorData = psdEJB.getProcessedSensorDataByTrainCoachID( currentTrainCoach.getId() );
		else
			currentSensorData = psdEJB.getProcessedSensorDataByID( currentpsdID );
		if( currentSensorData == null )
			System.out.println( "Failed to locate sensordata" );
		
		mechanics.clear();
		for ( User u : workplaceEJB.findMechanicsByWorkplaceId( currentWorkplace.getId() ) )
			mechanics.add( u );
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
	 * 	Calls into the TrainCoachEJB object to set this TrainCoach as reviewed.
	 *  @return If this method succeeds, it will redirect to index
	 * */
	public String setTrainCoachReviewed()
	{
		if( currentTrainCoach != null)
		{
			traincoachEJB.setTrainCoachReviewed( currentTrainCoach.getId() );
			return "workplace.xhtml?faces-redirect=true&workplace_id=" + currentWorkplace.getId();
		}
		return null;
	}
	
	/**
	 * 	Calls into the IssueEJB to create new issues.
	 * */
	public String createIssue()
	{
		if( description.length() < 10 )
		{
			FacesContext.getCurrentInstance().addMessage("inputDescription",  new FacesMessage("De beschrijving moet minimaal 10 tekens bevatten.", 
					"Het is zeer handig voor de onderhoudstechnicus als hij een gedetailleerde beschrijving van het probleem heeft."));
			return "traincoach.xhtml?faces-redirect=true&id=" + currentTrainCoach.getId() + "&psdid=0";
		}
		currentSensorData = psdEJB.getProcessedSensorDataByTrainCoachID( currentTrainCoach.getId() );
		
		Issue issue = new Issue();
		Date now=new Date();
		
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
		issue.setAssignedTime(now);
		
		//TODO TEST
		System.out.println("Coordinates:"+lat+","+lon);
		System.out.println(Double.parseDouble(lat));
		issue.setGpsLat(Double.parseDouble(lat));
		
		issue.setGpsLon(Double.parseDouble(lon));
		System.out.println("Coordinates:"+lat+","+lon);
		issueEJB.createIssue( issue );
		
		return "traincoach.xhtml?faces-redirect=true&id=" + currentTrainCoach.getId() + "&psdid=0";
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
		List<Issue> result = new ArrayList<Issue>();
		result.addAll(findInProgressIssuesByMechanicId(mechanicId));
		result.addAll(findAssignedIssuesByMechanicId(mechanicId));
		return result;
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
	
	public String sensorDataToPrettyTime()
	{
		PrettyTime pt = new PrettyTime( );
		return pt.format( currentSensorData.getTime() );
	}
	
	/**
	 * 	Test if the current displayed sensor data isn't the most recent one
	 *  @return True if the current displayed sensor data is old, false otherwise
	 * */
	public boolean isOldData()
	{
		return currentpsdID != 0;
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
		
		public int getCurrentTrainCoachID() {
			return currentTrainCoachID;
		}

		public void setCurrentTrainCoachID(int currentTrainCoachID) {
			this.currentTrainCoachID = currentTrainCoachID;
		}

		/**
		 * @return the currentpsdID
		 */
		public int getCurrentpsdID()
		{
			return currentpsdID;
		}

		/**
		 * @param currentpsdID the currentpsdID to set
		 */
		public void setCurrentpsdID( int currentpsdID )
		{
			this.currentpsdID = currentpsdID;
		}

		/**
		 * @return the lon
		 */
		public String getLon() {
			return lon;
		}

		/**
		 * @param lon the lon to set
		 */
		public void setLon(String lon) {
			this.lon = lon;
		}

		/**
		 * @return the lat
		 */
		public String getLat() {
			return lat;
		}

		/**
		 * @param lat the lat to set
		 */
		public void setLat(String lat) {
			this.lat = lat;
		}
}

