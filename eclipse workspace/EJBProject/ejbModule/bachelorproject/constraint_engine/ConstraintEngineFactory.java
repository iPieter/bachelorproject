package bachelorproject.constraint_engine;

import java.util.Arrays;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import bachelorproject.ejb.ConstraintEJB;

/**
 * 	This class is used to reliably supply multiple ConstraintEngine objects.
 *  It manages a pool of ConstraintEngine objects so that these objects are reused
 *  and the impact on the garbage collector is reduced
 *  @author Anton Danneels
 * */
@Startup
@Singleton
public class ConstraintEngineFactory
{
	@Inject
	private ConstraintEJB constraintEJB;
	
	public static final int CEF_SIZE = 100;
	private ConstraintEngine [] constraintEngines;
	private boolean [] freeEngines;
	
	@PostConstruct
	public void init()
	{
		constraintEngines = new ConstraintEngine[ CEF_SIZE ];
		freeEngines = new boolean[ CEF_SIZE ];
		for( int i = 0; i < CEF_SIZE; i++ )
		{
			constraintEngines[ i ] = new ConstraintEngine( this, i );
			freeEngines[ i ] = true;
		}
	}
	
	/**
	 * 	Returns the first free ConstraintEngine if one is free.
	 *  @throws OutOfConstraintEngineException
	 *  @return A free ContraintEngine object.
	 * */
	public ConstraintEngine getConstraintEngine() throws OutOfConstraintEngineException
	{
		for( int i = 0; i < freeEngines.length; i++ )
		{
			if( freeEngines[i] )
			{
				freeEngines[i] = false;
				return constraintEngines[i];
			}
		}
		throw new OutOfConstraintEngineException();
	}
	
	/**
	 *  Returns a ConstraintEngine object to the free pool. 
	 *  This method should not be called by any outside objects but is
	 *  intended for ConstraintEngine's to return themselves.
	 */
	public void returnConstraintEngine( int ID )
	{
		freeEngines[ ID ] = true;
	}

	public ConstraintEJB getConstraintEJB()
	{
		return constraintEJB;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode( constraintEngines );
		result = prime * result + Arrays.hashCode( freeEngines );
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals( Object obj )
	{
		if ( this == obj ) return true;
		if ( obj == null ) return false;
		if ( getClass() != obj.getClass() ) return false;
		ConstraintEngineFactory other = (ConstraintEngineFactory) obj;
		if ( !Arrays.equals( constraintEngines, other.constraintEngines ) ) return false;
		if ( !Arrays.equals( freeEngines, other.freeEngines ) ) return false;
		return true;
	}
}
