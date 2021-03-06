package bachelorproject.constraint_engine;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.annotation.PostConstruct;

import bachelorproject.ejb.ConstraintEJB;
import bachelorproject.ejb.IssueAssetEJB;
import bachelorproject.ejb.IssueEJB;
import bachelorproject.model.constraint_engine.Constraint;
import bachelorproject.model.constraint_engine.ConstraintElement;
import bachelorproject.model.constraint_engine.LocationConstraintElement;
import bachelorproject.model.constraint_engine.ModelTypeConstraintElement;
import bachelorproject.model.constraint_engine.ValueConstraintElement;
import bachelorproject.model.issue.Issue;
import bachelorproject.model.issue.IssueAsset;
import bachelorproject.model.issue.IssueStatus;
import bachelorproject.model.sensordata.SensorData;

/**
 * This class tests the supplied data for all the constraints and generates new
 * Issues if the constraint return true.
 * 
 * @author Anton Danneels
 */
public class ConstraintEngine
{
	private ConstraintEJB constraintEJB;
	private IssueEJB issueEJB;
	private IssueAssetEJB assetEJB;

	// Objects needed for the constraint engine to work
	private final int ID;
	private ConstraintEngineFactory factoryParent;
	private ConstraintEngineLocationTester locationTester;
	private ConstraintEngineValueTester valueTester;
	private ConstraintEngineData ceData;
	private String currentIssueDescription;
	private HashSet<Constraint> usedConstraints;

	// Objects from persistence context
	private SensorData data;
	private Issue currentIssue;
	private List<Issue> issues;
	private List<Constraint> constraints;

	/**
	 * Initializes a new ConstraintEngine
	 * 
	 * @param parent
	 *            CE's should not be created on their own, this parameter is a
	 *            link to a ConstraintEngineFactory
	 * @param ID
	 *            A unique ID for this ConstraintEngine
	 */
	public ConstraintEngine(ConstraintEngineFactory parent, int ID)
	{
		this.ID = ID;
		factoryParent = parent;
		locationTester = new ConstraintEngineLocationTester();
		valueTester = new ConstraintEngineValueTester();
		currentIssueDescription = "";
		constraintEJB = parent.getConstraintEJB();
		issueEJB = parent.getIssueEJB();
		assetEJB = parent.getIssueAssetEJB();
		issues = new ArrayList<>();
		usedConstraints = new HashSet<>();
	}

	/**
	 * Allows the Constraint Engine to start its work. It resets all data &
	 * prepares to test new constraints
	 * 
	 * @param data
	 *            The SensorData object that needs to be tested.
	 */
	@PostConstruct
	public void start( SensorData data )
	{
		this.data = data;
		constraints = constraintEJB.getAllConstraints();
		currentIssue = null;
		currentIssueDescription = "";

		List<Issue> issues = issueEJB.getIssuesBySensorDataID( data.getId() );
		for ( Issue i : issues )
		{
			for ( Constraint c : constraints )
			{
				if ( i.getDescr().contains( c.getName() ) )
				{
					usedConstraints.add( c );
				}
			}
		}
	}

	/**
	 * Processes all the constraints on target data
	 * <p>
	 * This method will test all constraints and generate a new Issue object if
	 * needed.
	 */
	public void addData( ConstraintEngineData data )
	{
		ceData = data;

		for ( Constraint c : constraints )
		{
			if ( !usedConstraints.contains( c ) )
			{
				boolean isIssue = true;
				currentIssue = new Issue();
				currentIssue.setGpsLat( 0 );
				currentIssue.setGpsLon( 0 );
				currentIssue.setAssignedTime( new Date() );
				currentIssue.setData( this.data );
				currentIssue.setStatus( IssueStatus.CREATED );
				currentIssue.setOperator( c.getCreator() );
				currentIssueDescription = c.getName() + System.getProperty( "line.separator" );

				for ( ConstraintElement ce : c.getConstraints() )
					isIssue = isIssue && ce.visit( this );

				if ( isIssue )
				{
					currentIssue.setDescr( c.getName() );
					IssueAsset asset = new IssueAsset();
					asset.setDescr( currentIssueDescription );
					asset.setLocation( "" );
					asset.setTime( new Date() );
					asset.setUser( c.getCreator() );

					issues.add( currentIssue );
					issueEJB.createIssue( currentIssue );
					assetEJB.createIssueAsset( asset );
					issueEJB.addAsset( asset, currentIssue.getId() );

					System.out.println( "============================================" );
					System.out.println( currentIssueDescription );
					System.out.println( "============================================" );
					usedConstraints.add( c );
				}
			}
		}
	}

