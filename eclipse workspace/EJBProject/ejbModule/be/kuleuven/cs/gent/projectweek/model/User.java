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
@NamedQuery(name = "User.findAll", query = "SELECT u FROM User u")
public class User implements Serializable
{
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
	@Size(max = 96)
	private byte[] pass;

	@Lob
	@NotNull
	@Size(max = 32)
	private byte[] salt;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastLogin;
	
	@Enumerated(EnumType.STRING)
	@NotNull
	private UserRole role;

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