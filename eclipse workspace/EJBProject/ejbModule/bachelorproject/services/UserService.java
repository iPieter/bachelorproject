package bachelorproject.services;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
//import javax.faces.bean.SessionScoped;
import javax.enterprise.context.SessionScoped;

import bachelorproject.ejb.UserEJB;
import bachelorproject.model.User;
import bachelorproject.model.UserRole;

@SessionScoped
public class UserService implements Serializable
{
	/*
	 * The purpose of the UserService is to provide a User object for the
	 * duration of the session. The service will also provide:
	 * 
	 * - Login with credential checks - Logout - Verifying the permissions of
	 * the user for the current page
	 * 
	 * When initialized, there'll be no User associated with this service, but
	 * after login it'll be provided by the AuthenticationService.
	 * 
	 */
	private static final long serialVersionUID = -6239216717833742873L;

	public static final String HASHING_METHOD = "SHA-256";

	@EJB
	private UserEJB userEJB;

	private User user;

	@PostConstruct
	public void init()
	{

		// TODO: generate a bunch of users
		if ( userEJB.findAllUsers().size() == 0 )
		{
			for ( int i = 0; i < 10; i++ )
			{
				User u = new User();

				u.setName( "John Doe" );
				u.setEmail( "john" + (int) ( Math.random() * 100 ) + "@test.be" );
				u.setLastLogin( new Date() );
				u.setSalt( salt( User.SALT_LENGTH ) );
				try
				{
					u.setPass( generateHash( "password123", u.getSalt() ) );
				}
				catch ( Exception e )
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				u.setRole( Math.random() > 0.75 ? UserRole.OPERATOR : UserRole.MECHANIC );

				userEJB.createUser( u );

				System.out.println( "Saved user ..." + u.getEmail() );
			}

		}

	}

	/*
	 * If the user that's currently logged in has the right UserRole, this
	 * function will return true. False otherwise. If no user is logged in, this
	 * function will also return false.
	 */
	public boolean hasCurrentUserRequiredRole( UserRole ur )
	{
		if ( this.user == null )
		{
			return false;
		}

		return this.user.getRole().equals( ur );

	}

	/*
	 * The verification of a login consists of two steps: 1. finding the
	 * appropriate user object 2. generating the digest based on password and
	 * salt
	 * 
	 * Note that timing attacks are still an issue in this implementation.
	 */
	public boolean verificateLogin( String email, String password )
	{
		User u = userEJB.findUserByEmail( email );

		if ( u != null )
		{
			try
			{
				if ( Arrays.equals( u.getPass(), ( generateHash( password, u.getSalt() ) ) ) )
				{
					this.user = u;
					return true;

				}
				else
				{

					return false;
				}

			}
			catch ( UnsupportedEncodingException e )
			{
				// TODO Auto-generated catch block
				e.printStackTrace();

				return false;
			}
			catch ( NoSuchAlgorithmException e )
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return false;
	}

	/*
	 * Simple method to remove the user object, if there's one. This will result
	 * in a logout.
	 */
	public void tryLogout()
	{
		this.user = null;
	}

	/*
	 * The generateHash method takes a salt, likely provided by the User object,
	 * and a password string and generates the hash described by the defined
	 * method.
	 * 
	 * This function is intended to be used internally, not by other services.
	 */
	private byte[] generateHash( String password, byte[] salt )
			throws NoSuchAlgorithmException, UnsupportedEncodingException
	{
		MessageDigest sha = MessageDigest.getInstance( UserService.HASHING_METHOD );
		sha.reset();
		sha.update( salt );
		return sha.digest( password.getBytes( "UTF-8" ) );
	}

	private byte[] salt( int length )
	{

		SecureRandom sRnd = new SecureRandom();
		byte[] salt = new byte[length];

		sRnd.nextBytes( salt );

		return salt;

	}

	public String bytesToString( byte[] bs )
	{
		StringBuilder s = new StringBuilder();

		for ( byte b : bs )
		{
			s.append( b );
		}

		return s.toString();
	}

	public User getUser()
	{
		return user;
	}

	public void setUser( User user )
	{
		this.user = user;
	}
	
	
}
