package controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import bachelorproject.ejb.IssueEJB;
import bachelorproject.ejb.TrainCoachEJB;
import bachelorproject.ejb.WorkplaceEJB;
import bachelorproject.model.Issue;
import bachelorproject.model.TrainCoach;
import bachelorproject.model.Workplace;

/**
 * 	The controller for traincoach_issue.xhtml
 *  <p>
 *  This class is responsible for rendering all current and previous
 *  issues of a traincoach. It allows operators to track progress of
 *  active issues and close them.
 *  @author Anton Danneels
 *  @version 0.0.1
 *  @see TrainCoach
 *  @see TrainCoachEJB
 *  @see Issue
 *  @see IssueEJB
 * */
@Named
@SessionScoped
public class TrainCoachIssuesController implements Serializable
{
	/**
	 *  Allows this class to be Serializable.
	 */
	private static final long serialVersionUID = -8774145827987221620L;

	@Inject
	private WorkplaceEJB workplaceEJB;
	@Inject
	private TrainCoachEJB traincoachEJB;
	@Inject
	private IssueEJB issueEJB;
	
	private Workplace currentWorkplace = new Workplace();
	private TrainCoach currentTrainCoach = new TrainCoach();
	private List<Issue> currentActiveIssues = new ArrayList<Issue>();
	private List<Issue> currentCompletedIssues = new ArrayList<>();
	
	private int currentTraincoachID = -1;
	
	/**
	 * 	This method gets called when the page first loads.
	 *  <p>
	 *  It extracts the id parameter from the URL requests and uses this
	 *  to load the correct data from the webpage.
	 * */
	public void loadPage()
	{
		currentTrainCoach = traincoachEJB.findTrainCoachByTraincoachId( currentTraincoachID );
	
		//TODO:: proper check
		currentWorkplace = workplaceEJB.findWorkplaceByTraincoachID( currentTraincoachID ).get( 0 );
		
		currentActiveIssues.clear();
		currentCompletedIssues.clear();
		
		currentActiveIssues.addAll( issueEJB.findAssignedIssuesByTraincoachId( currentTraincoachID ) );
		currentActiveIssues.addAll( issueEJB.findInProgressIssuesByTraincoachId( currentTraincoachID ) );
		
		currentCompletedIssues.addAll( issueEJB.findClosedIssuesByTraincoachId( currentTraincoachID ) );
	}

	/**
	 * @return the currentTrainCoach
	 */
	public TrainCoach getCurrentTrainCoach()
	{
		return currentTrainCoach;
	}

	/**
	 * @param currentTrainCoach the currentTrainCoach to set
	 */
	public void setCurrentTrainCoach( TrainCoach currentTrainCoach )
	{
		this.currentTrainCoach = currentTrainCoach;
	}

	/**
	 * @return the currentActiveIssues
	 */
	public List<Issue> getCurrentActiveIssues()
	{
		return currentActiveIssues;
	}

	/**
	 * @param currentActiveIssues the currentActiveIssues to set
	 */
	public void setCurrentActiveIssues( List<Issue> currentActiveIssues )
	{
		this.currentActiveIssues = currentActiveIssues;
	}

	/**
	 * @return the currentCompletedIssues
	 */
	public List<Issue> getCurrentCompletedIssues()
	{
		return currentCompletedIssues;
	}

	/**
	 * @param currentCompletedIssues the currentCompletedIssues to set
	 */
	public void setCurrentCompletedIssues( List<Issue> currentCompletedIssues )
	{
		this.currentCompletedIssues = currentCompletedIssues;
	}

	/**
	 * @return the currentTraincoachID
	 */
	public int getCurrentTraincoachID()
	{
		return currentTraincoachID;
	}

	/**
	 * @param currentTraincoachID the currentTraincoachID to set
	 */
	public void setCurrentTraincoachID( int currentTraincoachID )
	{
		this.currentTraincoachID = currentTraincoachID;
	}

	/**
	 * @return the currentWorkplace
	 */
	public Workplace getCurrentWorkplace()
	{
		return currentWorkplace;
	}

	/**
	 * @param currentWorkplace the currentWorkplace to set
	 */
	public void setCurrentWorkplace( Workplace currentWorkplace )
	{
		this.currentWorkplace = currentWorkplace;
	}
}
