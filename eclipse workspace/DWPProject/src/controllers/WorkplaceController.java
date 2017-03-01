package controllers;

import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import be.kuleuven.cs.gent.projectweek.model.Workplace;
import be.kuleuven.cs.gent.projectweek.services.InternalDatafetchService;
import java.io.Serializable;

@Named
@SessionScoped
public class WorkplaceController implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Inject
	private InternalDatafetchService internalDatafetchService;

	private Workplace currentWorkplace = new Workplace();

	public List<Workplace> getAllWorkplaces()
	{
		return internalDatafetchService.getAllWorkplaces();
	}

	public void doFindWorkplaceById()
	{
		currentWorkplace = internalDatafetchService.doFindWorkplaceById( currentWorkplace.getId() );
	}

	// GETTERS & SETTERS
	public Workplace getCurrentWorkplace()
	{
		return currentWorkplace;
	}

}
