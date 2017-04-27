package bachelorproject.model.constraint_engine;

/**
 * A ValueConstraint object can have multiple types:
 * <ul>
 * <li>Greater than</li>
 * <li>Less than</li>
 * </ul>
 * 
 * @author Anton Danneels
 * @version 1.0.0
 */
public enum ValueConstraintType
{
	GREATER_THAN( "groter dan" ), LESS_THAN( "kleiner dan" );

	private String descr;

	private ValueConstraintType(String descr)
	{
		this.descr = descr;
	}

	/**
	 * @return the descr
	 */
	public String getDescr()
	{
		return descr;
	}

	/**
	 * @param descr
	 *            the descr to set
	 */
	public void setDescr( String descr )
	{
		this.descr = descr;
	}

}
