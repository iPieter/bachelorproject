package rest;

import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import bachelorproject.ejb.IssueEJB;
import bachelorproject.model.Issue;

@Path( "/workplace" )
public class WorkplaceRestService {

	/** Allows acces to request info */
	@Context
	private UriInfo context;

	/** Every request passes through this EJB object which will validate the request.*/
	@EJB
	private IssueEJB issueEJB;
	
	@GET
	@Path( "map/{id}" )
	@Produces( "text/json" )
	public String getWorkplaceMapData(@PathParam( "id" ) int workplaceId )
	{
		List<Issue> activeIssues = issueEJB.findByWorkplaceId( workplaceId );
		
		String result="";
		
		if(!activeIssues.isEmpty()){
			result=result.concat("{");
			
			//Adding latitude values
			result=result.concat("\"gpsLat\":");
			result=result.concat("[");
			for(int i=0;i<activeIssues.size();i++){
				Issue issue=activeIssues.get(i);
				String val=String.valueOf(issue.getGpsLat());
				
				result=result.concat(val);
				
				if(i<activeIssues.size()-1){
					result=result.concat(",");
				}
			}
			result=result.concat("],");
			
			//Adding longitude values
			result=result.concat("\"gpsLon\":");
			result=result.concat("[");
			for(int i=0;i<activeIssues.size();i++){
				Issue issue=activeIssues.get(i);
				String val=String.valueOf(issue.getGpsLon());
				
				result=result.concat(val);
				
				if(i<activeIssues.size()-1){
					result=result.concat(",");
				}
			}
			result=result.concat("],");
			
			//Adding issue descriptions
			result=result.concat("\"descr\":");
			result=result.concat("[");
			for(int i=0;i<activeIssues.size();i++){
				Issue issue=activeIssues.get(i);
				String val=String.valueOf(issue.getDescr());
				
				result=result.concat("\""+val+"\"");
				
				if(i<activeIssues.size()-1){
					result=result.concat(",");
				}
			}
			result=result.concat("],");
			
			//Adding traincoach name
			result=result.concat("\"traincoach\":");
			result=result.concat("[");
			for(int i=0;i<activeIssues.size();i++){
				Issue issue=activeIssues.get(i);
				String type=String.valueOf(issue.getData().getTraincoach().getType());
				String name=String.valueOf(issue.getData().getTraincoach().getName());
				
				result=result.concat("\""+type+" - "+name+"\"");
				
				if(i<activeIssues.size()-1){
					result=result.concat(",");
				}
			}
			
			result=result.concat("]");
			result=result.concat("}");
		}
		else
		{
			System.out.println("WORKPLACEMAP_DATA: empty query");
			result=null;
		}
		System.out.println("WORKPLACEMAP_DATA: "+result);
		
		return result;
	}
}
