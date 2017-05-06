package bachelorproject;

import static org.junit.Assert.*;

import org.junit.Test;

public class SplitTest
{

	@Test
	public void test()
	{
		test( "Multiple -", "Gent-Sint-Pieters", "Brussel-Zuid" );
		test( "One -", "Gent-Sint-Pieters", "Leuven" );
		test( "One -", "Leuven", "Brussel-Zuid" );
		test( "No -", "Leuven", "Oostende" );
	}
	
	private void test( String testName, String s1, String s2 )
	{
		String test = s1 + "--" + s2;
		String split[] = test.split( "--" );
		String workplace1 = split[0];
		String workplace2 = split[1];
		
		System.out.println( workplace1 );
		System.out.println( workplace2 );
		
		assertTrue( testName + ":" + test, workplace1.equals( s1 ) );
		assertTrue( testName + ":" + test, workplace2.equals( s2 ) );
	}

}
