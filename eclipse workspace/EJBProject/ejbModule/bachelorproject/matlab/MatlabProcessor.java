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

/**	Searches the folder /project_televic/matlab_files/ in the users home folder and processes them.
 *  The resulting JSON file is stored in the same folder and entries are added in the database according
 *  to a specific naming structure.
 *  <p> 
 * 	We assume that the .mat files dropped in the above specified folder have a certain filestructure and name.
 *  The name should follow the following format: 
 *  <code>*name workplace*_*name constructor*_*name type*_*name*_*name track*.mat</code><br>
 *	Where:
 * <ul>
 *		<li>Name workplace: the current position of the traincoach where it needs to be inspected</li>
 *		<li>Name constructor: the builder of the traincoach</li>
 *	 	<li>Name type: the type of the traincoach</li>
 *	 	<li>Name: A unique identifier for this specific traincoach</li>
 *	 	<li>Name track: the start and destination of the ride, seperated by a "-" </li>
 *	</ul>
 *  Concrete example: GENTSP_BOMBARDIER_M7_78558_Oostende-GentSP.mat<br>
 *  After the data is processed this class will produce a file with the following data:
 *  <ul>
 *  	<li>The time(yy-mm-dd:hh-mm) of the trip</li>
 *  	<li>An array with the maximum yaw values of the trip</li>
 *  	<li>An array with the maximum roll values of the trip</li>
 *  	<li>An array with the lat values of the trip</li>
 *  	<li>An array with the lng values of the trip</li>
 *  </ul>
 *  Because of the naming convention, we are able to wire everything in the database together.
 *  @author: Anton Danneels
 *  @version: 1.0.0
 * */
@Singleton
@Startup
public class MatlabProcessor
{
	/** Stores the matlab script which will be executed, loaded on startup. */
	private String script = "";
	/** Stores the location of the matlab directory files. */
	private File matlabDirectory = new File( System.getProperty( "user.home" ) + "/project_televic/matlab_files" );

	/**
	 * 	Loads the script and tests the matlab folder on startup.
	 *  @see testFolderAndDatabase()
	 * */
	@PostConstruct
	public void init()
	{
		loadScript();
		testFolderAndDatabase();
	}

	/**
	 * 	Tests the matlab folder every 5 minutes for new files and processes them.
	 *  <p>
	 *  This method compares the content of the processedsensordata table and the files
	 *  present in the matlab folder. If there are any files not in the table they will get
	 *  processed. This method executes every 5 minutes.
	 * */
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

	/**
	 * 	Loads the matlabscript stored in the classpath.
	 *  <p>
	 *  The script contains the processing functions to convert the .mat file to a more
	 *  suitable format for displaying on the web.
	 * */
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

	/**
	 * 	Loads the matlab file, executes the script on it and writes an output JSON file.
	 *  <p>
	 *  Constructs a connection to MatLab, loads the .mat file with the given path, then executes the script.
	 *  If no errors occured it will retrieve the data from MatLab and write it to a JSON file.
	 *  @param name The path to a .mat file with a correct naming convention
	 *  @param em A valid(open) EntityManager object to write the objects to the database
	 * */
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

	/**
	 * 	Creates objects based on the filename and writes these to the database.
	 *  <p>
	 * 	Based on the naming convention described above, this method will write
	 *  objects to the database. If for example an object already exists (e.g. workplace)
	 *  it will append to it.
	 *  @param fileName A correct filename.
	 *  @param em A valid(open) EntityManager object.
	 * */
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

	/**
	 * 	Writes a JSON line to an open BufferedWriter.
	 *  <p>
	 *  Based on the line and comma parameters this method will write
	 *  a line to a BufferedWriter object.
	 *  @param line The line to be written
	 *  @param comma Indicates whether or not the line should be appended with a comma
	 *  @param writer An open BufferedWriter.
	 *  @throws IOException
	 * */
	private void writeLine( String line, boolean comma, BufferedWriter writer ) throws IOException
	{
		if ( comma ) writer.write( line + "," + System.getProperty( "line.separator" ) );
		else writer.write( line + System.getProperty( "line.separator" ) );
	}

	/**
	 * 	Writes a JSON array to an open BufferedWriter.
	 *  <p>
	 *  Writes a 2 dimensional array in JSON format to a BufferedWriter.
	 *  @param name The name of the array that should be written.
	 *  @param array A 2 dimensional array.
	 *  @param comma Indicates whether or not the line should be appended with a comma
	 *  @param writer An open BufferedWriter.
	 *  @throws IOException
	 * */
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

	/**
	 * 	Writes a MatlabNumericArray in JSON format to an open BufferedWriter.
	 *  <p>
	 *  Writes a MatlabNumericArray in JSON format to an open BufferedWriter.
	 *  @param name The name of the array that should be written.
	 *  @param array A numeric MatLab array.
	 *  @param comma Indicates whether or not the line should be appended with a comma
	 *  @param writer An open BufferedWriter.
	 *  @throws IOException
	 * */
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

	/**
	 * 	Writes a double value in JSON format to an open BufferedWriter.
	 *  <p>
	 *  Writes a double value in JSON format to an open BufferedWriter.
	 *  @param name The name of the value that should be written.
	 *  @param value The actual value that should be written.
	 * */
	private String writeValue( String name, double value )
	{
		return "\"" + name + "\":" + value;
	}

}
