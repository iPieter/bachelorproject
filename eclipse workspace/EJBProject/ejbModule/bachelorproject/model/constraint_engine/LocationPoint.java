package bachelorproject.model.constraint_engine;

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
public class LocationPoint
{
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	private int id;
	
	@NotNull
	private double lat;
	
	@NotNull
	private double lng;

	public LocationPoint()
	{
		super();
	}

	public LocationPoint(double lat, double lng)
	{
		super();
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
	
}
