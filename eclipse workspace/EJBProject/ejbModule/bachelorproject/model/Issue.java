package bachelorproject.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 * The persistent class for the Issue database table.
 * 
 */
@Entity
@NamedQueries(
{
	@NamedQuery( name = Issue.FIND_ALL, query = "SELECT i FROM Issue i" ),
	@NamedQuery( name = Issue.FIND_BY_MECHANIC_ID, query = "SELECT i FROM Issue i WHERE i.status = :status AND i.mechanic.id = :mechanic_id" ), 
	@NamedQuery( name = Issue.FIND_BY_OPERATOR_ID, query = "SELECT i FROM Issue i WHERE i.status = :status AND i.operator.id = :operator_id" ),
	@NamedQuery( name = Issue.FIND_BY_TRAINCOACH_ID, 
				query = "SELECT i FROM Issue i WHERE EXISTS (SELECT d FROM ProcessedSensorData d WHERE d.traincoach.id = :traincoachId AND d.id = i.data.id ) AND i.status = :status"),				
	@NamedQuery( name = Issue.COUNT_BY_OPERATOR_ID, query = "SELECT COUNT(i) FROM Issue i WHERE i.assignedTime BETWEEN CURRENT_TIMESTAMP and :backTime" ),
} )
public class Issue implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public static final String FIND_ALL = "Issue.findAll";
	public static final String FIND_BY_MECHANIC_ID = "Issue.findByMechanicId";
	public static final String FIND_BY_OPERATOR_ID = "Issue.findByOperatorId";
	public static final String FIND_BY_TRAINCOACH_ID = "Issue.findByTraincoachId";
	public static final String COUNT_BY_OPERATOR_ID="Issue.countByOperatorId";
	

	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	private int id;

	@Lob
	private String descr;

	@Enumerated( EnumType.STRING )
	private IssueStatus status;

	@OneToMany( fetch = FetchType.LAZY )
	private List<IssueAsset> assets = new ArrayList<IssueAsset>();

	@OneToOne( fetch = FetchType.LAZY )
	private User mechanic;

	@OneToOne( fetch = FetchType.LAZY )
	private User operator;

	@OneToOne( fetch = FetchType.LAZY )
	private ProcessedSensorData data;
	
	/** Date when the issue was created & assigned */
	@Temporal( TemporalType.TIMESTAMP )
	@NotNull
	private Date assignedTime;
	
	/** Date when the issue status was changed to IN_PROGRESS */
	@Temporal( TemporalType.TIMESTAMP )
	@NotNull
	private Date inProgressTime;
	
	/** Date when the issue status was changed to CLOSED */
	@Temporal( TemporalType.TIMESTAMP )
	@NotNull
	private Date closedTime;

	private double gpsLat;
	private double gpsLon;
	
	public Issue()
	{
	}

	// GETTERS & SETTERS
	public int getId()
	{
		return this.id;
	}

	public void setId( int id )
	{
		this.id = id;
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
		return operator;
	}

	public void setOperator( User operator )
	{
		this.operator = operator;
	}

	public ProcessedSensorData getData()
	{
		return data;
	}

	public void setData( ProcessedSensorData data )
	{
		this.data = data;
	}

	/**
	 * @return the assignedTime
	 */
	public Date getAssignedTime() {
		return assignedTime;
	}

	/**
	 * @param assignedTime the assignedTime to set
	 */
	public void setAssignedTime(Date assignedTime) {
		this.assignedTime = assignedTime;
	}

	/**
	 * @return the inProgressTime
	 */
	public Date getInProgressTime() {
		return inProgressTime;
	}

	/**
	 * @param inProgressTime the inProgressTime to set
	 */
	public void setInProgressTime(Date inProgressTime) {
		this.inProgressTime = inProgressTime;
	}

	/**
	 * @return the closedTime
	 */
	public Date getClosedTime() {
		return closedTime;
	}

	/**
	 * @param closedTime the closedTime to set
	 */
	public void setClosedTime(Date closedTime) {
		this.closedTime = closedTime;
	}
}