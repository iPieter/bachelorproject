package rest;

import java.util.Date;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import bachelorproject.ejb.LiveSensorDataEJB;
import bachelorproject.ejb.LiveSensorDataEntryEJB;
import bachelorproject.ejb.TrainCoachEJB;
import bachelorproject.model.LiveSensorData;
import bachelorproject.model.LiveSensorDataEntry;
import bachelorproject.model.TrainCoach;

/**
 * 	A REST endpoint to allow the sensors to communicate with the server.
 *  <p>
 *  This class defines a REST api for sensors. The sensor can create a new
 *  LiveSensorData object, add entries to it and close it. If this happens,
 *  
 *  @author Anton Danneels
 *  @see LiveSensorData
 *  @see LiveSensorDataEntry
 *  @see LiveSensorDataEJB
 *  @see LiveSensorDataEntryEJB
 * */
@Path( "/live_data" )
public class LiveSensorDataRestService
{
	@Inject
	private TrainCoachEJB traincoachEJB;
	
	@Inject
	private LiveSensorDataEJB lsdEJB;
	
	@Inject
	private LiveSensorDataEntryEJB lsdeEJB;
	
	/**
	 * 	Creates a new LiveSensorData object and allows the sensor to add entries
	 * 
	 * */
	@GET
	@Path( "register/{traincoachID}/{track}" )
	public Response startLiveTracking( @PathParam("traincoachID") int traincoachID, @PathParam("track") String track )
	{
		TrainCoach trainCoach = traincoachEJB.findTrainCoachByTraincoachId( traincoachID );
		
		if( trainCoach == null )
			return Response.status( Status.BAD_REQUEST ).build();
		
		Date now = new Date();
		LiveSensorData lsd = new LiveSensorData();
		lsd.setLive( true );
		lsd.setTrack( track );
		lsd.setTraincoach( trainCoach );
		
		lsdEJB.createLiveSensorData( lsd );
		
		LiveSensorData lsdCreated = lsdEJB.findLSDByDate( now );
		
		return Response.ok( "" ).build();
	}
	
	/**
	 * 	Adds an entry to the specified 
	 * */
	public void addEntry()
	{
		
	}
	
	public void stopLiveTracking()
	{
		
	}
}
