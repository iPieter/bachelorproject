package controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import bachelorproject.ejb.IssueEJB;
import bachelorproject.ejb.ProcessedSensorDataEJB;
import bachelorproject.ejb.TrainCoachEJB;
import bachelorproject.ejb.WorkplaceEJB;
import bachelorproject.model.TrainCoach;
import bachelorproject.model.Workplace;
import bachelorproject.model.issue.Issue;
import bachelorproject.model.sensordata.ProcessedSensorData;

/**
 * 	The controller for traincoach_sensor.xhtml
 *  <p>
 *  When visiting the traincoach_sensor page, operators can see 
 *  an overview of past sensordata.
 *  @author Anton Danneels
 *  @version 0.0.1
 *  @see TrainCoach
 *  @see TrainCoachEJB
 *  @see Issue
 *  @see IssueEJB
 * */
@Named
@SessionScoped
public class TrainCoachSensorController implements Serializable
{
	private static final long serialVersionUID = 6547792165686784385L;

	@Inject
	private WorkplaceEJB workplaceEJB;
	@Inject
	private TrainCoachEJB traincoachEJB;
	@Inject
	private ProcessedSensorDataEJB psdEJB;
	
	private int currentTrainCoachID = 0;
	private TrainCoach currentTrainCoach = null;
	private Workplace currentWorkplace = null;
	
	List<ProcessedSensorData> data = new ArrayList<>();
	
	/**
	 * 	Loads the page by retrieving the needed assets from the database.
	 * */
	public void loadPage()
	{
		currentTrainCoach = traincoachEJB.findTrainCoachByTraincoachId( currentTrainCoachID );
		if( currentTrainCoach == null )
			currentTrainCoach = new TrainCoach();
		
		List<Workplace> workplaces = workplaceEJB.findWorkplaceByTraincoachID( currentTrainCoachID );
		if( workplaces.size() > 0 )
			currentWorkplace = workplaces.get( 0 );
		
		data.clear();
		data.addAll( psdEJB.getProcessedSensorDataListByTrainCoachID( currentTrainCoachID ) );
	}

	/**
	 * @return the currentTrainCoachID
	 */
	public int getCurrentTrainCoachID()
	{
		return currentTrainCoachID;
	}

	/**
	 * @param currentTrainCoachID the currentTrainCoachID to set
	 */
	public void setCurrentTrainCoachID( int currentTrainCoachID )
	{
		this.currentTrainCoachID = currentTrainCoachID;
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
	 * @return the data
	 */
	public List<ProcessedSensorData> getData()
	{
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData( List<ProcessedSensorData> data )
	{
		this.data = data;
	}
	
}
