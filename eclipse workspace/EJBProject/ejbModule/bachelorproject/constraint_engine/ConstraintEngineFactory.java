package bachelorproject.constraint_engine;

import java.util.LinkedList;

import javax.annotation.PostConstruct;
import javax.ejb.Startup;

/**
 * 	This class is used to reliably supply multiple ConstraintEngine objects.
 *  It manages a pool of ConstraintEngine objects so that these objects are reused
 *  and the impact on the garbage collector is reduced
 *  @author Anton Danneels
 * */
@Startup
public class ConstraintEngineFactory
{
	public static final int CEF_SIZE = 100;
	private ConstraintEngine [] constraintEngines;
	private LinkedList<Integer> freeEngines;
	
	@PostConstruct
	public void init()
	{
		constraintEngines = new ConstraintEngine[ CEF_SIZE ];
		freeEngines = new LinkedList<>();
		for( int i = 0; i < CEF_SIZE; i++ )
		{
			constraintEngines[ i ] = new ConstraintEngine( this, i );
			freeEngines.add( i );
		}
	}
	
	/**
	 * 	Returns the first free ConstraintEngine if one is free.
	 *  @throws OutOfConstraintEngineException
	 *  @return A free ContraintEngine object.
	 * */
	public ConstraintEngine getConstraintEngine() throws OutOfConstraintEngineException
	{
		if( freeEngines.size() == 0 )
			throw new OutOfConstraintEngineException();
		
		int free = freeEngines.removeFirst();
		return constraintEngines[ free ];
	}
	
	/**
	 *  Returns a ConstraintEngine object to the free pool. 
	 *  This method should not be called by any outside objects but is
	 *  intended for ConstraintEngine's to return themselves.
	 */
	public void returnConstraintEngine( ConstraintEngine engine )
	{
		freeEngines.add( engine.getID() );
	}
}
