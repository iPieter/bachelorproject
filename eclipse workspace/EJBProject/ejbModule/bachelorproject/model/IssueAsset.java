package bachelorproject.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 * The persistent class for the IssueAssets database table.
 * 
 */
@Entity
@NamedQueries(
{
	@NamedQuery( name = IssueAsset.FIND_ALL, query = "SELECT i FROM IssueAsset i" ),
	@NamedQuery( name = IssueAsset.FIND_BY_ID, query = "SELECT i FROM IssueAsset i WHERE i.id = :id" ),
	@NamedQuery( name = IssueAsset.FIND_BY_ISSUE_ID, query = "SELECT ia FROM Issue i JOIN i.assets ia WHERE i.id = :id ORDER BY ia.time DESC" )
})

public class IssueAsset implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public static final String FIND_ALL = "IssueAsset.findAll";
	public static final String FIND_BY_ID = "IssueAsset.findByID";
	public static final String FIND_BY_ISSUE_ID = "IssueAsset.findByIssueID";

	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	private int id;

	@NotNull
	private String descr;
	
	@Temporal( TemporalType.TIMESTAMP )
	@NotNull
	private Date time;
	
	@Lob
	private String location;

	@NotNull
	@OneToOne
	private User user;
	
	public IssueAsset()
	{
	}

	/**
	 * @return the id
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId( int id )
	{
		this.id = id;
	}

	/**
	 * @return the descr
	 */
	public String getDescr()
	{
		return descr;
	}

	/**
	 * @param descr the descr to set
	 */
	public void setDescr( String descr )
	{
		this.descr = descr;
	}

	/**
	 * @return the location
	 */
	public String getLocation()
	{
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation( String location )
	{
		this.location = location;
	}

	/**
	 * @return the time
	 */
	public Date getTime()
	{
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime( Date time )
	{
		this.time = time;
	}

	/**
	 * @return the user
	 */
	public User getUser()
	{
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser( User user )
	{
		this.user = user;
	}

	/**
	 * 	Returns a String representation of this object
	 *  @return The String representation	 
	 * */
	public String toString()
	{
		return descr + ":" + location;
	}
}