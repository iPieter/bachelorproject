package controllers;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
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
import bachelorproject.services.UserService;

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
	}
	
	public void createConstraint()
	{
	}
	
	public void createConstraintElement()
	{
	}
	
	public List<Constraint> getConstraints()
	{
		return constraints;
	}
	
	public List<LocationPoint> getPolygon( int ID )
	{
		return polygons.get( ID );
	}
}
