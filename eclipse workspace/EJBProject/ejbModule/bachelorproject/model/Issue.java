package bachelorproject.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 * The persistent class for the Issue database table.
 * 
 */
@Entity
@NamedQuery( name = "Issue.findAll", query = "SELECT i FROM Issue i" )
public class Issue implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	private int id;

	@Lob
	private String descr;

	@Enumerated( EnumType.STRING )
	private IssueStatus status;

	@OneToMany( fetch = FetchType.LAZY )
	private List<IssueAsset> assets;

	@OneToOne( fetch = FetchType.LAZY )
	private User mechanic;

	@OneToOne( fetch = FetchType.LAZY )
	private User Operator;

	@OneToOne( fetch = FetchType.LAZY )
	private ProcessedSensorData data;

	public Issue()
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

	public String getDesc()
	{
		return this.descr;
	}

	public void setDesc( String desc )
	{
		this.descr = desc;
	}

	public IssueStatus getStatus()
	{
		return this.status;
	}

	public void setStatus( IssueStatus status )
	{
		this.status = status;
	}

	public String getDescr()
	{
		return descr;
	}

	public void setDescr( String descr )
	{
		this.descr = descr;
	}

	public List<IssueAsset> getAssets()
	{
		return assets;
	}

	public void setAssets( List<IssueAsset> assets )
	{
		this.assets = assets;
	}

	public User getMechanic()
	{
		return mechanic;
	}

	public void setMechanic( User mechanic )
	{
		this.mechanic = mechanic;
	}

	public User getOperator()
	{
		return Operator;
	}

	public void setOperator( User operator )
	{
		Operator = operator;
	}

	public ProcessedSensorData getData()
	{
		return data;
	}

	public void setData( ProcessedSensorData data )
	{
		this.data = data;
	}
}