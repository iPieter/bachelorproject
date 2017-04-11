package bachelorproject.model.issue;

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

import org.ocpsoft.prettytime.PrettyTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bachelorproject.model.sensordata.SensorData;
import bachelorproject.model.user.User;

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
	@NamedQuery( name = Issue.FIND_BY_TRAINCOACH_ID,query = "SELECT i FROM Issue i "
						+ "WHERE EXISTS (SELECT d FROM ProcessedSensorData d WHERE d.traincoach.id = :traincoachId AND d.id = i.data.id ) "
						+ "AND i.status = :status"),	
	@NamedQuery( name = Issue.COUNT_BY_OPERATOR_ID, query = "SELECT COUNT(i) FROM Issue i WHERE i.operator.id= :operatorId AND i.assignedTime BETWEEN :backTime AND CURRENT_TIMESTAMP AND i.status= :status"),
	@NamedQuery( name = Issue.FIND_ALL_ACTIVE, query = "SELECT i FROM Issue i WHERE i.status= :status1 OR i.status= :status2" ),
	@NamedQuery( name = Issue.FIND_BY_SENSOR_ID, query = "SELECT i FROM Issue i WHERE i.data.id = :id" ),
	@NamedQuery( name = Issue.FIND_BY_WORKPLACE_ID, 
				query = "SELECT i FROM Issue i, "
						+ "ProcessedSensorData d,"
						+ "Workplace w, "
						+ "TrainCoach t "
						+ "WHERE t.needsReview = true AND t MEMBER OF w.traincoaches AND w.id = :workplaceId AND d.traincoach.id= t.id AND i.data.id = d.id"),
} )
public class Issue implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public static final String FIND_ALL = "Issue.findAll";
	public static final String FIND_BY_MECHANIC_ID = "Issue.findByMechanicId";
	public static final String FIND_BY_OPERATOR_ID = "Issue.findByOperatorId";
	public static final String FIND_BY_TRAINCOACH_ID = "Issue.findByTraincoachId";
	public static final String COUNT_BY_OPERATOR_ID="Issue.countByOperatorId";
	public static final String FIND_ALL_ACTIVE="Issue.findAllActive";
	public static final String FIND_BY_SENSOR_ID = "Issue.findBySensorID";
	public static final String FIND_BY_WORKPLACE_ID = "Issue.findByWorkplaceId";

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
	private SensorData data;
	
	/** Date when the issue was created & assigned */
	@Temporal( TemporalType.TIMESTAMP )
	@NotNull
	private Date assignedTime;
	
	/** Date when the issue status was changed to IN_PROGRESS */
	@Temporal( TemporalType.TIMESTAMP )
	private Date inProgressTime;
	
	/** Date when the issue status was changed to CLOSED */
	@Temporal( TemporalType.TIMESTAMP )
	private Date closedTime;

	@NotNull
	private double gpsLat;
	
	@NotNull
	private double gpsLon;
	
	public Issue()
	{
		assignedTime = new Date();
		inProgressTime = new Date();
		closedTime = new Date();
		gpsLat = 0.0;
		gpsLon = 0.0;
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

	public SensorData getData()
	{
		return data;
	}

	public void setData( SensorData data )
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

	/**
	 * @return the gpsLat
	 */
	public double getGpsLat() {
		return gpsLat;
	}

	/**
	 * @param gpsLat the gpsLat to set
	 */
	public void setGpsLat(double gpsLat) {
		this.gpsLat = gpsLat;
	}

	/**
	 * @return the gpsLon
	 */
	public double getGpsLon() {
		return gpsLon;
	}

	/**
	 * @param gpsLon the gpsLon to set
	 */
	public void setGpsLon(double gpsLon) {
		this.gpsLon = gpsLon;
	}
	
	@JsonIgnore
	public String getAssignedPrettyTime()
	{
		PrettyTime p = new PrettyTime();
		return p.format(this.assignedTime);	
	}
	
	@JsonIgnore
	public String getClosedPrettyTime()
	{
		PrettyTime p = new PrettyTime();
		return p.format(this.closedTime);	
	}
	
	@JsonIgnore
	public String getInProgressPrettyTime()
	{
		PrettyTime p = new PrettyTime();
		return p.format(this.inProgressTime);	
	}
}