package rest;


import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import bachelorproject.ejb.IssueEJB;
import bachelorproject.model.IssueStatus;
import bachelorproject.services.UserService;

@Path( "/statistics_data" )
public class StatisticsRestService {
	
	/** Allows acces to request info */
	@Context
	private UriInfo context;

	/** Every request passes through this EJB object which will validate the request.*/
	@EJB
	private IssueEJB issueEJB;
	
	@Inject
	private UserService userService;
	
	@GET
	@Produces( "text/json" )
	public String getDonutData()
	{
		String result="";
		int id=userService.getUser().getId();
		
		int closedIssueCount=issueEJB.countOperatorIssues(id, IssueStatus.CLOSED, 30);
		int assignedIssueCount=issueEJB.countOperatorIssues(id, IssueStatus.ASSIGNED, 30);
		int inProgressIssueCount=issueEJB.countOperatorIssues(id, IssueStatus.IN_PROGRESS, 30);
		
		result=result.concat(
				"{"
				+ "\"issue_counts\":"
				+ "["
				+"[\"Afgewerkt\","+closedIssueCount+"],"
				+"[\"Toegewezen\","+assignedIssueCount+"],"
				+"[\"In Behandeling\","+inProgressIssueCount+"]"
				+ "]"
				+"}");
		
		System.out.println("DONUT_DATA: "+result);
		
		return result;
	}
}
