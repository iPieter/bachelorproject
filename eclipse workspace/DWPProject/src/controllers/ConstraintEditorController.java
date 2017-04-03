package controllers;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import bachelorproject.ejb.ConstraintEJB;
import bachelorproject.ejb.ConstraintElementEJB;
import bachelorproject.model.constraint_engine.Constraint;
import bachelorproject.model.constraint_engine.ConstraintElement;
import bachelorproject.model.constraint_engine.LocationConstraintElement;
import bachelorproject.model.constraint_engine.LocationPoint;
import bachelorproject.model.constraint_engine.ValueConstraintElement;
import bachelorproject.services.UserService;

/**
 * @author Pieter Delobelle
 * @version 1.0.0
 */
@Named
@ViewScoped
public class ConstraintEditorController implements Serializable
{
	private static final long serialVersionUID = 941412253990290335L;

	@Inject
	private ConstraintEJB cEJB;
	@Inject
	private ConstraintElementEJB ceEJB;
	@Inject
	private UserService userService;
	
	private List<Constraint> constraints;
	private List<ConstraintElement> currentConstraintElements = new LinkedList<>();
	private HashMap<Integer, List<LocationPoint>> polygons;
	
	@PostConstruct
	public void loadPage()
	{
		System.out.println( "Creating list." );
		constraints = cEJB.getAllConstraints();
		polygons = new HashMap<>();
		for( Constraint c : constraints )
		{
			for( ConstraintElement ce : c.getConstraints() )
			{
				if( ce instanceof LocationConstraintElement )
					polygons.put( c.getId(), ( (LocationConstraintElement) ce ).getPolygon() );
			}
		}
		
		//TODO: for testing
		currentConstraintElements.add(new ValueConstraintElement());

	}
	
	public void createConstraint()
	{
	}
	
	/**
	 * Add a default constraintElement (valueConstraintElement for now) to the list
	 * with constraintElements.
	 * 
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @see ConstraintElement
	 */
	public void createConstraintElement()
	{
		currentConstraintElements.add(new ValueConstraintElement());
		System.out.println("Created new ContraintElement");
	}
	
	public List<Constraint> getConstraints()
	{
		return constraints;
	}
	
	public List<LocationPoint> getPolygon( int ID )
	{
		return polygons.get( ID );
	}

	/**
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @return the currentConstraintElements
	 */
	public List<ConstraintElement> getCurrentConstraintElements()
	{
		return currentConstraintElements;
	}

	/**
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @param currentConstraintElements the currentConstraintElements to set
	 */
	public void setCurrentConstraintElements(List<ConstraintElement> currentConstraintElements)
	{
		this.currentConstraintElements = currentConstraintElements;
	}

	/**
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @param constraints the constraints to set
	 */
	public void setConstraints(List<Constraint> constraints)
	{
		this.constraints = constraints;
	}
	
	
}
