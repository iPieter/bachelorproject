package rest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.FormParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import bachelorproject.ejb.TokenEJB;
import bachelorproject.model.user.Token;
import bachelorproject.services.UserService;

/**
 * REST endpoint for logging in the user.
 * 
 * Inspired by
 * <a href="http://stackoverflow.com/questions/26777083/best-practice-for-rest-token-based-authentication-with-jax-rs-and-jersey">
 * this StackOverflow response</a>.
 * 
 * @author Pieter Delobelle
 * @version 1.0.0
 */
@Path("/login")
public class LoginRestService
{
	
	@EJB
	private TokenEJB tokenEJB;

	@Inject
	private UserService userService;
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response authenticateUser(@FormParam("email") String email, @FormParam("password") String password)
	{

		try
		{

			// Authenticate the user using the credentials provided
			authenticate(email, password);

			// Issue a token for the user
			Token token = UserService.issueToken(userService.getUser());
			
			tokenEJB.createToken(token);
			
			// Return the token on the response
			return Response.ok(token).build();

		} catch (Exception e)
		{
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}

	private void authenticate(String email, String password) throws Exception
	{
		if (!userService.verificateLogin(email, password)) 
		{
			throw new Exception("Login not mached.");
		}
	}

	
}