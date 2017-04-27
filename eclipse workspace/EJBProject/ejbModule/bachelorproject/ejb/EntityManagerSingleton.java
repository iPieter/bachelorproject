package bachelorproject.ejb;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * We are using RESOURCE_LOCAL for our JTA content. This implies that we are
 * responsible for the creation and management of EntityManager objects. The
 * EntityManagerFactory creates and manages these objects in an object pool. The
 * creation of this factory however is very costly. This singleton creates one
 * of these objects that will be used throughout the project.
 * 
 * @author Anton Danneels
 * @version 1.0.0
 */
@Singleton
@Startup
public class EntityManagerSingleton
{
	private EntityManagerFactory emf;
	private EntityManager em;

	/**
	 * Initializes the EntityManagerFactory. Automatically called on startup.
	 */
	@PostConstruct
	public void init()
	{
		emf = Persistence.createEntityManagerFactory( "EJBProject" );
		em = emf.createEntityManager();
	}

	/**
	 * Retrieve an EntityManager from the factory. After usage, this object
	 * should be returned to the factory by calling the close() method.
	 * 
	 * @return A EntityManager object owned by the global EntityManagerFactory
	 */
	public EntityManager getEntityManager()
	{
		return emf.createEntityManager();
	}

	/**
	 * Destroys all EntityManager objects and closes the factory. Called when
	 * the program exists.
	 */
	@PreDestroy
	public void delete()
	{
		em.close();
		emf.close();
	}
}
