package bachelorproject.model.issue;

/**
 * A Issue object can have three stages:
 * <ul>
 * <li>Assigned
 * <li>In progress
 * <li>Closed
 * </ul>
 * This enum provides these values and also a simple display text.
 * 
 * @author Pieter Delobelle
 * @version 1.0.0
 */
public enum IssueStatus
{
	ASSIGNED("Toegewezen"), IN_PROGRESS("In behandeling"), CLOSED("Gesloten");
	
	private String descr;
	
	IssueStatus(String descr)
	{
		this.descr = descr;
	}

	/**
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @return the description of the status
	 */
	public String getDescr()
	{
		return descr;
	}

	/**
	 * Set's the description of a IssueStatus, normally not used.
	 * 
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @param descr the description of the IssueStatus to set
	 */
	public void setDescr(String descr)
	{
		this.descr = descr;
	}
}
