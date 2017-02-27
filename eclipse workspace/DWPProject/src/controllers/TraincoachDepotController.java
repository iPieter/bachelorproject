package controllers;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import be.kuleuven.cs.gent.projectweek.model.Workplace;
import be.kuleuven.cs.gent.projectweek.services.InternalDatafetchService;

@Named
@RequestScoped
public class TraincoachDepotController {
	
	 @Inject
	 private InternalDatafetchService internalDatafetchService;	
	 
	public List<Workplace> getAllTraincoachDepots(){
		return internalDatafetchService.getAllTraincoachDepots();
	}
}
