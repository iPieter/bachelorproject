package rest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import bachelorproject.ejb.IssueAssetEJB;
import bachelorproject.ejb.IssueEJB;
import bachelorproject.ejb.UserEJB;
import bachelorproject.model.issue.Issue;
import bachelorproject.model.issue.IssueAsset;

/**
 * 	A REST endpoint to fetch issue asset Images.
 *  <p>
 *  In order to minimize loading times, we have opted to load these images via AJAX. 
 *  This REST endpoint allows authenticated users to request these files.
 *  @author Anton Danneels
 *  @see IssueAsset
 * */
@Path( "/assets/issue" )
public class IssueAssetRestService
{
	/** Allows access to request info */
	@Context
	private UriInfo context;
	
	@Inject
	private IssueAssetEJB issueAssetEJB;
	@Inject
	private UserEJB userEJB;
	@Inject
	private IssueEJB issueEJB;
	
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
	
	/**
	 * 	Returns the meta data of an asset.
	 *  @param id The ID of the issue asset to fetch
	 * */
	@GET
	@Path( "data/{id}" )
	@Produces( "text/json" )
	public Response getIssueAssetData( @PathParam( "id" ) int id )
	{
		IssueAsset asset = issueAssetEJB.getIssueAssetByID( id );
		if( asset == null )
			return Response.status( Status.NOT_FOUND ).build();
		
		return Response.ok( asset ).build();
	}
	
	/**
	 * 	Returns the meta data of an asset.
	 *  @param id The ID of the issue who's assets to fetch
	 * */
	@GET
	@Path( "data_by_issueID/{id}" )
	@Produces( "text/json" )
	public Response getIssueAssetDataByIssueID( @PathParam( "id" ) int id )
	{
		Issue issue = issueEJB.findByID( id );
		
		if( issue == null )
			return Response.status( Status.NOT_FOUND ).build();
		
		return Response.ok( issue.getAssets() ).build();
	}
	
	/**
	 * 	Returns the meta data of an asset.
	 *  @param id The ID of the issue who's assets to fetch
	 * */
	@GET
	@Path( "all" )
	@Produces( "text/json" )
	public Response getAllAssets( )
	{
		List<IssueAsset> issues = issueAssetEJB.getAll();		
				
		if( issues == null )
			return Response.status( Status.NOT_FOUND ).build();
		
		return Response.ok( issues ).build();
	}
	
	/**
	 * 	Allows the users to POST an issue asset via a multipart form
	 *  @param input The uploaded form.
	 *  @return a Response
	 * */
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response addIssueAsset( MultipartFormDataInput input )
	{		
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        List<InputPart> inputParts = uploadForm.get("file");
        
        if( inputParts == null )
        	return Response.status( Status.BAD_REQUEST ).build();
        
        for (InputPart inputPart : inputParts)
        {
            try
            {
            	String descr = uploadForm.get( "desc" ).get( 0 ).getBodyAsString();
				int userID = Integer.valueOf( uploadForm.get( "userID" ).get( 0 ).getBodyAsString() );
				int issueID = Integer.valueOf( uploadForm.get( "issueID" ).get( 0 ).getBodyAsString() );
				
				Issue issue = issueEJB.findByID( issueID );
				
				if( issue != null && issue.getMechanic().getId() == userID )
				{
					@SuppressWarnings("unused")
	                MultivaluedMap<String, String> header = inputPart.getHeaders();
	                 
	                InputStream inputStream = inputPart.getBody(InputStream.class, null);
	               
	                SimpleDateFormat format = new SimpleDateFormat( "yyyy_mm_dd_hh_mm_ss" );
	                Date now = new Date();
	                String path = System.getProperty( "user.home" ) + "/project_televic/issue_assets/" + format.format( now ) + "_" + userID + ".png";
	                
	                boolean hasFile = false;
	                byte[] buffer = new byte[4096];
					int n;
					n = inputStream.read( buffer );
					
					if( n != -1 )
					{
		                FileOutputStream outputStream = new FileOutputStream( path );
		                outputStream.write( buffer,0,n );
		                
						while ((n = inputStream.read(buffer)) > 0)
							outputStream.write(buffer, 0, n);
						
						outputStream.close();
						hasFile = true;
					}
	                
					IssueAsset asset = new IssueAsset();
					asset.setDescr( descr );
					asset.setTime( now );
					asset.setUser( userEJB.findUserById( userID ) );
					if( hasFile )
						asset.setLocation( path );
					else
						asset.setLocation( "" );
					
					issueAssetEJB.createIssueAsset( asset );
					issueEJB.addAsset( asset, issueID );
					
					return Response.status( Response.Status.OK ).build();
				}
				else
					return Response.status( Response.Status.BAD_REQUEST ).build();
            } 
            catch (Exception e) 
            {
                e.printStackTrace();
                return Response.status( Response.Status.BAD_REQUEST ).build();
            }
        }	
        return Response.status( Response.Status.BAD_REQUEST ).build();
	}
}
