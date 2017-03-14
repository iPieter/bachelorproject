package bachelorproject.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
@NamedQueries
({
	@NamedQuery( name=LiveSensorData.FIND_ALL, query="SELECT lsd FROM LiveSensorData lsd" ),
	@NamedQuery( name=LiveSensorData.FIND_BY_DATE, query="SELECT lsd FROM LiveSensorData lsd WHERE lsd.date = :date" )
})
public class LiveSensorData implements Serializable
{
	public static final String FIND_ALL = "LiveSensorData.findAll";
	public static final String FIND_BY_DATE = "LiveSensorData.findByDate";
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	private int id;

	@NotNull
	@Lob
	private String track;
	
	@NotNull
	private TrainCoach traincoach;

	@NotNull
	private boolean isLive;
	
	@OneToMany( fetch = FetchType.LAZY )
	private List<LiveSensorDataEntry> entries = new ArrayList<LiveSensorDataEntry>();
	
	@NotNull
	@Temporal( TemporalType.TIMESTAMP )
	private Date date;
	
	public LiveSensorData()
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
	 * @return the isLive
	 */
	public boolean isLive()
	{
		return isLive;
	}

	/**
	 * @param isLive the isLive to set
	 */
	public void setLive( boolean isLive )
	{
		this.isLive = isLive;
	}

	/**
	 * @return the entries
	 */
	public List<LiveSensorDataEntry> getEntries()
	{
		return entries;
	}

	/**
	 * @param entries the entries to set
	 */
	public void setEntries( List<LiveSensorDataEntry> entries )
	{
		this.entries = entries;
	}
	
}
