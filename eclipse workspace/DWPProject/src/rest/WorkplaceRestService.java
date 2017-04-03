package rest;

import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import bachelorproject.ejb.IssueEJB;
import bachelorproject.ejb.WorkplaceEJB;
import bachelorproject.model.Issue;
import bachelorproject.model.TrainCoach;
import bachelorproject.model.Workplace;

@Path( "/workplace" )
public class WorkplaceRestService {

	/** Allows acces to request info */
	@Context
	private UriInfo context;

	/** Every request passes through this EJB object which will validate the request.*/
	@EJB
	private IssueEJB issueEJB;
	
	@EJB
	private WorkplaceEJB workplaceEJB;
	
	@GET
	@Path( "{id}/map" )
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
	
	/**
	 * Returns a JSON response for a specific workplace id. 
	 * 
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @param id Id for the workplace to return
	 * @return JSON response with the workplace of provided id
	 */
	@GET
	@Path( "{id}" )
	@Produces( "text/json" )
	public Response getByWorkplaceId( @PathParam( "id" ) int id )
	{
		Workplace w = workplaceEJB.findWorkplaceByWorkplaceId(id);
		
		if( w == null )
			return Response.status( Status.NOT_FOUND ).build();
		
		return Response.ok( w ).build();
	}
	

	@GET
	@Path( "all" )
	@Produces( "text/json" )
	public Response getAllWorkplaces( )
	{
		List<Workplace> workplaceList = workplaceEJB.getAllWorkplaces();
		return Response.ok( workplaceList ).build();
	}
}
