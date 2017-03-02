package be.kuleuven.cs.gent.projectweek.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

/**
 * The persistent class for the User database table.
 * 
 */
@Entity
@NamedQueries({
	@NamedQuery(name = User.FIND_ALL, query = "SELECT u FROM User u"),
	@NamedQuery(name = User.FIND_BY_EMAIL, query = "SELECT u FROM User u WHERE u.email = :email"),
})
public class User implements Serializable
{
	//Named queries
	public static final String FIND_ALL = "findAll";
	public static final String FIND_BY_EMAIL = "findByEmail";
	
	//Private vars
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
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
	@Column(length = User.PASS_HASH_LENGTH)
	private byte[] pass;

	@Lob
	@NotNull
	@Column(length = User.SALT_LENGTH)
	private byte[] salt;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastLogin;
	
	@Enumerated(EnumType.STRING)
	@NotNull
	private UserRole role;

	//Used constants
	public static final int SALT_LENGTH = 32;
	public static final int PASS_HASH_LENGTH = 256/8;
	
	public User()
	{
	}

	public int getId()
	{
		return this.id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getEmail()
	{
		return this.email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}	
	
	public byte[] getPass()
	{
		return this.pass;
	}

	public void setPass(byte[] pass)
	{
		this.pass = pass;
	}

	public byte[] getSalt()
	{
		return this.salt;
	}

	public void setSalt(byte[] salt)
	{
		this.salt = salt;
	}

	public UserRole getRole()
	{
		return this.role;
	}

	public void setRole(UserRole role)
	{
		this.role = role;
	}

	public Date getLastLogin()
	{
		return this.lastLogin;
	}
	
	public void setLastLogin(Date lastLogin)
	{
		this.lastLogin = lastLogin;
	}
	
	@Override
	public String toString()
	{
		return this.name + " email: " + this.email;
	}
}