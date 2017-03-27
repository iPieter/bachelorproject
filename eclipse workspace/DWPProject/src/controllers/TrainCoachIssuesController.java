package controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import bachelorproject.ejb.IssueAssetEJB;
import bachelorproject.ejb.IssueEJB;
import bachelorproject.ejb.TrainCoachEJB;
import bachelorproject.ejb.WorkplaceEJB;
import bachelorproject.model.TrainCoach;
import bachelorproject.model.Workplace;
import bachelorproject.model.issue.Issue;
import bachelorproject.model.issue.IssueAsset;
import bachelorproject.services.UserService;

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
	@Inject
	private IssueAssetEJB issueAssetEJB;
	@Inject
	private UserService userService;
	
	private Workplace currentWorkplace = new Workplace();
	private TrainCoach currentTrainCoach = new TrainCoach();
	private List<Issue> currentActiveIssues = new ArrayList<Issue>();
	private List<Issue> currentCompletedIssues = new ArrayList<>();
	private HashMap<Integer,List<IssueAsset>> currentIssueAssets = new HashMap<Integer, List<IssueAsset>>();
	
	private int currentTraincoachID = -1;
	
	private int fieldIssueID = 0;
	private String fieldIssueAssetDescription = "";
	
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
		
		currentIssueAssets.clear();
		
		for( Issue i : currentActiveIssues )
			currentIssueAssets.put( i.getId(), issueAssetEJB.getAllIssueAssetsByIssueID( i.getId() ) );
		for( Issue i : currentCompletedIssues )
			currentIssueAssets.put( i.getId(), issueAssetEJB.getAllIssueAssetsByIssueID( i.getId() ) );
	}
	
	/**
	 * 	Creates a IssueAsset and links it to an issue.
	 *  @return The resulting URL.
	 * */
	public String createIssueAsset()
	{		
		IssueAsset asset = new IssueAsset();
		asset.setDescr( fieldIssueAssetDescription );
		asset.setLocation( "" );
		asset.setTime( new Date() );
		asset.setUser( userService.getUser() );
		
		issueAssetEJB.createIssueAsset( asset );
		issueEJB.addAsset( asset, fieldIssueID );
		
		return "traincoach_issues.xhtml?faces-redirect=true&id=" + currentTrainCoach.getId();
	}
	
	/**
	 * 	Returns a list of an issue's assets.
	 *  <p>
	 *  Looks up the HashMap to find an issue's assets. If found, it will return the list, else and empty one
	 *  @param issueID The ID of the Issue who's assets are needed.
	 *  @return A list with the issue's assets.
	 * */
	public List<IssueAsset> getIssueAssets( int issueID )
	{
		if( !currentIssueAssets.containsKey( issueID ) )
			return new ArrayList<IssueAsset>();
		
		return currentIssueAssets.get( issueID );
	}
	
	/**
	 * 	Closes an issue.
	 *  @param issueID The ID of an issue that needs to be closed.
	 * */
	public String closeIssue( )
	{
		if( issueEJB.closeIssue( fieldIssueID, currentTraincoachID ) )
		{
			System.out.println( "Issue closed." );
			return "index.xhtml?faces-redirect=true";
		}
		return "traincoach_issues.xhmtl?faces-redirect=true&id=" + currentTraincoachID;
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

	/**
	 * @return the currentIssueAssets
	 */
	public HashMap<Integer, List<IssueAsset>> getCurrentIssueAssets()
	{
		return currentIssueAssets;
	}

	/**
	 * @param currentIssueAssets the currentIssueAssets to set
	 */
	public void setCurrentIssueAssets( HashMap<Integer, List<IssueAsset>> currentIssueAssets )
	{
		this.currentIssueAssets = currentIssueAssets;
	}

	/**
	 * @return the fieldIssueID
	 */
	public int getFieldIssueID()
	{
		return fieldIssueID;
	}

	/**
	 * @param fieldIssueID the fieldIssueID to set
	 */
	public void setFieldIssueID( int fieldIssueID )
	{
		this.fieldIssueID = fieldIssueID;
	}

	/**
	 * @return the fieldIssueAssetDescription
	 */
	public String getFieldIssueAssetDescription()
	{
		return fieldIssueAssetDescription;
	}

	/**
	 * @param fieldIssueAssetDescription the fieldIssueAssetDescription to set
	 */
	public void setFieldIssueAssetDescription( String fieldIssueAssetDescription )
	{
		this.fieldIssueAssetDescription = fieldIssueAssetDescription;
	}
	
}
