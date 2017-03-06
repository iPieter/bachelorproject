package bachelorproject.data;

public class TaskValidationException extends Exception{
	private static final long serialVersionUID = -8024514858638546701L;

	public TaskValidationException(String message)
	{
		super(message);
	}
	
	public TaskValidationException()
	{
		super("Error while validating Task object.");
	}

}
