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
import javax.ws.rs.core.UriInfo;

import be.kuleuven.cs.gent.projectweek.model.ProcessedSensorData;
import be.kuleuven.cs.gent.projectweek.services.InternalDatafetchService;

@Path( "/processed_data" )
public class ProcessedSensorDataRestService
{
	@Context
	private UriInfo context;
	
	@Inject
	private InternalDatafetchService internalDatafetchService;

	public ProcessedSensorDataRestService()
	{
	}

	@GET
	@Path("{id}")
	@Produces( "text/json" )
	public String getProcessedSensorData( @PathParam("id") int id )
	{
		ProcessedSensorData data = internalDatafetchService.getProcessedSensorDataByID( id );
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader( data.getLocation()) );
			String file = "";
			String line = "";
			while( (line = reader.readLine()) != null )
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
