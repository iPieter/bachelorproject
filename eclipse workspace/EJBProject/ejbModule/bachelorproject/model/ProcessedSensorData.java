package bachelorproject.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
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
 * The persistent class for the ProcessedSensorData database table.
 * <p>
 * Defines a ProcessedSensorData object consisting of: <br>
 * <ul>
 * 		<li>A unique ID</li>
 * 		<li>The location where the associated JSON file is stored on the disk</li>
 * 		<li>The time of the trip</li>
 * 		<li>The track where this data was recorded</li>
 * 		<li>An associated TrainCoach object</li>
 * </ul>
 */
@Entity
@NamedQueries(
{ 
	@NamedQuery( name = ProcessedSensorData.FIND_ALL, query = "SELECT p FROM ProcessedSensorData p" ),
	@NamedQuery( name = ProcessedSensorData.FIND_BY_ID, query = "SELECT p FROM ProcessedSensorData p WHERE p.id = :id" ),
	@NamedQuery( name = ProcessedSensorData.FIND_BY_TRAINCOACH_ID, query = "SELECT p FROM ProcessedSensorData p WHERE p.traincoach.id = :id ORDER BY p.time DESC" )
})

public class ProcessedSensorData implements Serializable
{
	/** Unique ID for Serializable */
	private static final long serialVersionUID = 1L;
	
	public static final String FIND_ALL = "ProcessedSensorData.findAll";
	public static final String FIND_BY_ID = "ProcessedSensorData.findByID";
	public static final String FIND_BY_TRAINCOACH_ID = "ProcessedSensorData.findByTrainCoachID";
	

	/** A unique ID. */
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	private int id;

	/** The location where the associated JSON file is stored. */
	@Lob
	private String location;

	/** Date when the trip was made */
	@Temporal( TemporalType.TIMESTAMP )
	@NotNull
	private Date time;

	/** Trip metadata */
	@Lob
	private String track;

	/** Associated TrainCoach */
	@OneToOne( fetch = FetchType.LAZY )
	@NotNull
	private TrainCoach traincoach;

	
	public ProcessedSensorData()
	{
	}

	/** Returns the ID
	 * @return This objects ID 
	 * */
	public int getId()
	{
		return this.id;
	}

	/** Sets the ID
	 * @param The new ID for this object
	 * */
	public void setId( int id )
	{
		this.id = id;
	}

	/** Returns the location on the disk of this objects associated JSON file.
	 * @return A filepath.
	 * */
	public String getLocation()
	{
		return this.location;
	}

	/**
	 * Sets the location of the JSON file.
	 * @param The new location of the associated JSON file.
	 * */
	public void setLocation( String location )
	{
		this.location = location;
	}

	/**
	 * 	Returns the date of the ride.
	 *  @return The date of the ride.
	 * */
	public Date getTime()
	{
		return this.time;
	}

	/**
	 * Sets the data of the trip.
	 * @param time The date 
	 * */
	public void setTime( Date time )
	{
		this.time = time;
	}

	/**
	 * 	Returns the track of the ride.
	 *  @return The track of the ride.
	 * */
	public String getTrack()
	{
		return this.track;
	}

	/**
	 * Sets the track of the ride
	 * @param track A String containing the ride info: start_station-end_station
	 * */
	public void setTrack( String track )
	{
		this.track = track;
	}

	/**
	 * @return the traincoach
	 */
	public TrainCoach getTraincoach()
	{
		return traincoach;
	}

	/**
	 * @param traincoach the traincoach to set
	 */
	public void setTraincoach( TrainCoach traincoach )
	{
		this.traincoach = traincoach;
	}
}