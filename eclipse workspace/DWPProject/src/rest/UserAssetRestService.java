/**
 * A REST service for user images.
 */
package rest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
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
import javax.ws.rs.core.UriInfo;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import bachelorproject.ejb.UserEJB;
import bachelorproject.model.user.User;
import bachelorproject.services.UserService;

/**
 * Uploading and downloading of user avatars.
 * 
 * @author Pieter Delobelle
 * @version 1.0.0
 */
@Path( "/assets/user" )
public class UserAssetRestService
{
	@Context
	private UriInfo context;
	
	@EJB
	private UserEJB userEJB;
	
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
	 * @return An html response.
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
	
	/**
	 * 	Allows the users to POST an user avatar via a multipart data form.
	 * 
	 *  @author Pieter Delobelle and Anton Danneels
	 *  @param input The uploaded user avatar
	 *  @return an html response
	 * */
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response addIssueAsset( MultipartFormDataInput input )
	{		
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        System.out.println(uploadForm);
        List<InputPart> inputParts = uploadForm.get("password");
        System.out.println(inputParts);
        List<InputPart> inputParts2 = uploadForm.get("email");
        System.out.println(inputParts2);
        List<InputPart> inputParts3 = uploadForm.get("image");
 
        for (InputPart inputPart : inputParts3)
        {
            try
            {
				System.out.println(uploadForm.get( uploadForm.get("email").get( 0 ).getBodyAsString()));

				User user = userEJB.findUserByEmail(uploadForm.get( "email" ).get( 0 ).getBodyAsString());
				System.out.println(user);
				if( user != null )
				{
					@SuppressWarnings("unused")
	                MultivaluedMap<String, String> header = inputPart.getHeaders();
	                 
	                InputStream inputStream = inputPart.getBody(InputStream.class, null);
	               
	                //Generate a 8 char long identifier for the image.
	                //The reason I don't use id's, is because this makes them non-guessable, as
	                //opposed to id's. Changes for collisions are 64^8 = 218 * 10^12
	                
	                String identifier = Base64.getEncoder().encodeToString(UserService.salt(10)).substring(0, 8);
	                
	                String path = System.getProperty( "user.home" ) + "/project_televic/user_assets/" + identifier + ".png";
	                
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
	                
					//Persist to database
					user.setImageHash(identifier);
					userEJB.updateUser(user);
					
					return Response.temporaryRedirect(new URI("../change_account.xhtml")).build();
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
