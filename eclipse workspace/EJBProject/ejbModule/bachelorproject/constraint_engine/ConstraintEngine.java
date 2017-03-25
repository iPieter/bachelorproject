package bachelorproject.constraint_engine;

import java.util.List;

import javax.inject.Inject;

import org.jboss.dmr.ModelType;

import bachelorproject.ejb.ConstraintEJB;
import bachelorproject.model.Issue;
import bachelorproject.model.TrainCoach;
import bachelorproject.model.constraint_engine.Constraint;
import bachelorproject.model.constraint_engine.ConstraintElement;
import bachelorproject.model.constraint_engine.LocationConstraintElement;
import bachelorproject.model.constraint_engine.ModelTypeConstraintElement;
import bachelorproject.model.constraint_engine.ValueConstraintElement;

/**
 * 	This class tests the supplied data for all the constraints and generates new Issues
 *  if the constraint return true.
 *  @author Anton Danneels
 * */
public class ConstraintEngine
{
	@Inject
	private ConstraintEJB constraintEJB;
	
	private final int ID;
	private ConstraintEngineFactory factoryParent;
	
	private TrainCoach currentTraincoach;
	private Issue issue;
	private List<Constraint> constraints;
	
	public ConstraintEngine( ConstraintEngineFactory parent, int ID )
	{
		this.ID = ID;
		factoryParent = parent;
	}
	
	public void start( TrainCoach trainCoach )
	{
		currentTraincoach = trainCoach;
		constraints = constraintEJB.getAllConstraints();
	}
	
	/**
	 * 	Processes all the constraints on target data
	 *  <p>
	 *  This method will test all constraints and generate a
	 *  new Issue object if needed.
	 * */
	public void addData( ConstraintEngineData data )
	{
		for( Constraint c : constraints )
		{
			boolean isIssue = true;
			for( ConstraintElement ce : c.getConstraints() )
				isIssue = isIssue && ce.visit( this );
		}
	}
	
	public boolean visit( LocationConstraintElement lce )
	{
		return true;
	}
	
	public boolean visit( ModelTypeConstraintElement mtce )
	{
		return true;
	}
	
	public boolean visit( ValueConstraintElement vce )
	{
		return true;
	}
	
	/**
	 * 	Indicates that an engine is closed. Any call to this object after
	 *  this method will be undefined behavior. This method will also add
	 *  any new issue's to the database.
	 * */
	public void stop()
	{
		currentTraincoach = null;
		issue = null;
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
