package bachelorproject.constraint_engine;

import java.util.List;

import javax.inject.Inject;

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
	
	//Objects needed for the constraint engine to work
	private final int ID;
	private ConstraintEngineFactory factoryParent;
	private ConstraintEngineLocationTester locationTester;
	private ConstraintEngineValueTester valueTester;
	private ConstraintEngineData ceData;
	private String currentIssueDescription;
	
	//Objects from persistence context
	private TrainCoach currentTraincoach;
	private Issue currentIssue;
	private List<Issue> issues;
	private List<Constraint> constraints;
	
	public ConstraintEngine( ConstraintEngineFactory parent, int ID )
	{
		this.ID = ID;
		factoryParent = parent;
		locationTester = new ConstraintEngineLocationTester();
		valueTester = new ConstraintEngineValueTester();
		currentIssueDescription = "";
	}
	
	public void start( TrainCoach trainCoach )
	{
		currentTraincoach = trainCoach;
		constraints = constraintEJB.getAllConstraints();
		currentIssue = null;
		currentIssueDescription = "";
	}
	
	/**
	 * 	Processes all the constraints on target data
	 *  <p>
	 *  This method will test all constraints and generate a
	 *  new Issue object if needed.
	 * */
	public void addData( ConstraintEngineData data )
	{
		ceData = data;

		for( Constraint c : constraints )
		{
			boolean isIssue = true;
			currentIssue = new Issue();
			for( ConstraintElement ce : c.getConstraints() )
			{
				isIssue = isIssue && ce.visit( this );
			}
			if( isIssue )
			{
				currentIssue.setDescr( currentIssueDescription );
				issues.add( currentIssue );
			}
		}
	}
	
	public boolean visit( LocationConstraintElement lce )
	{
		if( locationTester.isPointInPolygon( lce.getPolygon(), ceData.getLat(), ceData.getLng() ) )
		{
			currentIssueDescription += "In lokatie: " + ceData.getLat() + "," + ceData.getLng() + System.getProperty( "line.separator" );
			currentIssue.setGpsLat( ceData.getLat() );
			currentIssue.setGpsLon( ceData.getLng() );
			return true;
		}
		return false;
	}
	
	public boolean visit( ModelTypeConstraintElement mtce )
	{
		if( mtce.getType().equals( currentTraincoach.getType() ) )
		{
			currentIssueDescription += "Voor: " + currentTraincoach.getType() + "-" + currentTraincoach.getName() + System.getProperty( "line.separator" );
			return true;
		}
		return false;
	}
	
	public boolean visit( ValueConstraintElement vce )
	{
		switch ( vce.getValueConstraintAttribute() )
		{
			case YAW:
				if( valueTester.testValueConstraint( vce.getType(), vce.getMaxValue(), ceData.getYaw() ) )
				{
					currentIssueDescription += "Waarde overschreven voor sensor: gyroscoop-yaw : " + ceData.getYaw();
					return true;
				}
				break;
			case ROLL:
				if( valueTester.testValueConstraint( vce.getType(), vce.getMaxValue(), ceData.getRoll() ) )
				{
					currentIssueDescription += "Waarde overschreven voor sensor: gyroscoop-roll : " + ceData.getRoll();
					return true;
				}
				break;
			case SPEED:
				if( valueTester.testValueConstraint( vce.getType(), vce.getMaxValue(), ceData.getSpeed() ) )
				{
					currentIssueDescription += "Waarde overschreven voor sensor: snelheidsmeter : " + ceData.getSpeed();
					return true;
				}
				break;
			case ACCEL:
				if( valueTester.testValueConstraint( vce.getType(), vce.getMaxValue(), ceData.getAccel() ) )
				{
					currentIssueDescription += "Waarde overschreven voor sensor: versnellingsmeter : " + ceData.getAccel();
					return true;
				}
				break;
		}
		return false;
	}
	
	public void printStatusReport()
	{
		for( Issue i : issues )
		{
			System.out.println( "============================================" );
			System.out.println( i.getDescr() );
			System.out.println( "============================================" );
		}
	}
	
	/**
	 * 	Indicates that an engine is closed. Any call to this object after
	 *  this method will be undefined behavior. This method will also add
	 *  any new issue's to the database.
	 * */
	public void stop()
	{
		currentTraincoach = null;
		currentIssue = null;
		issues.clear();
		ceData = null;
		constraints.clear();
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
