package bachelorproject.model.constraint_engine;

/**
 * A ValueConstraint object can have multiple attributes:
 * <ul>
 * 	<li>Yaw</li>
 * 	<li>Roll</li>
 * 	<li>Acceleration</li>
 * 	<li>Speed</li>
 * </ul>
 * @author Anton Danneels
 * @version 1.0.0
 */
public enum ValueConstraintAttribute
{
	YAW("yaw"), ROLL("roll"), ACCEL("versnelling"), SPEED("snelheid");
	
	private String descr;
	
	private ValueConstraintAttribute( String descr )
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
	 * @param descr the descr to set
	 */
	public void setDescr( String descr )
	{
		this.descr = descr;
	}
	
	
}
