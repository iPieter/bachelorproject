package bachelorproject.model.user;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Authentication tokens for REST service.
 * 
 * @author Pieter Delobelle
 * @version 1.0.0
 */
@Entity
@NamedQueries(
{ @NamedQuery( name = Token.FIND_BY_TOKEN, query = "SELECT t FROM Token t WHERE t.token = :token" ), } )
public class Token implements Serializable
{

	// Queries
	public final static String FIND_BY_TOKEN = "Token.findByToken";

	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	private int id;

	@NotNull
	@ManyToOne
	private User owner;

	@Lob
	@NotNull
	private String token;

	@Temporal( TemporalType.TIMESTAMP )
	@JsonIgnore
	private Date expires;

	private static final long serialVersionUID = 7660243869003371055L;

	public Token()
	{
		super();
	}

	/**
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @return the id
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @param id
	 *            the id to set
	 */
	public void setId( int id )
	{
		this.id = id;
	}

	/**
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @return the owner user object
	 */
	public User getOwner()
	{
		return owner;
	}

	/**
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @param owner
	 *            the owner user object to set
	 */
	public void setOwner( User owner )
	{
		this.owner = owner;
	}

	/**
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @return the token
	 */
	public String getToken()
	{
		return token;
	}

	/**
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @param token
	 *            the token to set
	 */
	public void setToken( String token )
	{
		this.token = token;
	}

	/**
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @return the expires time and date
	 */
	public Date getExpires()
	{
		return expires;
	}

	/**
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @param expires
	 *            the expires time and date to set
	 */
	public void setExpires( Date expires )
	{
		this.expires = expires;
	}

}
