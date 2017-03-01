package be.kuleuven.cs.gent.projectweek.services;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import be.kuleuven.cs.gent.projectweek.ejb.UserEJB;
import be.kuleuven.cs.gent.projectweek.model.User;
import be.kuleuven.cs.gent.projectweek.model.UserRole;
import java.io.Serializable;

@SessionScoped
public class UserService implements Serializable
{	
	/*
	 * The purpose of the UserService is to provide a User object for the
	 * duration of the session. This will be provided mainly for the 
	 * AuthenticationService.
	 * When initialized, there'll be no User associated with this service, 
	 * but after login it'll be provided by the AuthenticationService.
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
		User u = new User();
		
		u.setName("John Doe");
		u.setEmail("john" + (int) (Math.random()*100) + "@test.be");
		u.setLastLogin(new Date());
		u.setSalt(salt(User.SALT_LENGTH));
		try
		{
			u.setPass(generateHash("password123", u.getSalt()));
			System.out.println("pass hash: " + u.getPass().toString());
		} catch (NoSuchAlgorithmException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		u.setRole(UserRole.OPERATOR);
		
		userEJB.createUser(u);
		
		System.out.println("Saved user ..." + u.getEmail());
	}
	
	/*
	 * The verification of a login consists of two steps:
	 * 1. finding the appropriate user object
	 * 2. generating the digest based on password and salt
	 * 
	 * Note that timing attacks are still an issue in this implementation.
	 */
	public boolean verificateLogin(String email, String password)
	{
		User u = userEJB.findUserByEmail(email);
		
		if (u != null)
		{
			try
			{
				return u.getPass().equals(generateHash(password, u.getSalt()));
			} catch (NoSuchAlgorithmException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				return false;
			}
		}
		
		return false;
	}
	
	/*
	 * The generateHash method takes a salt, likely provided by the User object, and a 
	 * password string and generates the hash described by the defined method. 
	 * 
	 * This function is intended to be used internally, not by other services.
	 */
	private byte[] generateHash(String password, byte[] salt) throws NoSuchAlgorithmException
	{
		MessageDigest sha = MessageDigest.getInstance(UserService.HASHING_METHOD);
		return sha.digest((password + salt.toString()).getBytes()); 
	}
	
	
	private byte[] salt(int length)
	{
	
		SecureRandom sRnd = new SecureRandom();
		byte[] salt = new byte[length];
		
		sRnd.nextBytes(salt);
		
		return salt;
		
	}
}
