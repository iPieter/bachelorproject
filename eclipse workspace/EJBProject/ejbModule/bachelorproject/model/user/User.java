package bachelorproject.model.user;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.ocpsoft.prettytime.PrettyTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The persistent class for the User database table.
 * 
 */
@Entity
@NamedQueries(
{ 
	@NamedQuery( name = User.FIND_ALL, query = "SELECT u FROM User u" ),
	@NamedQuery( name = User.FIND_BY_EMAIL, query = "SELECT u FROM User u WHERE u.email = :email" ), 
	} )
public class User implements Serializable
{
	// Named queries
	public static final String FIND_ALL = "User.findAll";
	public static final String FIND_BY_EMAIL = "User.findByEmail";

	// Private vars
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	private int id;

	@Lob
	@NotNull
	private String name;

	@Lob
	@NotNull
	@Email
	private String email;
	
	@Lob
	@NotNull
	@Column( length = User.PASS_HASH_LENGTH )
	@JsonIgnore
	private byte[] pass;

	@Lob
	@NotNull
	@Column( length = User.SALT_LENGTH )
	@JsonIgnore
	private byte[] salt;

	@Temporal( TemporalType.TIMESTAMP )
	@JsonIgnore
	private Date lastLogin;

	@Enumerated( EnumType.STRING )
	@NotNull
	private UserRole role;
	
	@Lob
	private String imageHash;

	// Used constants
	public static final int SALT_LENGTH = 32;
	public static final int PASS_HASH_LENGTH = 256 / 8;

	public User()
	{
	}

	public int getId()
	{
		return this.id;
	}

	public void setId( int id )
	{
		this.id = id;
	}

	public String getName()
	{
		return this.name;
	}

	public void setName( String name )
	{
		this.name = name;
	}

	public String getEmail()
	{
		return this.email;
	}

	public void setEmail( String email )
	{
		this.email = email;
	}

	public byte[] getPass()
	{
		return this.pass;
	}

	public void setPass( byte[] pass )
	{
		this.pass = pass;
	}

	public byte[] getSalt()
	{
		return this.salt;
	}

	public void setSalt( byte[] salt )
	{
		this.salt = salt;
	}

	public UserRole getRole()
	{
		return this.role;
	}

	public void setRole( UserRole role )
	{
		this.role = role;
	}

	@JsonIgnore
	public Date getLastLogin()
	{
		return this.lastLogin;
	}

	public void setLastLogin( Date lastLogin )
	{
		this.lastLogin = lastLogin;
	}
	
	@JsonIgnore
	public String getLastPrettyLogin()
	{
		PrettyTime p = new PrettyTime();
		return p.format(this.lastLogin);
	}
	
	

	/**
	 * Returns a hash of the profile picture the user uploaded
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @return the imageHash
	 */
	public String getImageHash()
	{
		return imageHash;
	}

	/**
	 * Sets the hash of image the user uploaded as a profile picture.
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @param imageHash the imageHash to set
	 */
	public void setImageHash(String imageHash)
	{
		this.imageHash = imageHash;
	}

	@Override
	public String toString()
	{
		return this.name + " email: " + this.email;
	}
}