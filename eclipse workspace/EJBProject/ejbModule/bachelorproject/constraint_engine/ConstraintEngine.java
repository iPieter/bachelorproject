package bachelorproject.constraint_engine;

import bachelorproject.model.TrainCoach;

/**
 * 	This class tests the supplied data for all the constraints and generates new Issues
 *  if the constraint return true.
 *  @author Anton Danneels
 * */
public class ConstraintEngine
{
	private final int ID;
	private ConstraintEngineFactory factoryParent;
	
	public ConstraintEngine( ConstraintEngineFactory parent, int ID )
	{
		this.ID = ID;
		factoryParent = parent;
	}
	
	public void start( TrainCoach trainCoach )
	{
		
	}
	
	/**
	 * 	Processes all the constraints on target data
	 *  <p>
	 *  This method will test all constraints and generate a
	 *  new Issue object if needed.
	 * */
	public void addData( ConstraintEngineData data )
	{
		
	}
	
	/**
	 * 	Indicates that an engine is closed. Any call to this object after
	 *  this method will be undefined behavior. This method will also add
	 *  any new issue's to the database.
	 * */
	public void stop()
	{
		factoryParent.returnConstraintEngine( this );
	}

	/**
	 * @return the iD
	 */
	public int getID()
	{
		return ID;
	}
}
