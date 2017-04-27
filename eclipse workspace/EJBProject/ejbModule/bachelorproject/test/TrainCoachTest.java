package bachelorproject.test;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import bachelorproject.ejb.TrainCoachEJB;
import bachelorproject.model.TrainCoach;

//@Singleton
//@Startup
public class TrainCoachTest
{
	@Inject
	private TrainCoachEJB traincoachEJB;

	// @PostConstruct
	public void test()
	{
		/*
		 * System.out.println("===============================================")
		 * ;
		 * 
		 * TrainCoach traincoach = traincoachEJB.findByData( "7858558", "M7",
		 * "Bombardier" ); if( traincoach == null ) System.out.println(
		 * "Traincoach is null" ); else System.out.println("Success");
		 * 
		 * List<TrainCoach> traincoaches = traincoachEJB.getAllTraincoaches();
		 * for( TrainCoach t : traincoaches ) { System.out.println(
		 * t.getName().equals( "7858558" ) ); System.out.println(
		 * t.getType().equals( "M7" ) ); System.out.println(
		 * t.getConstructor().equals( "Bombardier" ) ); }
		 * System.out.println("===============================================")
		 * ;
		 */
	}

}
