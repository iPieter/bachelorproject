package rest;

import java.io.File;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import bachelorproject.ejb.IssueAssetEJB;
import bachelorproject.model.IssueAsset;

/**
 * 	A REST endpoint to fetch issue asset Images.
 *  <p>
 *  In order to minimize loading times, we have opted to load these images via AJAX. 
 *  This REST endpoint allows authenticated users to request these files.
 *  @author Anton Danneels
 *  @see IssueAsset
 * */
@Path( "/issue_asset" )
public class IssueAssetRestService
{
	/** Allows acces to request info */
	@Context
	private UriInfo context;
	
	@Inject
	private IssueAssetEJB issueAssetEJB;
	
	/**
	 * 	Creates an empty IssueAssetRestService object
	 * */
	public IssueAssetRestService()
	{
	}
	
	/**
	 * 	Allows users to GET an image object with a specified issueasset id. 
	 *  <p>
	 *  Exposes a GET path for /issue_asset/{id} to the outside world. If found,
	 *  it will return the image, else it will return a default image.
	 *  @param id The id of the issue asset.
	 * */
	@GET
	@Path( "{id}" )
	@Produces( "image/png" )
	public Response getIssueAsset( @PathParam( "id" ) int id )
	{
		IssueAsset asset = issueAssetEJB.getIssueAssetByID( id );
		
		if( asset == null || asset.getLocation().equals( "" ) )
			return Response.status( Status.NOT_FOUND ).build();
		
		File f = new File( asset.getLocation() );
		return Response.ok( f, MediaType.MEDIA_TYPE_WILDCARD ).build();
	}
}
