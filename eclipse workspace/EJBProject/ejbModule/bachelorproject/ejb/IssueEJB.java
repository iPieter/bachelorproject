package bachelorproject.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import bachelorproject.model.Issue;
import bachelorproject.model.TrainCoach;

@Named
@Stateless
public class IssueEJB
{
	
	public List<Issue> findInProgressIssuesByMechanicId(int mechanicId){
		EntityManagerFactory emf = Persistence.createEntityManagerFactory( "EJBProject" );
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		TypedQuery<Issue> query = em.createNamedQuery( Issue.FIND_BY_MECHANIC_ID, Issue.class )
									.setParameter("status", "IN_PROGRESS")
									.setParameter("mechanicId", mechanicId);
		List<Issue> result = query.getResultList();
		
		em.getTransaction().commit();
		em.close();
		emf.close();

		return result;
	}
	
	public List<Issue> findAssignedIssuesByMechanicId(int mechanicId){
		EntityManagerFactory emf = Persistence.createEntityManagerFactory( "EJBProject" );
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		TypedQuery<Issue> query = em.createNamedQuery( Issue.FIND_BY_MECHANIC_ID, Issue.class )
									.setParameter("status", "ASSIGNED")
									.setParameter("mechanicId", mechanicId);
		List<Issue> result = query.getResultList();
		
		for(Issue i:result){
			System.out.println(i.getDesc());
		}
		
		em.getTransaction().commit();
		em.close();
		emf.close();

		return result;
	}
}
