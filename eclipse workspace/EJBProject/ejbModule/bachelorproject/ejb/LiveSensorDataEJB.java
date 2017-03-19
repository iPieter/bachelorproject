package bachelorproject.ejb;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import bachelorproject.model.LiveSensorData;
import bachelorproject.model.LiveSensorDataEntry;
import bachelorproject.model.ProcessedSensorData;
import bachelorproject.model.TrainCoach;
import bachelorproject.model.Workplace;

/**
 * Defines the Entity Java Bean for the LiveSensorData Entity.
 * <p>
 * This class allows for the controller to manipulate and fetch specific LiveSensorData
 * instances. It validates new objects and handles errors when, for example, no
 * entries in the database exist.
 * @author Anton Danneels
 * @see LiveSensorData
 */
@Named
@Stateless
public class LiveSensorDataEJB
{
	@Inject
	private EntityManagerSingleton ems;
	@Inject
	private WorkplaceEJB workplaceEJB;
	
	/**
	 * 	Persists a LiveSensorData object to the database if it is correct
	 *  @param lsd The LiveSensorDataObject to be persisted
	 * */
	public int createLiveSensorData( LiveSensorData lsd )
	{
		EntityManager em = ems.getEntityManager();
		em.getTransaction().begin();
		
		em.persist( lsd );
		
		em.getTransaction().commit();
		em.close();
		
		return lsd.getId();
	}
	
	/**
	 * 	Closes the LiveSensorData specified by lsdID.
	 *  <p>
	 *  When this method gets called, it is a signal that the ride has ended.
	 *  This function closes the live sensor data object, generates a new JSON 
	 *  file from this and adds a new ProcessedSensorData object and deletes all
	 *  entries from this ride.
	 *  @param lsdID The LiveSensorDataObject
	 * */
	public void closeLiveSensorData( int lsdID )
	{
		EntityManager em = ems.getEntityManager();
		em.getTransaction().begin();
		
		LiveSensorData sensordata = em.find( LiveSensorData.class, lsdID );
		
		if( sensordata != null )
		{
			sensordata.setLive( false );
			
			List<Double> lat = new ArrayList<Double>();
			List<Double> lng = new ArrayList<Double>();
			List<Double> yaw = new ArrayList<Double>();
			List<Double> roll = new ArrayList<Double>();
			List<Double> speed = new ArrayList<Double>();
			List<Double> accel = new ArrayList<Double>();
			double maxYaw = 0.0;
			double minYaw = 0.0;
			double maxRoll = 0.0;
			double minRoll = 0.0;
			
			Calendar calendar = new GregorianCalendar();
			calendar.setTime( sensordata.getDate() );
			double year = calendar.get( Calendar.YEAR );
			double month = calendar.get( Calendar.MONTH );
			double day = calendar.get( Calendar.DATE );
			double hour = calendar.get( Calendar.HOUR );
			double minute = calendar.get( Calendar.MINUTE );
			double second = calendar.get( Calendar.SECOND );
			
			double lat_off = 0.0;
			double lng_off = 0.0;
			
			for( LiveSensorDataEntry lsde : sensordata.getEntries() )
			{
				lat.add( lsde.getLat() );
				lng.add( lsde.getLng() );
				yaw.add( lsde.getYaw() );
				roll.add( lsde.getRoll() );
				speed.add( lsde.getSpeed() );
				accel.add( lsde.getAccel() );
				
				if( lsde.getYaw() < minYaw )
					minYaw = lsde.getYaw();
				if( lsde.getYaw() > maxYaw )
					maxYaw = lsde.getYaw();
				if( lsde.getRoll() < minRoll )
					minRoll = lsde.getRoll();
				if( lsde.getRoll() > maxRoll )
					maxRoll = lsde.getRoll();
			}
			
			SimpleDateFormat format = new SimpleDateFormat( "yyyy_MM_dd_HH_mm_ss" );
			String fileName = sensordata.getTraincoach().getType() + "_" + sensordata.getTraincoach().getName() 
							  + "_" + format.format( sensordata.getDate() ) + ".json";
			String path = System.getProperty( "user.home" ) + "/project_televic/matlab_files/" + fileName;

			try
			{
				BufferedWriter writer = new BufferedWriter( new FileWriter( path ) );
				writer.write( "{" + System.getProperty( "line.separator" ) ) ;
				writer.write( writeValue( "year", year, true ) );
				writer.write( writeValue( "month", month, true ) );
				writer.write( writeValue( "day", day, true ) );
				writer.write( writeValue( "hour", hour, true ) );
				writer.write( writeValue( "minute", minute, true ) );
				writer.write( writeValue( "second", second, true ) );
				
				writer.write( writeValue( "lat_off", lat_off, true ) );
				writer.write( writeValue( "lng_off", lng_off, true ) );
				
				writer.write( writeValue( "max_yaw", maxYaw, true ) );
				writer.write( writeValue( "min_yaw", minYaw, true ) );
				writer.write( writeValue( "max_roll", maxRoll, true ) );
				writer.write( writeValue( "min_roll", minRoll, true ) );
				
				writer.write( writeArrayList( "yaw", yaw, true ) );
				writer.write( writeArrayList( "roll", roll, true ) );
				writer.write( writeArrayList( "lat", lat, true ) );
				writer.write( writeArrayList( "lng", lng, true ) );
				writer.write( writeArrayList( "speed", speed, true ) );
				writer.write( writeArrayList( "accel", accel, false ) );
				
				writer.write( "}" );
				writer.flush();
				writer.close();
			}
			catch ( IOException e )
			{
				e.printStackTrace();
			}
			
			ProcessedSensorData data = new ProcessedSensorData();
			data.setLocation( path );
			data.setTime( new Date() );
			data.setTrack( sensordata.getTrack() );
			data.setTraincoach( sensordata.getTraincoach() );
			
			boolean newWorkplace = false;
			List<Workplace> workplaces = workplaceEJB.getAllWorkplaces();
			Workplace workplace = null;
			
			String split [] = sensordata.getTrack().split( "-" );
			String workplaceName = split[1];
			
			TrainCoach trainCoach = sensordata.getTraincoach();
			
			//Note(Anton): this can be optimized
			for( Workplace p : workplaces )
			{
				if( p.getName().toLowerCase().equals( workplaceName.toLowerCase() ) )
					workplace = p;
				
				Iterator<TrainCoach> iterator = p.getTraincoaches().iterator();
				while( iterator.hasNext() )
				{
					TrainCoach t = iterator.next();
					if( t.getId() == trainCoach.getId() )
					{
						iterator.remove();
						em.merge( p );
					}
				}
			}
			if( workplace == null )
			{
				workplace = new Workplace();
				workplace.setName( workplaceName );
				newWorkplace = true;
			}
			
			workplace.getTraincoaches().add( trainCoach );
			trainCoach.setNeedsReview( true );
			
			em.persist( data );
			em.merge( trainCoach );
			
			if( newWorkplace )
				em.persist( workplace );
			else
				em.merge( workplace );
			
			for( LiveSensorDataEntry lsde : sensordata.getEntries() )
				em.remove( lsde );
			
			sensordata.getEntries().clear();
			
			em.merge( sensordata );
			em.remove( sensordata );
		}
		
		em.getTransaction().commit();
		em.close();
	}
	
