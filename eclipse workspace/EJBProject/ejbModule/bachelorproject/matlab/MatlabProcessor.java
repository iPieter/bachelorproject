package bachelorproject.matlab;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import bachelorproject.model.ProcessedSensorData;
import bachelorproject.model.TrainCoach;
import bachelorproject.model.Workplace;
import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;
import matlabcontrol.MatlabProxyFactoryOptions;
import matlabcontrol.extensions.MatlabNumericArray;
import matlabcontrol.extensions.MatlabTypeConverter;

@Singleton
@Startup
public class MatlabProcessor
{
	String script = "";
	File matlabDirectory = new File( System.getProperty( "user.home" ) + "/project_televic/matlab_files" );

	@PostConstruct
	public void init()
	{
		loadScript();
		testFolderAndDatabase();
	}

	@Schedule( hour = "*", minute = "*/5" )
	public void testFolderAndDatabase()
	{
		System.out.println( "MATLABPROCESSOR: SCANNING FOR NEW FILES" );
		EntityManagerFactory emf = Persistence.createEntityManagerFactory( "EJBProject" );
		EntityManager em = emf.createEntityManager();

		List<ProcessedSensorData> result = new ArrayList<>();

		try
		{
			TypedQuery<ProcessedSensorData> query = em.createNamedQuery( "ProcessedSensorData.findAll",
					ProcessedSensorData.class );
			result = query.getResultList();
		}
		catch ( Exception e )
		{
			System.out.println( "ERROR IN MATLABPROCESSOR(query):" + e.getLocalizedMessage() );
		}

		ArrayList<String> filesToBeAdded = new ArrayList<>();

		for ( File f : matlabDirectory.listFiles() )
		{
			if ( !f.isDirectory() )
			{
				String fileName = f.getName();
				String split[] = fileName.split( "\\." );
				if ( split.length == 2 && split[1].equals( "mat" ) )
				{
					boolean exists = false;
					for ( ProcessedSensorData data : result )
					{
						if ( data.getLocation().contains( split[0] ) )
						{
							exists = true;
							break;
						}
					}
					if ( !exists ) filesToBeAdded.add( fileName );
				}
			}
		}

		for ( String s : filesToBeAdded )
		{
			try
			{
				analyseFile( s, em );
			}
			catch ( IOException | MatlabConnectionException | MatlabInvocationException e )
			{
				System.out.println( "ERROR IN MATLABPROCESSOR(analysis): " + e.getLocalizedMessage() );
			}
		}

		em.close();
		emf.close();
	}

	private void loadScript()
	{
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
	}

	private void analyseFile( String name, EntityManager em )
			throws MatlabConnectionException, MatlabInvocationException, IOException
	{
		MatlabProxyFactoryOptions options = new MatlabProxyFactoryOptions.Builder().setHidden( true ).build();
		MatlabProxyFactory factory = new MatlabProxyFactory( options );
		MatlabProxy proxy = factory.getProxy();

		String path = matlabDirectory + "/" + name;
		// System.out.println( path );

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

		try
		{
			String outputPath = matlabDirectory + "/" + name.split( "\\." )[0] + ".json";

			BufferedWriter writer = new BufferedWriter( new FileWriter( outputPath ) );
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
			writeToDatabase( name, em );
		}
		catch ( IOException io )
		{
			io.printStackTrace();
		}
		proxy.exit();
	}

	private void writeToDatabase( String fileName, EntityManager em )
	{
		// System.out.println( "WRITING TO DB" );
		String split[] = fileName.split( "\\." );
		String outputPath = matlabDirectory + "/" + split[0] + ".json";

		String nameSplit[] = split[0].split( "_" );

		// for( String s : nameSplit )
		// System.out.println( s );

		List<TrainCoach> traincoachResult = new ArrayList<>();
		List<Workplace> workplaceResult = new ArrayList<>();

		try
		{
			TypedQuery<TrainCoach> query = em.createNamedQuery( TrainCoach.FIND_BY_DATA, TrainCoach.class );
			query.setParameter( "name", nameSplit[1] );
			query.setParameter( "type", nameSplit[2] );
			query.setParameter( "constructor", nameSplit[3] );
			traincoachResult = query.getResultList();

			TypedQuery<Workplace> workplaceQuery = em.createNamedQuery( "Workplace.findByData", Workplace.class );
			workplaceQuery.setParameter( "name", nameSplit[0] );
			workplaceResult = workplaceQuery.getResultList();
		}
		catch ( Exception e )
		{
			System.out.println( "ERROR IN MATLABPROCESSOR(query traincoach):" + e.getLocalizedMessage() );
		}

		EntityTransaction tx = em.getTransaction();
		tx.begin();

		TrainCoach trainCoach;
		if ( traincoachResult.size() == 0 )
		{
			trainCoach = new TrainCoach();
			trainCoach.setConstructor( nameSplit[1] );
			trainCoach.setType( nameSplit[2] );
			trainCoach.setName( nameSplit[3] );
		}
		else trainCoach = traincoachResult.get( 0 );

		Workplace workplace;
		if ( workplaceResult.size() == 0 )
		{
			workplace = new Workplace();
			workplace.setName( nameSplit[0] );
		}
		else workplace = workplaceResult.get( 0 );

		if ( workplace.getTraincoaches() != null && !workplace.getTraincoaches().contains( trainCoach ) )
		{
			if ( workplace.getTraincoaches() != null ) workplace.getTraincoaches().add( trainCoach );
		}

		ProcessedSensorData data = new ProcessedSensorData();
		data.setLocation( outputPath );
		data.setTime( new Date() );
		data.setTrack( nameSplit[4] );
		data.setTrainCoach( trainCoach );

		em.persist( trainCoach );
		em.persist( data );
		em.persist( workplace );
		tx.commit();
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
