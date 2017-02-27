package controllers;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import be.kuleuven.cs.gent.projectweek.model.TrainCoach;
import be.kuleuven.cs.gent.projectweek.services.InternalDatafetchService;

@Named
@RequestScoped
public class TraincoachController {

	@Inject
	 private InternalDatafetchService internalDatafetchService;	
	
	public List<TrainCoach> getAllTraincoaches(){
		return internalDatafetchService.getAllTraincoaches();
	}
}
