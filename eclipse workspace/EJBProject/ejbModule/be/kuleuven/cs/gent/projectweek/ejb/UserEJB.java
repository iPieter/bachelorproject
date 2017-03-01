package be.kuleuven.cs.gent.projectweek.ejb;

import java.util.Date;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import be.kuleuven.cs.gent.projectweek.model.User;
import be.kuleuven.cs.gent.projectweek.model.UserRole;

@Stateless
@Local
public class UserEJB
{
	
	public void createUser(User user)
	{
		
	}
	
	public User findUserByEmail(String email)
	{
		EntityManagerFactory emf =  Persistence.createEntityManagerFactory("EJBProject");
		EntityManager em = emf.createEntityManager();
		
		TypedQuery<User> q = em.createNamedQuery(User.FIND_BY_EMAIL, User.class);
		q.setParameter("email", email);
		
		User result = q.getSingleResult();
		
		em.close();
		emf.close();
		
		return result;
	}

}
