package bachelorproject.model.constraint_engine;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

import bachelorproject.constraint_engine.ConstraintEngine;

/**
 * 	Allows operators to define constraints for specific traincoach models.
 *  @author Anton Danneels
 * */
@Entity
public class ModelTypeConstraintElement extends ConstraintElement
{
	private static final long serialVersionUID = -4632676131576240262L;

	@NotNull
	@Lob
	String modelType;
	
	public ModelTypeConstraintElement()
	{
		super();
	}

	public ModelTypeConstraintElement(String type)
	{
		super();
		this.modelType = type;
	}

	public boolean visit( ConstraintEngine ce )
	{
		return ce.visit( this );
	}
	
	/**
	 * @return the type
	 */
	public String getType()
	{
		return modelType;
	}

	/**
	 * @param type the type to set
	 */
	public void setType( String type )
	{
		this.modelType = type;
	}
	
}