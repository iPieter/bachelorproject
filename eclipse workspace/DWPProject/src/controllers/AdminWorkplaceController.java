package controllers;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import bachelorproject.ejb.UserEJB;
import bachelorproject.ejb.WorkplaceEJB;
import bachelorproject.model.Workplace;
import bachelorproject.model.user.User;
import bachelorproject.model.user.UserRole;

@Named
@SessionScoped
public class AdminWorkplaceController implements Serializable
{
	private static final long serialVersionUID = -5824382672006285083L;

	@EJB
	private WorkplaceEJB workplaceEJB;

	@EJB
	private UserEJB userEJB;

	private String mechanicId;

	private Workplace currentWorkplace;

	/**
	 * Provides a list with all the workplaces known to the database.
	 * 
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @return List object with all Workplace objects
	 * @see Workplace
	 */
	public List<Workplace> getAllWorkplaces()
	{
		return workplaceEJB.getAllWorkplaces();
	}

	/**
	 * When the admin wants to add a mechanic to a workplace, a workplace the
	 * admin selected is set to the currentWorkplace by it's id.
	 * 
	 * @author Pieter Delobelle
	 * @version 0.9.0
	 * @param workplaceId
	 *            The id of the workplace to set as the currentWorkplace
	 */
	public void setCurrentWorkplace( int workplaceId )
	{
		if ( workplaceId != 0 )
		{
			this.currentWorkplace = workplaceEJB.findWorkplaceByWorkplaceId( workplaceId );
		}
	}

	/**
	 * Get the current workplace object.
	 * 
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @return The current workplace object
	 */
	public Workplace getCurrentWorkplace()
	{
		return currentWorkplace;
	}

	/**
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @param currentWorkplace
	 *            the currentWorkplace to set
	 */
	public void setCurrentWorkplace( Workplace currentWorkplace )
	{
		this.currentWorkplace = currentWorkplace;
	}

	/**
	 * Returns all the mechanics in the database
	 * 
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @return All the mechanics in the database.
	 */
	public List<User> getMechanics()
	{
		return userEJB.findAllUsersByRole( UserRole.MECHANIC );
	}

	/**
	 * Inserts the mechnanic specified by it's id to the current workplace.
	 * 
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 */
	public void addMechanic()
	{

		currentWorkplace.getMechanics().add( userEJB.findUserById( Integer.valueOf( mechanicId ) ) );

		workplaceEJB.updateWorkplace( currentWorkplace );
	}

	/**
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @return the mechanicId
	 */
	public String getMechanicId()
	{
		return mechanicId;
	}

	/**
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @param mechanicId
	 *            the mechanicId to set
	 */
	public void setMechanicId( String mechanicId )
	{
		this.mechanicId = mechanicId;
	}

}
