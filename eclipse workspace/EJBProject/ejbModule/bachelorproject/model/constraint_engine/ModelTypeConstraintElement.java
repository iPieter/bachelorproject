package bachelorproject.model.constraint_engine;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

import bachelorproject.constraint_engine.ConstraintEngine;

/**
 * Allows operators to define constraints for specific traincoach models.
 * 
 * @author Anton Danneels
 */
@Entity
public class ModelTypeConstraintElement extends ConstraintElement implements Serializable
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

	/**
	 * The ConstraintEngine uses a visitor pattern to implemented evaluating
	 * ConstraintElements.
	 */
	public boolean visit( ConstraintEngine ce )
	{
		return ce.visit( this );
	}

	/**
	 * @return the modelType
	 */
	public String getModelType()
	{
		return modelType;
	}

	/**
	 * @param modelType
	 *            the modelType to set
	 */
	public void setModelType( String modelType )
	{
		this.modelType = modelType;
	}

	public String toString()
	{
		return "Model beperking: " + modelType;
	}
}
