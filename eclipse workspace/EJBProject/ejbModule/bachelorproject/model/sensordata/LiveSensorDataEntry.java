package bachelorproject.model.sensordata;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
@NamedQueries
({
	@NamedQuery( name=LiveSensorDataEntry.FIND_ALL, query="SELECT lsd FROM LiveSensorData lsd" )
})
public class LiveSensorDataEntry implements Serializable
{
	private static final long serialVersionUID = 1L;

	public static final String FIND_ALL = "LiveSensorDataEntry.findAll";
	
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	private int id;

	@NotNull
	private double lat;
	
	@NotNull
	private double lng;
	
	@NotNull
	private double yaw;
	
	@NotNull
	private double roll;
	
	@NotNull
	private double speed;
	
	@NotNull
	private double accel;
	
	@Temporal( TemporalType.TIMESTAMP )
	private Date time;

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
	 * @return the lat
	 */
	public double getLat()
	{
		return lat;
	}

	/**
	 * @param lat the lat to set
	 */
	public void setLat( double lat )
	{
		this.lat = lat;
	}

	/**
	 * @return the lng
	 */
	public double getLng()
	{
		return lng;
	}

	/**
	 * @param lng the lng to set
	 */
	public void setLng( double lng )
	{
		this.lng = lng;
	}

	/**
	 * @return the yaw
	 */
	public double getYaw()
	{
		return yaw;
	}

	/**
	 * @param yaw the yaw to set
	 */
	public void setYaw( double yaw )
	{
		this.yaw = yaw;
	}

	/**
	 * @return the roll
	 */
	public double getRoll()
	{
		return roll;
	}

	/**
	 * @param roll the roll to set
	 */
	public void setRoll( double roll )
	{
		this.roll = roll;
	}

	/**
	 * @return the speed
	 */
	public double getSpeed()
	{
		return speed;
	}

	/**
	 * @param speed the speed to set
	 */
	public void setSpeed( double speed )
	{
		this.speed = speed;
	}

	/**
	 * @return the accel
	 */
	public double getAccel()
	{
		return accel;
	}

	/**
	 * @param accel the accel to set
	 */
	public void setAccel( double accel )
	{
		this.accel = accel;
	}

	/**
	 * @return the time
	 */
	public Date getTime()
	{
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime( Date time )
	{
		this.time = time;
	}
}
