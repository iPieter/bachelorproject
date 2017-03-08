package bachelorproject.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import bachelorproject.model.IssueAsset;

/**
 * Defines the Entity Java Bean for the IssueAsset Entity.
 * <p>
 * This class allows for the controller to manipulate and fetch specific IssueAsset
 * instances. It validates new objects and handles errors when, for example, no
 * entries in the database exist.
 * @author Anton Danneels
 * @version 0.0.1
 */
@Named
@Stateless
public class IssueAssetEJB
{
	/**
	 * 	Gets all the IssueAssets connected to the IssueID.
	 *  <p>
	 * 	Retrieves all the issue asset objects from the database
	 *  that are connected to the issue specified with issueID
	 *  @param issueID The associated Issue.
	 * */
	public List<IssueAsset> getAllIssueAssetsByIssueID( int issueID )
	{
		EntityManagerFactory emf = Persistence.createEntityManagerFactory( "EJBProject" );
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		TypedQuery<IssueAsset> query = em.createNamedQuery( IssueAsset.FIND_BY_ISSUE_ID, IssueAsset.class );
		query.setParameter( "id", issueID );
		
		List<IssueAsset> result = query.getResultList();

		em.getTransaction().commit();
		em.close();
		emf.close();
		
		return result;
	}
}