	//Note( Anton) this method should probably be in a seperate util package.
	private String writeArrayList( String name, List<Double> list, boolean comma )
	{
		String newLine = System.getProperty( "line.separator" );
		StringBuffer buffer = new StringBuffer();
		
		buffer.append( "\"" + name + "\": [" + newLine );
		for( int i = 0; i < list.size(); i++ )
		{
			buffer.append( list.get( i ) );
			if( i != list.size() - 1 )
				buffer.append( "," );
		}
		buffer.append( "]" );
		if( comma )
			buffer.append( "," );
		buffer.append( newLine );
		
		return buffer.toString();
	}
	
	private String writeValue( String name, double value, boolean comma )
	{
		String result = "";
		
		result += "\"" + name + "\":" + value;
		
		if( comma )
			result += ",";
		result += System.getProperty( "line.separator" );
		
		return result;
	}
	
	/**
	 * 	Finds a LiveSensorDataEntry and returns it if it exists
	 *  @param lsdID The object that needs to be found
	 * */
	public LiveSensorData findLSDByID( int lsdID )
	{
		EntityManager em = ems.getEntityManager();
		em.getTransaction().begin();
		
		LiveSensorData sensordata = em.find( LiveSensorData.class, lsdID );
		
		em.getTransaction().commit();
		em.close();
	
		return sensordata;
	}
	
	/**
	 * 	Finds a LiveSensorDataEntry and returns it if it exists
	 *  @param lsdID The object that needs to be found
	 * */
	public LiveSensorData findLSDByDate( Date date )
	{
		EntityManager em = ems.getEntityManager();
		em.getTransaction().begin();
		
		TypedQuery<LiveSensorData> query = em.createNamedQuery( LiveSensorData.FIND_BY_DATE, LiveSensorData.class );
		query.setParameter( "date", date, TemporalType.TIMESTAMP );
		
		List<LiveSensorData> result = query.getResultList();
		
		em.getTransaction().commit();
		em.close();
	
		if( result.size() == 0 )
			return null;
		return result.get( 0 );
	}
	
	/**
	 * 	Returns a list of all active sensors.
	 *  @return The list of all active sensors
	 * */
	public List<LiveSensorData> getAllActiveSensorObjects()
	{
		EntityManager em = ems.getEntityManager();
		em.getTransaction().begin();
		
		TypedQuery<LiveSensorData> query = em.createNamedQuery( LiveSensorData.FIND_ALL_ACTIVE, LiveSensorData.class );
		
		List<LiveSensorData> result = query.getResultList();
		
		em.getTransaction().commit();
		em.close();
	
		return result;
	}
}
