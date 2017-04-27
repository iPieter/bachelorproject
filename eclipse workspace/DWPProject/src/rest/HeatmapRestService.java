package rest;

import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import bachelorproject.ejb.IssueEJB;
import bachelorproject.model.issue.Issue;
import bachelorproject.model.user.UserRole;

/**
 * Provides a REST endpoint for the issue heatmap
 */
@Path( "/heatmap_data" )
@Secured(
{ UserRole.MECHANIC, UserRole.OPERATOR } )
public class HeatmapRestService
{

	/** Allows acces to request info */
	@Context
	private UriInfo context;

	/**
	 * Every request passes through this EJB object which will validate the
	 * request.
	 */
	@EJB
	private IssueEJB issueEJB;

	/**
	 * Returns a JSON file with the positional data of the issues.
	 */
	@GET
	@Produces( "text/json" )
	public String getHeatmapData()
	{
		List<Issue> activeIssues = issueEJB.findAllActiveIssues();
		String result = "";

		if ( !activeIssues.isEmpty() )
		{
			result = result.concat( "{" );

			// Adding latitude values
			result = result.concat( "\"gpsLat\":" );
			result = result.concat( "[" );
			for ( int i = 0; i < activeIssues.size(); i++ )
			{
				Issue issue = activeIssues.get( i );
				String val = String.valueOf( issue.getGpsLat() );

				result = result.concat( val );

				if ( i < activeIssues.size() - 1 )
				{
					result = result.concat( "," );
				}
			}
			result = result.concat( "]," );

			// Adding longitude values
			result = result.concat( "\"gpsLon\":" );
			result = result.concat( "[" );
			for ( int i = 0; i < activeIssues.size(); i++ )
			{
				Issue issue = activeIssues.get( i );
				String val = String.valueOf( issue.getGpsLon() );

				result = result.concat( val );

				if ( i < activeIssues.size() - 1 )
				{
					result = result.concat( "," );
				}
			}
			result = result.concat( "]" );
			result = result.concat( "}" );
		}
		else
		{
			System.out.println( "HEATMAP_DATA: empty query" );
			result = null;
		}
		System.out.println( "HEATMAP_DATA: " + result );

		return result;
	}
}
