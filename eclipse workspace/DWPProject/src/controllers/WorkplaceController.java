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

	private TrainCoach currentTrainCoach = new TrainCoach();
	private Workplace currentWorkplace = new Workplace();
	private List<User> mechanics = new ArrayList<User>();

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

	public void findWorkplaceByWorkplaceId()
	{
		currentWorkplace = workplaceEJB.findWorkplaceByWorkplaceId( currentWorkplace.getId() );
		System.out.println( "ID:" + currentWorkplace.getId() );
	}

	/**
	 * Returns a List of Issue objects, for a given traincoachId.
	 * Herefore it calls the TraincoachEJB
	 *
	 * @author Matthias De Lange
	 * @version 0.0.1
	 * @param traincoachId
	 * @return List of Issue objects
	 * @exception NullPointerException
	 * @see Issue
	 */
	public List<Issue> findActiveIssuesByTraincoachId( int traincoachId )
	{
		// TODO ERROR CHECKING
		List<Issue> result = new ArrayList<>();
		result.addAll( findInProgressIssuesByTraincoachId( traincoachId ));
		result.addAll( findAssignedIssuesByTraincoachId( traincoachId ));
		return result;
	}
	
	public List<Issue> findInProgressIssuesByTraincoachId( int traincoachId ){
		return issueEJB.findInProgressIssuesByTraincoachId(traincoachId);
	}
	
	public List<Issue> findAssignedIssuesByTraincoachId( int traincoachId ){
		return issueEJB.findAssignedIssuesByTraincoachId(traincoachId);
	}
	
	public List<Issue> findClosedIssuesByTraincoachId( int traincoachId ){
		return issueEJB.findClosedIssuesByTraincoachId(traincoachId);
	}

	public List<Issue> findActiveIssuesByMechanicId( int mechanicId )
	{
		// TODO ERRORCHECKING
		List<Issue> result = new ArrayList();
		result.addAll(findInProgressIssuesByMechanicId(mechanicId));
		result.addAll(findAssignedIssuesByMechanicId(mechanicId));
		return result;
	}
	
	public List<Issue> findInProgressIssuesByMechanicId( int mechanicId ){
		return issueEJB.findInProgressIssuesByMechanicId(mechanicId);
	}
	
	public List<Issue> findAssignedIssuesByMechanicId( int mechanicId ){
		return issueEJB.findAssignedIssuesByMechanicId(mechanicId);
	}
	
	public List<User> findMechanicsOfCurrentWorkplace(){
		return workplaceEJB.findMechanicsByWorkplaceId(currentWorkplace.getId());
	}

	public List<TrainCoach> getAllTraincoaches()
	{
		return traincoachEJB.getAllTraincoaches();
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
