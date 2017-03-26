package controllers;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import bachelorproject.ejb.LiveSensorDataEJB;
import bachelorproject.model.LiveSensorData;

/**
 * 	The controller for live_train_overview.xhtml and live_train.xhtml
 *  <p>
 *  This class allows the operator to view all the live trains and follow 
 *  them in more detail.
 *  @author Anton Danneels
 *  @version 0.0.1
 * */
@Named
@RequestScoped
public class LiveTrainOverviewController
{
	@Inject
	private LiveSensorDataEJB lsdEJB;
	
	private List<LiveSensorData> liveTrains = new ArrayList<LiveSensorData>();
	
	private int currentTrainID = 0;
	private LiveSensorData currentLSD = new LiveSensorData();
	
	@PostConstruct
	public void loadPage()
	{
		liveTrains.clear();
		liveTrains = lsdEJB.getAllActiveSensorObjects();
		
		currentLSD = lsdEJB.findLSDByID( currentTrainID );
	}

	/**
	 * @return the liveTrains
	 */
	public List<LiveSensorData> getLiveTrains()
	{
		return liveTrains;
	}

	/**
	 * @param liveTrains the liveTrains to set
	 */
	public void setLiveTrains( List<LiveSensorData> liveTrains )
	{
		this.liveTrains = liveTrains;
	}

	/**
	 * @return the currentTrainID
	 */
	public int getCurrentTrainID()
	{
		return currentTrainID;
	}

	/**
	 * @param currentTrainID the currentTrainID to set
	 */
	public void setCurrentTrainID( int currentTrainID )
	{
		this.currentTrainID = currentTrainID;
	}

	/**
	 * @return the currentLSD
	 */
	public LiveSensorData getCurrentLSD()
	{
		return currentLSD;
	}

	/**
	 * @param currentLSD the currentLSD to set
	 */
	public void setCurrentLSD( LiveSensorData currentLSD )
	{
		this.currentLSD = currentLSD;
	}
	
}
