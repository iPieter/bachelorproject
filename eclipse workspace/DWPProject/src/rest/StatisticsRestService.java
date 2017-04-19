package rest;


import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import bachelorproject.ejb.IssueEJB;
import bachelorproject.model.issue.IssueStatus;
import bachelorproject.model.user.UserRole;
import bachelorproject.services.UserService;

@Path( "/statistics_data" )
@Secured({UserRole.MECHANIC, UserRole.OPERATOR})
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
		
		final int DAYS_BACK_FROM_NOW = 30;
		int closedIssueCount=issueEJB.countOperatorIssues(id, IssueStatus.CLOSED, DAYS_BACK_FROM_NOW);
		int assignedIssueCount=issueEJB.countOperatorIssues(id, IssueStatus.ASSIGNED, DAYS_BACK_FROM_NOW);
		int inProgressIssueCount=issueEJB.countOperatorIssues(id, IssueStatus.IN_PROGRESS, DAYS_BACK_FROM_NOW);
		
		result=result.concat(
				"{"
				+ "\"issue_counts\":"
				+ "["
				+"[\"Afgewerkte Problemen\","+closedIssueCount+"],"
				+"[\"Toegewezen Problemen\","+assignedIssueCount+"],"
				+"[\"Problemen In Behandeling\","+inProgressIssueCount+"]"
				+ "]"
				+"}");
		
		System.out.println("DONUT_DATA: "+result);
		System.out.println("DONUT_DATA: USER_ID="+id);
		
		return result;
	}
}