	/**
	 * The ConstraintEngine uses a visitor pattern to evaluate all the
	 * Constraints. This method will test if a LocationConstraintElement if met
	 * by testing if a point is in a polygon. @see{LocationTester}
	 * 
	 * @param lce
	 *            A LocationConstraintElement containing a polygon.
	 * @return true if the point is in the polygon, false otherwise
	 */
	public boolean visit( LocationConstraintElement lce )
	{
		if ( locationTester.isPointInPolygon( lce.getPolygon(), ceData.getLat(), ceData.getLng() ) )
		{
			currentIssueDescription += "In locatie: " + ceData.getLat() + "," + ceData.getLng()
					+ System.getProperty( "line.separator" );
			currentIssue.setGpsLat( ceData.getLat() );
			currentIssue.setGpsLon( ceData.getLng() );
			return true;
		}
		return false;
	}

	/**
	 * The ConstraintEngine uses a visitor pattern to evaluate all the
	 * Constraints. This method will test if a @see{TrainCoach} has a specific
	 * type.
	 * 
	 * @param mtce
	 *            A ModelTypeConstraintElement that contains the type that
	 *            should be tested.
	 * @return true if a model is of a certain type, false otherwise
	 */
	public boolean visit( ModelTypeConstraintElement mtce )
	{
		if ( mtce.getModelType().equals( data.getTraincoach().getType() ) )
		{
			currentIssueDescription += "Voor: " + data.getTraincoach().getType() + "-" + data.getTraincoach().getName()
					+ System.getProperty( "line.separator" );
			return true;
		}
		return false;
	}

	/**
	 * The ConstraintEngine uses a visitor pattern to evaluate all the
	 * Constraints. This method will test if a value constraint is met.
	 * Currently this method tests:
	 * <ul>
	 * <li>Yaw</li>
	 * <li>Roll</li>
	 * <li>Speed</li>
	 * <li>Accel</li>
	 * </ul>
	 * With the following constraints:
	 * <ul>
	 * <li>GREATER_THAN</li>
	 * <li>LESS_THAN</li>
	 * </ul>
	 * 
	 * @param vce
	 *            A ValueConstraintElement with a type to be tested and the type
	 *            of test.
	 * @return true if the value is correctly evaluated according to the
	 *         parameters, false otherwise
	 */
	public boolean visit( ValueConstraintElement vce )
	{
		switch ( vce.getValueConstraintAttribute() )
		{
		case YAW:
			if ( valueTester.testValueConstraint( vce.getValueConstraintType(), vce.getMaxValue(), ceData.getYaw() ) )
			{
				currentIssueDescription += "Waarde overschreven voor sensor: gyroscoop-yaw : " + ceData.getYaw();
				return true;
			}
			break;
		case ROLL:
			if ( valueTester.testValueConstraint( vce.getValueConstraintType(), vce.getMaxValue(), ceData.getRoll() ) )
			{
				currentIssueDescription += "Waarde overschreven voor sensor: gyroscoop-roll : " + ceData.getRoll();
				return true;
			}
			break;
		case SPEED:
			if ( valueTester.testValueConstraint( vce.getValueConstraintType(), vce.getMaxValue(), ceData.getSpeed() ) )
			{
				currentIssueDescription += "Waarde overschreven voor sensor: snelheidsmeter : " + ceData.getSpeed();
				return true;
			}
			break;
		case ACCEL:
			if ( valueTester.testValueConstraint( vce.getValueConstraintType(), vce.getMaxValue(), ceData.getAccel() ) )
			{
				currentIssueDescription += "Waarde overschreven voor sensor: versnellingsmeter : " + ceData.getAccel();
				return true;
			}
			break;
		}
		return false;
	}

	/**
	 * Prints all the issues generated by the constraint engine. Note: debug use
	 * only.
	 */
	public void printStatusReport()
	{
		for ( Issue i : issues )
		{
			System.out.println( "============================================" );
			System.out.println( i.getDescr() );
			System.out.println( "============================================" );
		}
	}

	/**
	 * Indicates that an engine is closed. Any call to this object after this
	 * method will be undefined behavior. This method will also add any new
	 * issue's to the database.
	 */
	public void stop()
	{
		data = null;
		currentIssue = null;
		ceData = null;
		issues.clear();
		constraints.clear();
		usedConstraints.clear();
		// Reset attributes before this
		factoryParent.returnConstraintEngine( getID() );
	}

	/**
	 * @return the iD
	 */
	public int getID()
	{
		return ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ID;
		result = prime * result + ( ( factoryParent == null ) ? 0 : factoryParent.hashCode() );
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals( Object obj )
	{
		if ( this == obj ) return true;
		if ( obj == null ) return false;
		if ( getClass() != obj.getClass() ) return false;
		ConstraintEngine other = (ConstraintEngine) obj;
		if ( ID != other.ID ) return false;
		if ( factoryParent == null )
		{
			if ( other.factoryParent != null ) return false;
		}
		else if ( !factoryParent.equals( other.factoryParent ) ) return false;
		return true;
	}
}
