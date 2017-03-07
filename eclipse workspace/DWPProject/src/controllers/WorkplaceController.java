package controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import bachelorproject.ejb.IssueEJB;
import bachelorproject.ejb.TrainCoachEJB;
import bachelorproject.ejb.WorkplaceEJB;
import bachelorproject.model.Issue;
import bachelorproject.model.TrainCoach;
import bachelorproject.model.User;
import bachelorproject.model.Workplace;

@Named
@RequestScoped
public class WorkplaceController implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Inject
	private WorkplaceEJB workplaceEJB;
	@Inject
	private TrainCoachEJB traincoachEJB;
	@Inject
	private IssueEJB issueEJB;

	//TODO zijn attr nodig? ==>rechtstreeks in methoden OF via init methode die alles aanroept
	private TrainCoach currentTrainCoach = new TrainCoach();
	private Workplace currentWorkplace = new Workplace();
	private List<User> mechanics = new ArrayList<User>();

	//TODO using attr? or returning list?
	public void findTrainCoachByTraincoachId()
	{
		currentTrainCoach = traincoachEJB.findTrainCoachByTraincoachId( currentTrainCoach.getId() );
		List<Workplace> result = workplaceEJB.findWorkplaceByTraincoachID( currentTrainCoach.getId() );
		if ( result.size() > 0 )
		{
			currentWorkplace = result.get( 0 );
		}
		mechanics.clear();
		for ( User u : workplaceEJB.findMechanicsByWorkplaceId( currentWorkplace.getId() ) )
			mechanics.add( u );
	}

	//TODO using attr? or returning list?
	public void findWorkplaceByWorkplaceId()
	{
		currentWorkplace = workplaceEJB.findWorkplaceByWorkplaceId( currentWorkplace.getId() );
		System.out.println( "ID:" + currentWorkplace.getId() );
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
}
