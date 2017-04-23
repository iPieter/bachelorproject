package rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.fasterxml.jackson.databind.ObjectMapper;

import bachelorproject.ejb.IssueEJB;
import bachelorproject.model.issue.Issue;
import bachelorproject.model.user.UserRole;;

/**
 * 	A REST endpoint to fetch issues.
 *  <p>
 *  @author Anton Danneels
 *  @see Issue
 * */
@Path( "/issues" )
@Secured({UserRole.MECHANIC, UserRole.OPERATOR})
public class IssueRestService
{
	@Inject
	private IssueEJB issueEJB;
	
	/**
	 * 	Retrieves a specific Issue object
	 * */
	@GET
	@Path( "{id}" )
	@Produces( "text/json" )
	public Response getIssueByID( @PathParam( "id" ) int id )
	{
		Issue asset = issueEJB.findByID( id );
		
		if( asset == null )
			return Response.status( Status.NOT_FOUND ).build();
		
		return Response.ok( asset ).build();
	}
	
	/**
	 * 	Retrieves a list of active Issue objects for the user
	 *  @param userID The ID of a userID.
	 * */
	@GET
	@Path( "all_for_user/{userid}" )
	@Produces( "text/json" )
	public Response getActiveIssueList( @PathParam( "userid" ) int userID )
	{
		List<Issue> assignedIssues = issueEJB.findAssignedIssuesByMechanicId( userID );
		List<Issue> inProgressIssues = issueEJB.findInProgressIssuesByMechanicId( userID );
		assignedIssues.addAll( inProgressIssues );
		
		return Response.ok( assignedIssues ).build();
	}
	
	/**
	 * 	Retrieves a list of active Issue objects for the target TrainCoach
	 *  @param traincoachID The ID of the TrainCoach
	 * */
	@GET
	@Path( "get_by_traincoach_id/{traincoachID}" )
	@Produces( "text/json" )
	public Response getIssuesByTraincoachID( @PathParam( "traincoachID" ) int traincoachID )
	{	
		List<Issue> issues = issueEJB.findAssignedIssuesByTraincoachId( traincoachID );
		issues.addAll( issueEJB.findInProgressIssuesByTraincoachId( traincoachID ) );
		
		System.out.println( issues );
		
		return Response.ok( issues ).build();
	}
	
}
