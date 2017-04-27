package bachelorproject.constraint_engine;

import bachelorproject.model.constraint_engine.ValueConstraintType;

/**
 * Test if a value constraint is correct.
 * 
 * @see ConstraintEngine
 * @author Anton Danneels
 */
public class ConstraintEngineValueTester
{
	/**
	 * Test if a value constraint is correct
	 * 
	 * @param type
	 *            The type of valueconstraint
	 * @param maxVal
	 *            The maximum value of the constraint
	 * @param val
	 *            The value to be tested
	 */
	public boolean testValueConstraint( ValueConstraintType type, double maxVal, double val )
	{
		switch ( type )
		{
		case GREATER_THAN:
			return val > maxVal;
		case LESS_THAN:
			return val < maxVal;
		default:
			return false;
		}
	}
}
