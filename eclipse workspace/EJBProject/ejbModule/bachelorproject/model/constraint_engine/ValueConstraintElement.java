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
	private double maxValue;
	
	@NotNull
	@Enumerated( EnumType.STRING )
	ValueConstraintType valueConstraintType;

	@NotNull
	@Enumerated( EnumType.STRING )
	ValueConstraintAttribute valueConstraintAttribute;
	
	public ValueConstraintElement( )
	{
		super();
	}
	
	public ValueConstraintElement(double maxValue, ValueConstraintType type, ValueConstraintAttribute attr )
	{
		super();
		this.maxValue = maxValue;
		this.valueConstraintType = type;
		this.valueConstraintAttribute = attr;
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
	 * @return the valueConstraintType
	 */
	public ValueConstraintType getValueConstraintType()
	{
		return valueConstraintType;
	}

	/**
	 * @param valueConstraintType the valueConstraintType to set
	 */
	public void setValueConstraintType( ValueConstraintType valueConstraintType )
	{
		this.valueConstraintType = valueConstraintType;
	}

	/**
	 * @return the valueConstraintAttribute
	 */
	public ValueConstraintAttribute getValueConstraintAttribute()
	{
		return valueConstraintAttribute;
	}

	/**
	 * @param valueConstraintAttribute the valueConstraintAttribute to set
	 */
	public void setValueConstraintAttribute( ValueConstraintAttribute valueConstraintAttribute )
	{
		this.valueConstraintAttribute = valueConstraintAttribute;
	}
	
	public String toString()
	{
		return "Beperking op " + valueConstraintAttribute.getDescr() + " ,mag niet " + valueConstraintType.getDescr() + " " + maxValue + " zijn.";
	}
}
