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

	private int descr;

	@Lob
	private String location;

	public IssueAsset()
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

	public int getDesc()
	{
		return this.descr;
	}

	public void setDesc( int desc )
	{
		this.descr = desc;
	}

	public String getLocation()
	{
		return this.location;
	}

	public void setLocation( String location )
	{
		this.location = location;
	}

}