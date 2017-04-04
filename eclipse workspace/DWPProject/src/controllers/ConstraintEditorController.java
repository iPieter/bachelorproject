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
import bachelorproject.model.constraint_engine.ModelTypeConstraintElement;
import bachelorproject.model.constraint_engine.ValueConstraintAttribute;
import bachelorproject.model.constraint_engine.ValueConstraintElement;
import bachelorproject.model.constraint_engine.ValueConstraintType;
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
		System.out.println("Creating list.");
		constraints = cEJB.getAllConstraints();
		polygons = new HashMap<>();
		for (Constraint c : constraints)
		{
			for (ConstraintElement ce : c.getConstraints())
			{
				if (ce instanceof LocationConstraintElement)
					polygons.put(c.getId(), ((LocationConstraintElement) ce).getPolygon());
			}
		}

		currentConstraintElements
				.add(new LocationConstraintElement());

	}

	public void createConstraint()
	{
	}

	/**
	 * Add a default constraintElement (valueConstraintElement for now) to the
	 * list with constraintElements.
	 * 
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @see ConstraintElement
	 */
	public void createConstraintElement()
	{
		currentConstraintElements
				.add(new ValueConstraintElement(10, ValueConstraintType.GREATER_THAN, ValueConstraintAttribute.ACCEL));
		System.out.println("Created new ContraintElement");
	}

	/**
	 * Analyses the given constraintElement and returns a string with the type
	 * of constraintElement, like yaw, roll, accel, model, location.
	 * 
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @param ce
	 *            The contraintElement to be tested
	 * @return A string with the type of contraintElement, as decribed.
	 */
	public String determineContraintElementType(ConstraintElement ce)
	{
		if (ce instanceof ValueConstraintElement)
		{
			return ((ValueConstraintElement) ce).getValueConstraintAttribute().getDescr();
		} else if (ce instanceof ModelTypeConstraintElement)
		{
			return "model";
		}

		return "locatie";
	}

	/**
	 * Simple boolean method to test if an ConstraintElement is a 
	 * ValueConstraintElement.
	 * 
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @param ce The ConstraintElement object to be tested.
	 * @return True if it's a ValueContraintElement, false otherwise.
	 */
	public boolean isValueContraintElement(ConstraintElement ce)
	{
		return ce instanceof ValueConstraintElement;
	}
	
	/**
	 * Simple boolean method to test if an ConstraintElement is a 
	 * LocationConstraintElement.
	 * 
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @param ce The ConstraintElement object to be tested.
	 * @return True if it's a LocationConstraintElement, false otherwise.
	 */
	public boolean isLocationConstraintElement(ConstraintElement ce)
	{
		return ce instanceof LocationConstraintElement;
	}
	
	/**
	 * Simple boolean method to test if an ConstraintElement is a 
	 * ModelTypeConstraintElement.
	 * 
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @param ce The ConstraintElement object to be tested.
	 * @return True if it's a ModelTypeConstraintElement, false otherwise.
	 */
	public boolean isModelTypeConstraintElement(ConstraintElement ce)
	{
		return ce instanceof ModelTypeConstraintElement;
	}
	
	/**
	 * Removes the provided constraintElement from the list of currentContraintElements
	 * and adds new one of the type ValueContraintElement with the ValueConstraintAttribute
	 * provided as second parameter.
	 * <p>
	 * If no constraintElement is found that matches the provided constraintElement, nothing
	 * will happen. So the list with current ConstraintElements will stay the same.
	 * 
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @param ce The constraintElement object to be replaced
	 * @param vca The ValueConstraintAttribute to use for the new ValueConstraintElement
	 * @see ValueConstraintElement
	 */
	public void updateContraintElement(ConstraintElement ce, ValueConstraintAttribute vca)
	{
		int index = currentConstraintElements.indexOf(ce);
		
		if (index != -1)
		{
			currentConstraintElements.remove(index);
			currentConstraintElements.add(index, new ValueConstraintElement(10, ValueConstraintType.GREATER_THAN, vca));
		}
		
	}
	
	/**
	 * Set replace a constraintElement with a new one, determined by a string with the
	 * following possibilities:
	 * <ul>
	 * 	<li>ModelTypeConstraintElement</li>
	 * 	<li>LocationConstraintElement</li>
	 * </ul>
	 * 
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @param ce The constraintElement to be replaced
	 * @param futureConstraintElement A string with the future type of the constraintElement
	 */
	public void updateContraintElement(ConstraintElement ce, String futureConstraintElement)
	{
		int index = currentConstraintElements.indexOf(ce);
		
		if (index != -1)
		{
			currentConstraintElements.remove(index);
			switch (futureConstraintElement)
			{
			case "ModelTypeConstraintElement":
				currentConstraintElements.add(index, new ModelTypeConstraintElement("M7"));
				break;
				
			case "LocationConstraintElement":
				currentConstraintElements.add(index, new LocationConstraintElement());
				break;
				
			default:
				break;
			}
		}
	}
	
	/**
	 * Determines if the list with current constaintElements contains a locationConstaintElement.
	 * 
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @return True if the list with current constaintElements contains a locationConstaintElement
	 */
	public boolean containsLocationConstaintElement()
	{
		boolean containsLCE = false;
		
		for (ConstraintElement ce : this.currentConstraintElements)
		{
			containsLCE = containsLCE || isLocationConstraintElement(ce);
		}
		
		return containsLCE;
		
	}
	

	public List<Constraint> getConstraints()
	{
		return constraints;
	}

	public List<LocationPoint> getPolygon(int ID)
	{
		return polygons.get(ID);
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
	 * @param currentConstraintElements
	 *            the currentConstraintElements to set
	 */
	public void setCurrentConstraintElements(List<ConstraintElement> currentConstraintElements)
	{
		this.currentConstraintElements = currentConstraintElements;
	}

	/**
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @param constraints
	 *            the constraints to set
	 */
	public void setConstraints(List<Constraint> constraints)
	{
		this.constraints = constraints;
	}

	/**
	 * Returns a simple array with the defined attributes:
	 * <ul>
	 * <li>Yaw</li>
	 * <li>Roll</li>
	 * <li>Acceleration</li>
	 * <li>Speed</li>
	 * </ul>
	 * 
	 * These are returned as ValueConstraintAttribute objects.
	 * 
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @return A list with all defined ValueConstraintAttributes.
	 */
	public ValueConstraintAttribute[] getValueConstraintAttributes()
	{
		return ValueConstraintAttribute.values();
	}

}
