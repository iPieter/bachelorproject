package controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import be.kuleuven.cs.gent.projectweek.model.TrainCoach;
import be.kuleuven.cs.gent.projectweek.model.User;
import be.kuleuven.cs.gent.projectweek.model.Workplace;
import be.kuleuven.cs.gent.projectweek.services.InternalDatafetchService;

@Named
@RequestScoped
public class TraincoachController implements Serializable
{

	private static final long serialVersionUID = 1L;

	@Inject
	private InternalDatafetchService internalDatafetchService;
	
	private TrainCoach currentTrainCoach = new TrainCoach();
	private Workplace currentWorkplace = new Workplace();
	private List<User> mechanics = new ArrayList<User>();
	
	public List<TrainCoach> getAllTraincoaches()
	{
		return internalDatafetchService.getAllTraincoaches();
	}

	public void findTrainCoachById()
	{
		currentTrainCoach = internalDatafetchService.findTrainCoachByTraincoachId( currentTrainCoach.getId() );
		List<Workplace> result = internalDatafetchService.findWorkplaceByTraincoachID( currentTrainCoach.getId() );
		if( result.size() > 0 )
		{
			currentWorkplace = result.get( 0 );
		}
		mechanics.clear();
		for( User u : internalDatafetchService.getWorkplaceMechanics( currentWorkplace.getId() ) )
			mechanics.add( u );
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
	public List<User> getMechanics()
	{
		return mechanics;
	}
}
