package bachelorproject.model.constraint_engine;

/**
 * A ValueConstraint object can have multiple attributes:
 * <ul>
 * <li>Yaw</li>
 * <li>Roll</li>
 * <li>Acceleration</li>
 * <li>Speed</li>
 * </ul>
 * 
 * @author Anton Danneels
 * @version 1.0.0
 */
public enum ValueConstraintAttribute
{
	YAW( "yaw", "rad/s" ), ROLL( "roll", "rad/s" ), ACCEL( "versnelling", "m/s^2" ), SPEED( "snelheid", "m/s" );

	private String descr;
	private String unit;

	private ValueConstraintAttribute(String descr, String unit)
	{
		this.descr = descr;
		this.unit = unit;
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

	/**
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @return the unit
	 */
	public String getUnit()
	{
		return unit;
	}

	/**
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @param unit
	 *            the unit to set
	 */
	public void setUnit( String unit )
	{
		this.unit = unit;
	}

}
