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
 * 
 */
@Entity
@NamedQueries(
{ 
	@NamedQuery( name = "ProcessedSensorData.findAll", query = "SELECT p FROM ProcessedSensorData p" ),
	@NamedQuery( name = "ProcessedSensorData.findByID", query = "SELECT p FROM ProcessedSensorData p WHERE p.id = :id" ) 
} )

public class ProcessedSensorData implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	private int id;

	@Lob
	private String location;

	@Temporal( TemporalType.TIMESTAMP )
	@NotNull
	private Date time;

	@Lob
	private String track;

	@OneToOne( fetch = FetchType.LAZY )
	@NotNull
	private TrainCoach traincoach;

	public ProcessedSensorData()
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

	public String getLocation()
	{
		return this.location;
	}

	public void setLocation( String location )
	{
		this.location = location;
	}

	public Date getTime()
	{
		return this.time;
	}

	public void setTime( Date time )
	{
		this.time = time;
	}

	public String getTrack()
	{
		return this.track;
	}

	public void setTrack( String track )
	{
		this.track = track;
	}

	public void setTrainCoach( TrainCoach t )
	{
		this.traincoach = t;
	}
}