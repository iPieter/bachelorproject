package bachelorproject.model.constraint_engine;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 * 	Defines a point(lat,lng) in the database.
 *  @author Anton Danneels
 * */
@Entity
public class LocationPoint implements Serializable
{
	private static final long serialVersionUID = 4294967991640387961L;

	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	private int id;
	
	@NotNull
	private double lat;
	
	@NotNull
	private double lng;

	public LocationPoint()
	{
	}

	public LocationPoint(double lat, double lng)
	{
		this.lat = lat;
		this.lng = lng;
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
	
	public String toString()
	{
		return lat + "," + lng;
	}
}
