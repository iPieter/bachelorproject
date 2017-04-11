package bachelorproject.model.sensordata;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import bachelorproject.model.TrainCoach;

/**
 * 	Defines a base class for sensordata
 *  @see LiveSensorData
 *  @see ProcessedSensorData
 *  @author Anton Danneels
 * */
@Entity
public abstract class SensorData
{
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	protected int id;

	@NotNull
	@Lob
	protected String track;
	
	@NotNull
	@OneToOne
	protected TrainCoach traincoach;
	
	@NotNull
	@Temporal( TemporalType.TIMESTAMP )
	protected Date date;

	public SensorData()
	{
	}

	public SensorData(int id, String track, TrainCoach traincoach, Date date)
	{
		this.id = id;
		this.track = track;
		this.traincoach = traincoach;
		this.date = date;
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
	 * @return the track
	 */
	public String getTrack()
	{
		return track;
	}

	/**
	 * @param track the track to set
	 */
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

	/**
	 * @return the date
	 */
	public Date getDate()
	{
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate( Date date )
	{
		this.date = date;
	}
}
