package controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import bachelorproject.ejb.IssueEJB;
import bachelorproject.ejb.TrainCoachEJB;
import bachelorproject.ejb.WorkplaceEJB;
import bachelorproject.model.TrainCoach;
import bachelorproject.model.Workplace;
import bachelorproject.model.issue.Issue;
import bachelorproject.model.user.User;

@Named
@SessionScoped
public class WorkplaceController implements Serializable
{
	private static final long serialVersionUID = -5824382672006285083L;
	
	@EJB
	private WorkplaceEJB workplaceEJB;
	@EJB
	private TrainCoachEJB traincoachEJB;
	@EJB
	private IssueEJB issueEJB;

	private Workplace currentWorkplace = new Workplace();
	private int currentWorkplaceID;
	
	/**
	 * 	This method gets called when the page first loads.
	 *  <p>
	 *  It extracts the id parameter from the URL requests and uses this
	 *  to load the correct data from the webpage.
	 * */
	public void loadPage()
	{
		currentWorkplace = workplaceEJB.findWorkplaceByWorkplaceId( currentWorkplaceID );
	}
	
	/**
	 * 	Returns a list of all the current active TrainCoaches of the currentWorkplace object.
	 * 	@return A List of TrainCoach objects stored in the currentWorkplace.
	 * */
	public List<TrainCoach> findActiveTrainCoaches( )
	{
		return traincoachEJB.getAllTraincoachesNeedReview( currentWorkplace.getId() );
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
	 * Therefore it queries Issues with issueStates IN_PROGRESS or ASSIGNED through the IssueEJB.
	 *
	 * @author Matthias De Lange
	 * @version 0.0.1
	 * @param traincoachId
	 * @return List of Issue objects
	 * @see IssueEJB
	 */
	public List<Issue> findActiveIssuesByTraincoachId( int traincoachId )
	{
		return issueEJB.findActiveIssuesByTraincoachId( traincoachId );
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

	// GETTERS & SETTERS
	public Workplace getCurrentWorkplace()
	{
		return currentWorkplace;
	}

	public List<Workplace> getAllWorkplaces()
	{
		return workplaceEJB.getAllWorkplaces();
	}

	/**
	 * @return the currentWorkplaceID
	 */
	public int getCurrentWorkplaceID() {
		return currentWorkplaceID;
	}

	/**
	 * @param currentWorkplaceID the currentWorkplaceID to set
	 */
	public void setCurrentWorkplaceID(int currentWorkplaceID) {
		this.currentWorkplaceID = currentWorkplaceID;
	}

	/**
	 * @param currentWorkplace the currentWorkplace to set
	 */
	public void setCurrentWorkplace(Workplace currentWorkplace) {
		this.currentWorkplace = currentWorkplace;
	}
}
