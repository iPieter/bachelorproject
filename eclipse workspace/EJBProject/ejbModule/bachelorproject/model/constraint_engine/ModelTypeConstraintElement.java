package bachelorproject.model.constraint_engine;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

/**
 * 	Allows operators to define constraints for specific traincoach models.
 *  @author Anton Danneels
 * */
@Entity
public class ModelTypeConstraintElement extends ConstraintElement
{
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
