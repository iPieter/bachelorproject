package bachelorproject;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import bachelorproject.constraint_engine.ConstraintEngineLocationTester;
import bachelorproject.model.constraint_engine.LocationPoint;

public class ConstraintLocationTest
{

	@Test
	public void test()
	{
		ConstraintEngineLocationTester tester = new ConstraintEngineLocationTester();
		ArrayList<LocationPoint> polygon = new ArrayList<>();
		polygon.add( new LocationPoint( 100, 100 ) );
		polygon.add( new LocationPoint( 100, 500 ) );
		polygon.add( new LocationPoint( 300, 100 ) );
		
		assertTrue( tester.isPointInPolygon( polygon, 105, 480 ) );
		assertTrue( tester.isPointInPolygon( polygon, 150, 115 ) );
		assertFalse( tester.isPointInPolygon( polygon, 0, 0 ) );
		assertFalse( tester.isPointInPolygon( polygon, -8, -8 ) );
	}

}
