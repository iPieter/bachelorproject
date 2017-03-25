package bachelorproject.model.constraint_engine;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

/**
 * 	This class stores a value constraint: a max value and a condition.
 * */
@Entity
public class ValueConstraintElement extends ConstraintElement
{
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
