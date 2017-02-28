package controllers;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import be.kuleuven.cs.gent.projectweek.model.TrainCoach;
import be.kuleuven.cs.gent.projectweek.model.Workplace;
import be.kuleuven.cs.gent.projectweek.services.InternalDatafetchService;

@Named
//@RequestScoped
@SessionScoped
public class TraincoachController implements Serializable
{

	private static final long serialVersionUID = 1L;

	@Inject
	private InternalDatafetchService internalDatafetchService;
	private TrainCoach currentTrainCoach = new TrainCoach();
	private Workplace currentWorkplace = new Workplace();
	
	public List<TrainCoach> getAllTraincoaches()
	{
		return internalDatafetchService.getAllTraincoaches();
	}

	public void doFindTrainCoachById()
	{
		currentTrainCoach = internalDatafetchService.doFindTrainCoachById( currentTrainCoach.getId() );
		List<Workplace> result = internalDatafetchService.findWorkplaceByTraincoachID( currentTrainCoach.getId() );
		if( result.size() > 0 )
		{
			currentWorkplace = result.get( 0 );
		}
	}

	// GETTERS & SETTERS
	public TrainCoach getCurrentTrainCoach()
	{
		return currentTrainCoach;
	}
	public Workplace getCurrentWorkplace()
	{
		return currentWorkplace;
	}
}
