package bachelorproject.model.constraint_engine;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

/**
 * 	Allows the operator to define constraints for a specific area.
 *  @author Anton Danneels
 * */
@Entity
public class LocationConstraintElement extends ConstraintElement
{
	@NotNull
	@OneToMany
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
	
	
}
