package rest;

import java.util.Map;
import java.util.TreeMap;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import bachelorproject.ejb.IssueEJB;
import bachelorproject.ejb.TokenEJB;
import bachelorproject.model.issue.IssueStatus;
import bachelorproject.model.user.UserRole;
import bachelorproject.services.UserService;

@Path( "/statistics_data" )
@Secured(
{ UserRole.MECHANIC, UserRole.OPERATOR } )
public class StatisticsRestService
{

	/** Allows access to request info */
	@Context
	private UriInfo context;

	/**
	 * Every request passes through this EJB object which will validate the
	 * request.
	 */
	@EJB
	private IssueEJB issueEJB;

	@EJB
	private TokenEJB tokenEJB;

	/**
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @param id
	 * @return A json file with the count of each issue.
	 */
	@GET
	@Produces( "text/json" )
	public Response getDonutData( @HeaderParam( "Authorization" ) String authorizationHeader )
	{
		String token = authorizationHeader.substring( "Bearer".length() ).trim();
		int id = tokenEJB.findTokenByToken( token ).getOwner().getId();
		System.out.println( token );
		Map<String, Integer> results = new TreeMap<>();

		final int DAYS_BACK_FROM_NOW = 30;

		for ( IssueStatus is : IssueStatus.values() )
		{
			int count = issueEJB.countOperatorIssues( id, is, DAYS_BACK_FROM_NOW );
			results.put( is.getDescr(), count );
		}

		return Response.ok( results ).build();
	}
}
