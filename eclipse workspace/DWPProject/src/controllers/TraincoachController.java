package controllers;

import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import be.kuleuven.cs.gent.projectweek.model.TrainCoach;
import be.kuleuven.cs.gent.projectweek.services.InternalDatafetchService;
import java.io.Serializable;

@Named
//@RequestScoped
@SessionScoped
public class TraincoachController implements Serializable
{

	private static final long serialVersionUID = 1L;

	@Inject
	private InternalDatafetchService internalDatafetchService;
	private TrainCoach currentTrainCoach = new TrainCoach();

	public List<TrainCoach> getAllTraincoaches()
	{
		return internalDatafetchService.getAllTraincoaches();
	}

	public void doFindTrainCoachById()
	{
		currentTrainCoach = internalDatafetchService.doFindTrainCoachById( currentTrainCoach.getId() );
	}

	// GETTERS & SETTERS
	public TrainCoach getCurrentTrainCoach()
	{
		return currentTrainCoach;
	}
}
