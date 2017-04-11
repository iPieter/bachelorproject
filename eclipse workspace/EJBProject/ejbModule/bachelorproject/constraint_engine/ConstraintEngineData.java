package bachelorproject.constraint_engine;

/**
 * 	This class resembles all the data that is used in the constraint
 *  analysis. 
 *  <p>
 *  More specifically this class contains:
 *  <ul>
 *  	<li>Latitude in degrees</li>
 *  	<li>Longitude in degrees</li>
 * 		<li>Yaw</li>
 * 		<li>Roll</li>
 * 		<li>Acceleration</li>
 * 		<li>Speed</li>
 *  </ul>
 *  @author Anton Danneels
 * */
public class ConstraintEngineData
{
	private double lat;
	private double lng;
	private double yaw;
	private double roll;
	private double accel;
	private double speed;
	
	public ConstraintEngineData(double lat, double lng, double yaw, double roll, double accel, double speed)
	{
		this.lat = lat;
		this.lng = lng;
		this.yaw = yaw;
		this.roll = roll;
		this.accel = accel;
		this.speed = speed;
	}
	public ConstraintEngineData()
	{
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
}
