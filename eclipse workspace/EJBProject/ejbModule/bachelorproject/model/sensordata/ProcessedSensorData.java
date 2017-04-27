package bachelorproject.model.sensordata;

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

import bachelorproject.model.TrainCoach;

/**
 * The persistent class for the ProcessedSensorData database table.
 * <p>
 * Defines a ProcessedSensorData object consisting of: <br>
 * <ul>
 * <li>A unique ID</li>
 * <li>The location where the associated JSON file is stored on the disk</li>
 * <li>The time of the trip</li>
 * <li>The track where this data was recorded</li>
 * <li>An associated TrainCoach object</li>
 * </ul>
 */
@Entity
@NamedQueries(
{ @NamedQuery( name = ProcessedSensorData.FIND_ALL, query = "SELECT p FROM ProcessedSensorData p" ),
		@NamedQuery( name = ProcessedSensorData.FIND_BY_ID, query = "SELECT p FROM ProcessedSensorData p WHERE p.id = :id" ),
		@NamedQuery( name = ProcessedSensorData.FIND_BY_TRAINCOACH_ID, query = "SELECT p FROM ProcessedSensorData p WHERE p.traincoach.id = :id ORDER BY p.date DESC" ) } )

public class ProcessedSensorData extends SensorData implements Serializable
{
	/** Unique ID for Serializable */
	private static final long serialVersionUID = 1L;

	public static final String FIND_ALL = "ProcessedSensorData.findAll";
	public static final String FIND_BY_ID = "ProcessedSensorData.findByID";
	public static final String FIND_BY_TRAINCOACH_ID = "ProcessedSensorData.findByTrainCoachID";

	/** The location where the associated JSON file is stored. */
	@Lob
	private String location;

	public ProcessedSensorData()
	{
	}

	/**
	 * Returns the location on the disk of this objects associated JSON file.
	 * 
	 * @return A filepath.
	 */
	public String getLocation()
	{
		return this.location;
	}

	/**
	 * Sets the location of the JSON file.
	 * 
	 * @param The
	 *            new location of the associated JSON file.
	 */
	public void setLocation( String location )
	{
		this.location = location;
	}
}