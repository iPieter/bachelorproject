package rest;

import java.io.IOException;
import java.util.Date;

import javax.annotation.Priority;
import javax.ejb.EJB;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import bachelorproject.ejb.TokenEJB;
import bachelorproject.model.user.Token;

/**
 * This class allows us to secure the REST API by using tokens to validate a
 * user's request
 * 
 * @author Pieter Delobelle
 * @version 1.0.0
 */
@Secured
@Provider
@Priority( Priorities.AUTHENTICATION )
public class AuthenticationFilter implements ContainerRequestFilter
{

	@EJB
	private TokenEJB tokenEJB;

	/**
	 * Tests if a request has a valid token.
	 * 
	 * @param requestContext
	 *            The request.
	 * @throws NotAuthorizedException
	 * @throws IOException
	 */
	@Override
	public void filter( ContainerRequestContext requestContext ) throws IOException
	{

		// Get the HTTP Authorization header from the request
		String authorizationHeader = requestContext.getHeaderString( HttpHeaders.AUTHORIZATION );

		// Check if the HTTP Authorization header is present and formatted
		// correctly
		if ( authorizationHeader == null || !authorizationHeader.startsWith( "Bearer " ) )
		{
			throw new NotAuthorizedException( "Authorization header must be provided" );
		}

		// Extract the token from the HTTP Authorization header
		String token = authorizationHeader.substring( "Bearer".length() ).trim();

		try
		{

			// Validate the token
			validateToken( token );

		}
		catch ( Exception e )
		{
			requestContext.abortWith( Response.status( Response.Status.UNAUTHORIZED ).build() );
		}
	}

	/**
	 * Validates the token by testing if it exists and is not expired
	 * 
	 * @param token
	 *            The token to be tested
	 * @throws Exception
	 */
	private void validateToken( String token ) throws Exception
	{
		Token t = tokenEJB.findTokenByToken( token );

		if ( t == null || t.getExpires().before( new Date() ) )
		{
			throw new Exception( "Not a known token." );
		}

	}

}