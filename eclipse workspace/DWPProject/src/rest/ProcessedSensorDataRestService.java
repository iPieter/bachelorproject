package rest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
/*
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
*/
import javax.ws.rs.core.UriInfo;

import bachelorproject.ejb.ProcessedSensorDataEJB;
import bachelorproject.model.sensordata.ProcessedSensorData;

/**
 * 	A REST endpoint to fetch processed sensor data in JSON format.
 *  <p>
 *  Processed sensor data can be quite big. In order to minimize loading
 *  times, we have opted to load these files via AJAX. This REST endpoint
 *  allows authenticated users to request these files.
 *  @author Anton Danneels
 *  @see ProcessedSensorData
 *  @see ProcessedSensorDataEJB
 * */
@Path( "/processed_data" )
public class ProcessedSensorDataRestService
{
	/** Allows acces to request info */
	@Context
	private UriInfo context;

	/** Every request passes through this EJB object which will validate the request.*/
	@Inject
	private ProcessedSensorDataEJB psdEJB;

	/**
	 * 	Creates an empty ProcessedSensorDataRestService object. Needed for JavaEE to recognize this 
	 *  as a REST class.
	 * */
	public ProcessedSensorDataRestService()
	{
	}

	/**
	 * 	Allows users to GET a ProcessedSensorData object with a specified id. 
	 *  <p>
	 *  Exposes a GET path for /processed_data/{id} to the outside world. If found,
	 *  it will return the json file, else it will return an error in json format.
	 *  @param id The id of the file that's needed.
	 * */
	@GET
	@Path( "{id}" )
	@Produces( "text/json" )
	public String getProcessedSensorData( @PathParam( "id" ) int id )
	{
		ProcessedSensorData data = psdEJB.getProcessedSensorDataByID( id );
		
		if( data == null )
		{
			System.out.println( "ProcessedSensorData does not exist:" + id );
			return "{ \"error\" : \"Error loading file.\" }";
		}
		
		try
		{
			BufferedReader reader = new BufferedReader( new FileReader( data.getLocation() ) );
			String file = "";
			String line = "";
			while ( ( line = reader.readLine() ) != null )
				file += line + System.getProperty( "line.separator" );

			reader.close();

			return file;
		}
		catch ( IOException e )
		{
			e.printStackTrace();
			return "{ \"error\" : \"Error loading file.\" }";
		}
	}
}
