package bachelorproject.model.user;

/**
 * The UserRole a User can have, like ADMIN. 
 * 
 * @author Pieter Delobelle
 * @version 1.0.0
 * @see User
 */
public enum UserRole
{
	ADMIN("Admin"), MECHANIC("Technicus"), OPERATOR("Operator");

	
	private String descr;
	
	UserRole(String descr)
	{
		this.descr = descr;
	}

	/**
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @return the description of the role
	 */
	public String getDescr()
	{
		return descr;
	}

	/**
	 * Set's the description of a UserRole, normally not used.
	 * 
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @param descr the description of the role to set
	 */
	public void setDescr(String descr)
	{
		this.descr = descr;
	}
	
	
	
}
