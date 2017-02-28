package be.kuleuven.cs.gent.projectweek.services;

import javax.enterprise.context.ConversationScoped;

import be.kuleuven.cs.gent.projectweek.model.User;

import java.io.Serializable;

@ConversationScoped
public class AuthenticationService implements Serializable
{

	/*
	 * The AuthenticationService is to provide:
	 * - Login with credential checks
	 * - Logout
	 * - Verifying the permissions of the user for the current page
	 * 
	 * Most of these information is provided by the UserService for the
	 * duration of the session. A login will be handled by this service
	 * and produces a User object.
	 */
	
	private static final long serialVersionUID = 3713738638802589887L;

	public boolean login(String email, String password)
	{
		
		return false;
	}
}
