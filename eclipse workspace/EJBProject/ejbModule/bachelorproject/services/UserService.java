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
import bachelorproject.model.user.User;
import bachelorproject.model.user.UserRole;

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

	/**
	 * Generate some fake users when none are present.
	 * <p>
	 * This is only ment for debugging.
	 * 
	 * @author Pieter Delobelle
	 * @version 0.0.1
	 */
	@PostConstruct
	public void init()
	{

		// TODO: generate a bunch of users
		if (userEJB.findAllUsers().size() == 0)
		{
			for (int i = 0; i < 10; i++)
			{
				User u = new User();

				u.setName("John Doe");
				u.setEmail("john" + i + "@test.be");

				populateUser(u, "password123", "qwertyui");

				u.setRole(i > 5 ? UserRole.OPERATOR : UserRole.MECHANIC);

				userEJB.createUser(u);

				System.out.println("Saved user ..." + u.getEmail());
			}

			User u = new User();

			u.setName("Jane Doe");
			u.setEmail("admin@test.be");

			populateUser(u, "password123", "qwertyui");

			u.setRole(UserRole.ADMIN);

			userEJB.createUser(u);

			System.out.println("Saved user ..." + u.getEmail());

		}

	}

	/**
	 * If the user that's currently logged in has the right UserRole, this
	 * function will return true. False otherwise. If no user is logged in, this
	 * function will also return false.
	 * 
	 * @author Pieter Delobelle
	 * @version 0.1.0
	 * @param ur
	 *            UserRole to check.
	 * @return True if the user has the required role, false otherwise.
	 */
	public boolean hasCurrentUserRequiredRole(UserRole ur)
	{
		if (this.user == null)
		{
			return false;
		}

		return this.user.getRole().equals(ur);

	}

	/**
	 * Update the password of the provided User object. Also generates a new
	 * salt, so each hash is unique even if the password would be the same for
	 * some reason.
	 * 
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @param user
	 * @param password
	 */
	public static void setPassword(User user, String password)
	{
		user.setSalt(salt(User.SALT_LENGTH));
		try
		{
			user.setPass(generateHash(password, user.getSalt()));
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * The verification of a login consists of two steps: 1. finding the
	 * appropriate user object 2. generating the digest based on password and
	 * salt
	 * <p>
	 * Note that timing attacks are still an issue in this implementation.
	 * <p>
	 * Note we do not provide feedback about the existence of either the email
	 * nor the password (obviously). This is to prevent automated guessing and
	 * stuff.
	 * 
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @param email
	 *            A string with the email address used as login credential
	 * @param password
	 *            A string with the password used as login credential
	 * @return True if the user has correct credentials, false otherwise.
	 */
	public boolean verificateLogin(String email, String password)
	{
		User u = userEJB.findUserByEmail(email);

		if (u != null)
		{
			try
			{
				if (Arrays.equals(u.getPass(), (generateHash(password, u.getSalt()))))
				{
					this.user = u;

					// set the lastLogin
					u.setLastLogin(new Date());

					return true;

				} else
				{

					return false;
				}

			} catch (UnsupportedEncodingException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();

				return false;
			} catch (NoSuchAlgorithmException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return false;
	}

	/**
	 * Simple method to remove the user object, if there's one. This will result
	 * in a logout.
	 */
	public void tryLogout()
	{
		this.user = null;
	}

	/**
	 * The generateHash method takes a salt, likely provided by the User object,
	 * and a password string and generates the hash described by the defined
	 * method.
	 * <p>
	 * This function is intended to be used internally, not by other services.
	 * 
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @param password
	 *            A String with the password to generate a hash.
	 * @param salt
	 *            For added security, we combine this hash with a salt.
	 * @return A hash of the password with salt by the specified hashing
	 *         mechanism.
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 * @see MesageDigest
	 */
	public static byte[] generateHash(String password, byte[] salt)
			throws NoSuchAlgorithmException, UnsupportedEncodingException
	{
		MessageDigest sha = MessageDigest.getInstance(UserService.HASHING_METHOD);
		sha.reset();
		sha.update(salt);
		return sha.digest(password.getBytes("UTF-8"));
	}

	/**
	 * Given a User object with filled in email, name etc..., this service will
	 * populate the other fields (like salt, lastLogin etc...) and calculate the
	 * password hash provided by the second parameter.
	 * 
	 * @author Pieter Delobelle
	 * @version 0.1.0
	 * @param user
	 *            The User object to be populated
	 * @param password
	 *            The password intender for the linked User object
	 */
	public static void populateUser(User user, String password, String image)
	{

		// set password
		setPassword(user, password);

		// Set the lastLogin to the current date/time
		user.setLastLogin(new Date());

		// Set to default profile avatar
		// TODO: change this
		user.setImageHash(image);

	}

	/**
	 * Generates a salt of a provided length in bytes using SecureRandom.
	 * 
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @param length
	 *            The desired length in bytes
	 * @return A byte array with random bytes.
	 * @see SecureRandom
	 */
	public static byte[] salt(int length)
	{

		SecureRandom sRnd = new SecureRandom();
		byte[] salt = new byte[length];

		sRnd.nextBytes(salt);

		return salt;

	}

	/**
	 * Convert a byte array to an ASCII string.
	 * 
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @param bs
	 *            The byte array to be converted to a string.
	 * @return A string representing the byte array as ASCII.
	 */
	public static String bytesToString(byte[] bs)
	{
		StringBuilder s = new StringBuilder();

		for (byte b : bs)
		{
			s.append(b);
		}

		return s.toString();
	}

	//GETTERS & SETTERS
	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}

	/**
	 * This function matches the hash against the database. If no user is logged
	 * in, it returns false. Otherwise true if it's a match and false if not.
	 * 
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @param password
	 *            Calculates a hash with salt from this password and verificates
	 *            it.
	 * @return True if a match, false otherwise.
	 * @see verificateLogin
	 */
	public boolean veficatePassword(String password)
	{
		try
		{
			if (this.user != null && Arrays.equals(this.user.getPass(), (generateHash(password, this.user.getSalt()))))
			{
				return true;

			} else
			{
				return false;
			}
		} catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();

			return false;
		} catch (NoSuchAlgorithmException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}
}
