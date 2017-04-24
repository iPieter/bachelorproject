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
	CREATED("Aangemaakt", "info"), ASSIGNED("Toegewezen", "danger"), IN_PROGRESS("In behandeling", "warning"), CLOSED("Gesloten", "success");
	
	private String descr;
	private String color;
	
	IssueStatus(String descr, String color)
	{
		this.descr = descr;
		this.color = color;
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
	
	/**
	 * Getter to generate a color description for each status. Uses the 
	 * following values
	 * <ul>
	 *   <li>danger</li>
	 *   <li>warning</li>
	 *   <li>success</li>
	 * </ul>
	 * 
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @return a 
	 */
	public String getColor()
	{
		return color;
	}
}
