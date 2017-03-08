package bachelorproject.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * The persistent class for the IssueAssets database table.
 * 
 */
@Entity
@NamedQueries(
{
	@NamedQuery( name = IssueAsset.FIND_ALL, query = "SELECT i FROM IssueAsset i" ),
	@NamedQuery( name = IssueAsset.FIND_BY_ISSUE_ID, query = "SELECT ia FROM Issue i JOIN i.assets ia WHERE i.id = :id" )
})

public class IssueAsset implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public static final String FIND_ALL = "IssueAsset.findAll";
	public static final String FIND_BY_ISSUE_ID = "IssueAsset.findByIssueID";

	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	private int id;

	private String descr;

	@Lob
	private String location;

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
}