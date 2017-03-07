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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 * The persistent class for the Issue database table.
 * 
 */
@Entity
@NamedQueries(
{
	@NamedQuery( name = Issue.FIND_ALL, query = "SELECT i FROM Issue i" ),
	@NamedQuery( name = Issue.FIND_BY_MECHANIC_ID, query = "SELECT i FROM Issue i WHERE i.status = :status AND i.mechanic.id = :mechanic_id" ), 
	@NamedQuery( name = Issue.FIND_BY_TRAINCOACH_ID, 
				query = "SELECT i FROM Issue i WHERE EXISTS (SELECT d FROM ProcessedSensorData d WHERE d.traincoach.id = :traincoachId AND i.status = :status)")
				//"SELECT i FROM Issue i, (SELECT d FROM ProcessedSensorData d WHERE d.traincoach.id = :traincoachId) d WHERE i.data.id = d.id AND i.status = :status") 
} )
public class Issue implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public static final String FIND_ALL = "Issue.findAll";
	public static final String FIND_BY_MECHANIC_ID = "Issue.findByMechanicId";
	public static final String FIND_BY_TRAINCOACH_ID = "Issue.findByTraincoachId";
	

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

	public String getDescr()
	{
		return this.descr;
	}

	public void setDescr( String descr )
	{
		this.descr = descr;
	}

	public IssueStatus getStatus()
	{
		return this.status;
	}

	public void setStatus( IssueStatus status )
	{
		this.status = status;
	}

}