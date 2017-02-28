package controllers;

import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import be.kuleuven.cs.gent.projectweek.model.Workplace;
import be.kuleuven.cs.gent.projectweek.services.InternalDatafetchService;
import java.io.Serializable;

@Named
//@RequestScoped
@SessionScoped
public class TraincoachDepotController implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Inject
	private InternalDatafetchService internalDatafetchService;

	private Workplace currentTrainCoachDepot = new Workplace();

	public List<Workplace> getAllTraincoachDepots()
	{
		return internalDatafetchService.getAllTraincoachDepots();
	}

	public void doFindTrainCoachDepotById()
	{
		currentTrainCoachDepot = internalDatafetchService.doFindTrainCoachDepotById( currentTrainCoachDepot.getId() );
	}

	// GETTERS & SETTERS
	public Workplace getCurrentTrainCoachDepot()
	{
		return currentTrainCoachDepot;
	}

}
