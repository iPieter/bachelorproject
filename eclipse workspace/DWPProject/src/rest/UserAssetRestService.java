/**
 * A REST service for user images.
 */
package rest;

import java.io.File;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import bachelorproject.model.Issue;

/**
 * @author Pieter Delobelle
 * @version 1.0.0
 */
@Path( "/assets/user" )
public class UserAssetRestService
{
	@Context
	private UriInfo context;
	

	
	public UserAssetRestService()
	{
		
	}
	
	/**
	 * Returns a image when provided with the correct resource name, likely provided by
	 * the caller of the REST service. If the resource name ables the function to find
	 * a file (.png) with the same name, it is returned. 
	 * 
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @param resource The resource name that's provided by the function caller.
	 * @return
	 */
	@GET
	@Path( "{resource}" )
	@Produces( "image/png" )
	public Response getIssueAsset( @PathParam( "resource" ) String resource )
	{
		File f = new File( System.getProperty( "user.home" ) + "/project_televic/user_assets/" + resource + ".png" );
		
		System.out.println("getting " + f.getAbsolutePath());
		
		//check if the file excists, otherwise try a jpg
		if (!f.exists())
		{
			f = new File( System.getProperty( "user.home" ) + "/project_televic/user_assets/" + resource + ".jpg" );	
		}
		
		return Response.ok( f, MediaType.MEDIA_TYPE_WILDCARD ).build();
	}
}
