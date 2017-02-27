package be.kuleuven.cs.gent.projectweek.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import be.kuleuven.cs.gent.projectweek.model.*;

@Named
@Stateless
public class InternalDatafetchService {
	
	public List<Workplace> getAllTraincoachDepots(){	
		
		EntityManagerFactory emf =  Persistence.createEntityManagerFactory("EJBProject");
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		TypedQuery<Workplace> query= em.createNamedQuery("findAll", Workplace.class);
		
		em.getTransaction().commit();
		em.close();
		emf.close();
		
		return query.getResultList();	
	}
	
}
