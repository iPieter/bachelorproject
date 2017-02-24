package controllers;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import model.Workplace;
import service.InternalDatafetchService;

@Named
@RequestScoped
public class TraincoachDepotController {
	
	 @Inject
	 private InternalDatafetchService internalDatafetchService;	
	 
	public List<Workplace> getAllTraincoachDepots(){
		return internalDatafetchService.getAllTraincoachDepots();
	}
}
