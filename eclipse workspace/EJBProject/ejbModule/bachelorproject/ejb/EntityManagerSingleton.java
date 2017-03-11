package bachelorproject.ejb;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Singleton
@Startup
public class EntityManagerSingleton
{
	private EntityManagerFactory emf;
	private EntityManager em;
	
	@PostConstruct
	public void init()
	{
		emf = Persistence.createEntityManagerFactory( "EJBProject" );
		em = emf.createEntityManager();
	}
	
	public EntityManager getEntityManager()
	{
		return em;
	}
	
	@PreDestroy
	public void delete()
	{
		em.close();
		emf.close();
	}
}
