package bachelorproject.model.sensordata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

@NamedQueries
({
	@NamedQuery( name=LiveSensorData.FIND_ALL, query="SELECT lsd FROM LiveSensorData lsd" ),
	@NamedQuery( name=LiveSensorData.FIND_BY_DATE, query="SELECT lsd FROM LiveSensorData lsd WHERE lsd.date = :date" ),
	@NamedQuery( name=LiveSensorData.FIND_ALL_ACTIVE, query="SELECT lsd FROM LiveSensorData lsd WHERE lsd.isLive = true" ),
	@NamedQuery( name=LiveSensorData.FIND_ALL_AFTER_DATE, query="SELECT lsde FROM LiveSensorData lsd JOIN lsd.entries lsde WHERE lsde.time > :date AND lsd.id = :id ORDER BY lsde.time ASC" )
})
@Entity
public class LiveSensorData extends SensorData implements Serializable
{
	public static final String FIND_ALL = "LiveSensorData.findAll";
	public static final String FIND_BY_DATE = "LiveSensorData.findByDate";
	public static final String FIND_ALL_AFTER_DATE = "LiveSensorDataEntry.findAfterDate";
	public static final String FIND_ALL_ACTIVE = "LiveSensorData.findAllActive";

	private static final long serialVersionUID = 1L;
	
	@NotNull
	private boolean isLive;
	
	@OneToMany( fetch = FetchType.LAZY )
	private List<LiveSensorDataEntry> entries = new ArrayList<LiveSensorDataEntry>();
		
	public LiveSensorData()
	{
		super();
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
