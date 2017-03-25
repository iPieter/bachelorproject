package bachelorproject.model.constraint_engine;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

import bachelorproject.constraint_engine.ConstraintEngine;

/**
 * 	This class stores a value constraint: a max value and a condition.
 * */
@Entity
public class ValueConstraintElement extends ConstraintElement
{
	private static final long serialVersionUID = 9149375801125492782L;

	@NotNull
	@Column( name = "`max_value`" )
	double maxValue;
	
	@NotNull
	@Enumerated( EnumType.STRING )
	ValueConstraintType valueConstraintType;

	public ValueConstraintElement( )
	{
		super();
	}
	
	public ValueConstraintElement(double maxValue, ValueConstraintType type)
	{
		super();
		this.maxValue = maxValue;
		this.valueConstraintType = type;
	}
	
	public boolean visit( ConstraintEngine ce )
	{
		return ce.visit( this );
	}

	/**
	 * @return the maxValue
	 */
	public double getMaxValue()
	{
		return maxValue;
	}

	/**
	 * @param maxValue the maxValue to set
	 */
	public void setMaxValue( double maxValue )
	{
		this.maxValue = maxValue;
	}

	/**
	 * @return the type
	 */
	public ValueConstraintType getType()
	{
		return valueConstraintType;
	}

	/**
	 * @param type the type to set
	 */
	public void setType( ValueConstraintType type )
	{
		this.valueConstraintType = type;
	}
}
