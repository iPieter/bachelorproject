package bachelorproject.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import bachelorproject.model.issue.IssueAsset;

/**
 * Defines the Entity Java Bean for the IssueAsset Entity.
 * <p>
 * This class allows for the controller to manipulate and fetch specific
 * IssueAsset instances. It validates new objects and handles errors when, for
 * example, no entries in the database exist.
 * 
 * @author Anton Danneels
 * @version 0.0.1
 */
@Named
@Stateless
public class IssueAssetEJB
{
	@Inject
	private EntityManagerSingleton ems;

	/**
	 * Gets all the IssueAssets connected to the IssueID.
	 * <p>
	 * Retrieves all the issue asset objects from the database that are
	 * connected to the issue specified with issueID
	 * 
	 * @param issueID
	 *            The associated Issue.
	 * @return A list of issue assets.
	 */
	public List<IssueAsset> getAllIssueAssetsByIssueID( int issueID )
	{
		EntityManager em = ems.getEntityManager();
		em.getTransaction().begin();

		TypedQuery<IssueAsset> query = em.createNamedQuery( IssueAsset.FIND_BY_ISSUE_ID, IssueAsset.class );
		query.setParameter( "id", issueID );

		List<IssueAsset> result = query.getResultList();

		em.getTransaction().commit();
		em.close();

		return result;
	}

	/**
	 * Retrieves an issueAsset from the database.
	 * 
	 * @param issueAssetID
	 *            The ID of the IssueAsset thats needed.
	 * @return The IssueAsset if found, null otherwise.
	 */
	public IssueAsset getIssueAssetByID( int issueAssetID )
	{
		EntityManager em = ems.getEntityManager();
		em.getTransaction().begin();

		TypedQuery<IssueAsset> query = em.createNamedQuery( IssueAsset.FIND_BY_ID, IssueAsset.class );
		query.setParameter( "id", issueAssetID );

		List<IssueAsset> result = query.getResultList();

		em.getTransaction().commit();
		em.close();

		if ( result.size() == 0 ) return null;
		return result.get( 0 );
	}

	/**
	 * Persists an IssueAsset object to the database.
	 * 
	 * @param asset
	 *            A valid IssueAsset object.
	 */
	public void createIssueAsset( IssueAsset asset )
	{
		EntityManager em = ems.getEntityManager();
		em.getTransaction().begin();

		em.persist( asset );

		em.getTransaction().commit();
		em.close();
	}

	/**
	 * Retrieves a list of all issueassets
	 * 
	 * @return A list of all issue assets.
	 */
	public List<IssueAsset> getAll()
	{
		EntityManager em = ems.getEntityManager();
		em.getTransaction().begin();

		TypedQuery<IssueAsset> query = em.createNamedQuery( IssueAsset.FIND_ALL, IssueAsset.class );

		List<IssueAsset> result = query.getResultList();

		em.getTransaction().commit();
		em.close();

		return result;
	}
}
