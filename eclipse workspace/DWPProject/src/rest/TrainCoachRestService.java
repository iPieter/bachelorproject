package rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import bachelorproject.ejb.TrainCoachEJB;
import bachelorproject.model.Issue;
import bachelorproject.model.TrainCoach;

/**
 * 	A REST endpoint to fetch traincoaches.
 *  <p>
 *  @author Anton Danneels
 *  @see Issue
 * */
@Path( "/traincoach" )
public class TrainCoachRestService
{
	@Inject
	private TrainCoachEJB traincoachEJB;
	
	/**
	 * 	Retrieves a specific TrainCoach object
	 * */
	@GET
	@Path( "{id}" )
	@Produces( "text/json" )
	public Response getByTrainCoachID( @PathParam( "id" ) int id )
	{
		TrainCoach traincoach = traincoachEJB.findTrainCoachByTraincoachId( id );
		
		if( traincoach == null )
			return Response.status( Status.NOT_FOUND ).build();
		
		return Response.ok( traincoach ).build();
	}
	
}
