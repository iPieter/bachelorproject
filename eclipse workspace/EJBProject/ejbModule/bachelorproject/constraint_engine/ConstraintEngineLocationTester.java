package bachelorproject.constraint_engine;

import java.util.List;

import bachelorproject.model.constraint_engine.LocationPoint;

/**
 * Test if a point is in the defined polygon.
 * 
 * @see ConstraintEngine
 * @author Anton Danneels
 */
public class ConstraintEngineLocationTester
{
	/**
	 * Test if a point is left of a line specified by 2 points p0 and p1 using a
	 * signed area calculation.
	 * <p>
	 * If the points are supplied in a clockwise order, then the area will be
	 * positive, otherwise it will be negative. If the point is on the line,
	 * there is no area so this method will return 0. It is based on the cross
	 * product of 2 vectors.
	 * 
	 * @param p0
	 *            The first point of a line
	 * @param p1
	 *            The second point of a line
	 * @param lat
	 *            The lat of the point to be tested
	 * @param lng
	 *            The lng of the point to be tested
	 * @return > 0 if the point is left of the line = 0 if the point is on the
	 *         line < 0 if the point is right of the line
	 */
	private double isLeft( LocationPoint p0, LocationPoint p1, double lat, double lng )
	{
		return ( p1.getLat() - p0.getLat() ) * ( lng - p0.getLng() )
				- ( lat - p0.getLat() ) * ( p1.getLng() - p0.getLng() );
	}

	/**
	 * Tests if a point is in a polygon using the winding number algorithm.
	 * {@link https://en.wikipedia.org/wiki/Point_in_polygon}
	 * 
	 * @param polygon
	 *            A list of {@link LocationPoint } objects. Note that the end
	 *            and start point can be different.
	 * @param lat
	 *            The latitude of the point that needs to be tested.
	 * @param lng
	 *            The longitude of the point that needs to be tested.
	 * @return This method returns true if the point is in the polygon, false
	 *         otherwise.
	 */
	public boolean isPointInPolygon( List<LocationPoint> polygon, double lat, double lng )
	{
		// In order to test every line of the polygon, we need the last & first
		// point to be connected
		polygon.add( polygon.get( 0 ) );

		int wn = 0;

		for ( int i = 0; i < polygon.size() - 1; i++ )
		{
			LocationPoint p0 = polygon.get( i + 0 );
			LocationPoint p1 = polygon.get( i + 1 );

			if ( p0.getLng() <= lng )
			{
				// Upward crossing: p1 is above p0
				if ( p1.getLng() > lng )
				{
					if ( isLeft( p0, p1, lat, lng ) > 0 ) wn++;
				}
			}
			else
			{
				// Downward crossing: p1 is under p0
				if ( p1.getLng() < lng )
				{
					if ( isLeft( p0, p1, lat, lng ) < 0 ) wn--;
				}
			}
		}

		polygon.remove( polygon.size() - 1 );

		return wn != 0;
	}
}
