package bachelorproject.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import bachelorproject.model.constraint_engine.Constraint;
import bachelorproject.model.constraint_engine.ConstraintElement;

/**
 * Defines the Entity Java Bean for the Constraint Entity.
 * <p>
 * This class allows for the controller to manipulate and fetch specific Constraint
 * instances. It validates new objects and handles errors when, for example, no
 * entries in the database exist.
 * @author Anton Danneels
 */
@Named
@Stateless
public class ConstraintEJB
{
	@Inject
	private EntityManagerSingleton ems;
	
	/**
	 * 	Retrieves a list of all constraints.
	 *  @return A List of all constraints.
	 * */
	public List<Constraint> getAllConstraints()
	{
		EntityManager em = ems.getEntityManager();
		em.getTransaction().begin();
		
		TypedQuery<Constraint> query = em.createNamedQuery( Constraint.FIND_ALL, Constraint.class );
		List<Constraint> result = query.getResultList();
		
		em.getTransaction().commit();
		em.close();
		
		return result;
	}
	
	/**
	 * 	Persists a new Constraint object to the database
	 * 	@param constraint A valid Constraint object.
	 * */
	public void createConstraint( Constraint constraint )
	{
		EntityManager em = ems.getEntityManager();
		em.getTransaction().begin();
		
		em.persist( constraint );
		
		em.getTransaction().commit();
		em.close();
	}
	
	/**
	 * 	Adds a ConstraintElement to a Constraint. Note that this method
	 *  expects a ConstraintElement to be already persisted.
	 *  @param ce The element to add to a ConstraintElement
	 *  @param constraintID The constraint id that the element needs to be added to
	 */
	public void addConstraintElement( int constraintID, ConstraintElement ce )
	{
		EntityManager em = ems.getEntityManager();
		em.getTransaction().begin();
		
		Constraint c = em.find( Constraint.class, constraintID );
		if( c != null )
		{
			c.getConstraints().add( ce );
			em.merge( c );
		}
		
		em.getTransaction().commit();
		em.close();
	}
}
