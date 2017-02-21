package model;

import java.io.Serializable;
import javax.persistence.*;

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
	private String name;

	@Lob
	private String pass;

	@Lob
	private String salt;

	@Enumerated(EnumType.STRING)
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

	public String getPass()
	{
		return this.pass;
	}

	public void setPass(String pass)
	{
		this.pass = pass;
	}

	public String getSalt()
	{
		return this.salt;
	}

	public void setSalt(String salt)
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

}