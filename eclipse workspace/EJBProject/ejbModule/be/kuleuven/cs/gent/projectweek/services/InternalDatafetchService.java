package be.kuleuven.cs.gent.projectweek.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import be.kuleuven.cs.gent.projectweek.model.*;

@Named
@Stateless
public class InternalDatafetchService {
	
	//@PersistenceContext(unitName="EJBProject")
	//private EntityManager em;
	
	public List<Workplace> getAllTraincoachDepots(){
		//TypedQuery<Workplace> query= em.createNamedQuery("findAll", Workplace.class);
		//return query.getResultList();	

		return null;
	}
	
}
