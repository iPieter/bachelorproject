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
	
	@PostConstruct
	public void loadPage()
	{
		System.out.println( "Creating list." );
		constraints = cEJB.getAllConstraints();
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
}
