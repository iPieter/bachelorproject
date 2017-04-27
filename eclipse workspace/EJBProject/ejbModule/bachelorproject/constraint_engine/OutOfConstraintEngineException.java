package bachelorproject.constraint_engine;

/**
 * Used when the {@link ConstraintEngineFactory} has no more free CE objects
 * 
 * @author Anton Danneels
 * @version 0.0.1
 */
public class OutOfConstraintEngineException extends Exception
{
	private static final long serialVersionUID = 1L;

	public OutOfConstraintEngineException()
	{
		super( "There are no more free ContraintEngine objects. Close those objects to free them." );
	}
}
