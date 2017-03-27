package rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import bachelorproject.ejb.IssueEJB;
import bachelorproject.model.Issue;

/**
 * 	A REST endpoint to fetch issues.
 *  <p>
 *  @author Anton Danneels
 *  @see Issue
 * */
@Path( "/issues" )
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
}
