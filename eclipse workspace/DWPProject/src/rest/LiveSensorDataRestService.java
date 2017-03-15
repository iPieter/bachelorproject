package rest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
	 *  @param traincoachID The associated traincoach
	 *  @param track The track of the ride.
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
		lsd.setDate( now );
		
		int id = lsdEJB.createLiveSensorData( lsd );
		
		return Response.ok( id ).build();
	}
	
	/**
	 * 	Adds an entry to the specified LiveSensorData object
	 *  
	 * */
	@POST
	@Path( "add_entry/{lsdID}/{lat}/{lng}/{speed}/{accel}/{yaw}/{roll}" )
	public Response addEntry( @PathParam("lsdID") int lsdID, @PathParam("lat") double lat, @PathParam("lng") double lng, 
							  @PathParam("speed") double speed, @PathParam("accel") double accel,
							  @PathParam("yaw") double yaw, @PathParam("roll") double roll )
	{
		LiveSensorDataEntry lsde = new LiveSensorDataEntry();
		lsde.setLat( lat );
		lsde.setLng( lng );
		lsde.setSpeed( speed );
		lsde.setAccel( accel );
		lsde.setYaw( yaw );
		lsde.setRoll( roll );
		lsde.setTime( new Date() );
		
		if( !lsdeEJB.addEntry( lsdID, lsde ) )
			return Response.status( Status.BAD_REQUEST ).build();
			
		return Response.ok().build();
	}
	
	/**
	 * 	Returns all LiveSensorDataEntry objects after specified date
	 * 	@param lsdID The ID of the associated LiveSensorData object
	 *  @param from The data from which to retrieve the entries
	 * */
	@GET
	@Path( "get/{lsdID}/{date}" )
	@Produces( "text/json" )
	public Response getSensorDataEntries( @PathParam("lsdID") int lsdID, @PathParam("date") String from )
	{
		SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd_HH-mm-ss" );
		Date date = new Date();
		try
		{
			date = format.parse( from );
		}
		catch ( ParseException e )
		{
			e.printStackTrace();
		}
		List<LiveSensorDataEntry> entries = lsdeEJB.getAllEntriesAfterDate( lsdID, date );
	
		String newLine = System.getProperty( "line.separator" );
		String out = "{ " + newLine;
		out += "\"data\": [" + newLine;
		int i = 0;
		for( LiveSensorDataEntry lsde : entries )
		{
			out += "{" + newLine;
			out += "\"lat\":" + lsde.getLat() + "," + newLine;
			out += "\"lng\":" + lsde.getLng() + "," + newLine;
			out += "\"speed\":" + lsde.getSpeed() + "," + newLine;
			out += "\"accel\":" + lsde.getAccel() + "," + newLine;
			out += "\"yaw\":" + lsde.getYaw() + "," + newLine;
			out += "\"roll\":" + lsde.getRoll() + "," + newLine;
			out += "\"time\": \"" + format.format( lsde.getTime() ) + "\"" + newLine;
			
			if( i != entries.size() -1 )
				out += "}," + newLine;
			else
				out += "}" + newLine;
			i++;
		}
		out += "]" + newLine;
		out += "}" + newLine;
		
		return Response.ok( out ).build();
	}
	
	/**
	 * 	Closes the {@link LiveSensorData} object.
	 *  @param lsdID The {@link LiveSensorData} to be closed.
	 * */
	@GET
	@Path( "/stop/{lsdID}" )
	public void stopLiveTracking( @PathParam("lsdID") int lsdID )
	{
		lsdEJB.closeLiveSensorData( lsdID );
	}
}
