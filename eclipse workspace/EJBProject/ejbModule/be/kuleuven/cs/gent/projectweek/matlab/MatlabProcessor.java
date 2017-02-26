package be.kuleuven.cs.gent.projectweek.matlab;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;
import matlabcontrol.extensions.MatlabNumericArray;
import matlabcontrol.extensions.MatlabTypeConverter;
import model.ProcessedSensorData;
import model.TrainCoach;

@Singleton
@Startup
public class MatlabProcessor
{
	String script = "";

	//@PersistenceContext( unitName = "EJBProject" )
	//EntityManager manager;

	@PostConstruct
	public void init() throws MatlabConnectionException, MatlabInvocationException, IOException
	{
		String script = "";
		try
		{
			URL url = getClass().getResource( "script.txt" );
			BufferedReader reader = new BufferedReader( new InputStreamReader( url.openStream() ) );
			String line = "";
			while ( ( line = reader.readLine() ) != null )
				script += line + System.getProperty( "line.separator" );
			reader.close();
		}
		catch ( Exception e )
		{
			System.out.println( e.getMessage() );
		}

		MatlabProxyFactory factory = new MatlabProxyFactory();
		MatlabProxy proxy = factory.getProxy();

		URL url = getClass().getResource( "test03_track16_speed40_radius125.mat" );
		String path = url.toString().replaceAll( "vfs:", "" );
		// Linux heeft een path nodig dat begint met een /, windows niet dus dit
		// is een idiote hack
		// om het op beide platformen werkend te krijgen
		if ( System.getProperty( "os.name" ).toLowerCase().contains( "win" ) )
			path = path.substring( 1, path.length() );

		proxy.eval( "load('" + path + "')" );
		proxy.eval( script );

		MatlabTypeConverter processor = new MatlabTypeConverter( proxy );
		MatlabNumericArray yaw_out = processor.getNumericArray( "yaw_out" );
		MatlabNumericArray roll_out = processor.getNumericArray( "roll_out" );
		MatlabNumericArray time_out = processor.getNumericArray( "time_out" );
		MatlabNumericArray lat_out = processor.getNumericArray( "lat_out" );
		MatlabNumericArray lng_out = processor.getNumericArray( "lng_out" );
		double lat_off = ( (double[]) proxy.getVariable( "lat_offset" ) )[0];
		double lng_off = ( (double[]) proxy.getVariable( "lon_offset" ) )[0];

		double yaw[][] = yaw_out.getRealArray2D();
		double roll[][] = roll_out.getRealArray2D();

		double year = time_out.getRealValue( 0 );
		double month = time_out.getRealValue( 1 );
		double day = time_out.getRealValue( 2 );
		double hour = time_out.getRealValue( 3 );
		double minute = time_out.getRealValue( 4 );
		double second = time_out.getRealValue( 5 );

		System.out.println( System.getProperty( "user.home" ) );
		try
		{
			BufferedWriter writer = new BufferedWriter(
					new FileWriter( System.getProperty( "user.home" ) + "/test03_track16_speed40_radius125.json" ) );
			writeLine( "{", false, writer );
			writeLine( writeValue( "year", year ), true, writer );
			writeLine( writeValue( "month", month ), true, writer );
			writeLine( writeValue( "day", day ), true, writer );
			writeLine( writeValue( "hour", hour ), true, writer );
			writeLine( writeValue( "minute", minute ), true, writer );
			writeLine( writeValue( "second", second ), true, writer );
			writeLine( writeValue( "lat_off", lat_off ), true, writer );
			writeLine( writeValue( "lng_off", lng_off ), true, writer );

			writeArray2D( "yaw", yaw, true, writer );
			writeArray2D( "roll", roll, true, writer );

			writeNumericArray( "lat", lat_out, true, writer );
			writeNumericArray( "lng", lng_out, false, writer );

			writeLine( "}", false, writer );

			writer.flush();
			writer.close();

			/*
			TrainCoach trainCoach = new TrainCoach();
			trainCoach.setConductor( "test" );
			trainCoach.setName( "test" );
			trainCoach.setType( "test" );
			ProcessedSensorData data = new ProcessedSensorData();
			data.setLocation( System.getProperty( "user.home" ) + "/test03_track16_speed40_radius125.json" );
			data.setTime( new Date() );
			data.setTrack( "test" );
			data.setTrainCoach( trainCoach );
			*/
			/*
			EntityManagerFactory emf = Persistence.createEntityManagerFactory( "EJBProject" );
			EntityManager em = emf.createEntityManager();
			
			EntityTransaction tx = em.getTransaction();
			
			tx.begin();
			em.persist( trainCoach );
			em.flush();
			em.persist( data );
			tx.commit();
		
			emf.close();
			em.close();
			*/
		}
		
		catch ( IOException io )
		{
			io.printStackTrace();
		}
		proxy.disconnect();
	}

	private void writeLine( String line, boolean comma, BufferedWriter writer ) throws IOException
	{
		if ( comma ) writer.write( line + "," + System.getProperty( "line.separator" ) );
		else writer.write( line + System.getProperty( "line.separator" ) );
	}

	private void writeArray2D( String name, double[][] array, boolean comma, BufferedWriter writer ) throws IOException
	{
		writer.write( "\"" + name + "\": [ " );
		for ( int i = 0; i < array.length; i++ )
		{
			writer.write( "" + array[i][0] );
			if ( i != array.length - 1 ) writer.write( "," );
		}
		writeLine( "]", comma, writer );
	}

	private void writeNumericArray( String name, MatlabNumericArray array, boolean comma, BufferedWriter writer )
			throws IOException
	{
		writer.write( "\"" + name + "\": [ " );
		for ( int i = 0; i < array.getLength(); i++ )
		{
			writer.write( "" + array.getRealValue( i ) );
			if ( i != array.getLength() - 1 ) writer.write( "," );
		}
		writeLine( "]", comma, writer );
	}

	private String writeValue( String name, double value )
	{
		return "\"" + name + "\":" + value;
	}

}
