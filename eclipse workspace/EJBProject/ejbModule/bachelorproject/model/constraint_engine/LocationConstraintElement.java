package bachelorproject.model.constraint_engine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import bachelorproject.constraint_engine.ConstraintEngine;

/**
 * 	Allows the operator to define constraints for a specific area.
 *  @author Anton Danneels
 * */
@Entity
public class LocationConstraintElement extends ConstraintElement implements Serializable
{
	private static final long serialVersionUID = -5790355500796825408L;

	@NotNull
	@OneToMany( fetch = FetchType.EAGER )
	private List<LocationPoint> polygon;

	public LocationConstraintElement()
	{
		super();
		polygon = new ArrayList<LocationPoint>();
	}

	public LocationConstraintElement( List<LocationPoint> polygon )
	{
		super();
		this.polygon = polygon;
	}
	
	public boolean visit( ConstraintEngine ce )
	{
		return ce.visit( this );
	}

	/**
	 * @return the polygon
	 */
	public List<LocationPoint> getPolygon()
	{
		return polygon;
	}

	/**
	 * @param polygon the polygon to set
	 */
	public void setPolygon( List<LocationPoint> polygon )
	{
		this.polygon = polygon;
	}

	@Override
	public String toString()
	{
		return "";
	}
}
