package rest;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

@Path( "/processed_data" )
public class ProcessedSensorDataRestService
{
	@Context
	private UriInfo context;

	public ProcessedSensorDataRestService()
	{
	}

	@GET
	@Produces( "text/json" )
	public String getProcessedSensorData()
	{
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader( System.getProperty( "user.home" ) + "/test03_track16_speed40_radius125.json" ));
			String file = "";
			String line = "";
			while( (line = reader.readLine()) != null )
				file += line + System.getProperty( "line.separator" );
			
			return file;
		}
		catch ( IOException e )
		{
			e.printStackTrace();
			return "{ \"error\" : \"Error loading file.\" }";
		}
	}
}
