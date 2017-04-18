package bachelorproject.ejb;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import bachelorproject.model.user.Token;
import bachelorproject.model.user.User;

/**
 * EJB for Token object
 * 
 * @author Pieter Delobelle
 * @version 1.0.0
 */
@Stateless
@Local
public class TokenEJB
{
	@Inject
	private EntityManagerSingleton ems;

	public void createToken(Token t)
	{
		EntityManager em = ems.getEntityManager();
		em.getTransaction().begin();

		em.persist(t);

		em.getTransaction().commit();
		em.close();
	}

	public Token findTokenByToken(String token)
	{
		EntityManager em = ems.getEntityManager();
		
		TypedQuery<Token> q = em.createNamedQuery( Token.FIND_BY_TOKEN, Token.class );
		
		q.setParameter( "token", token );

		Token result;
		try
		{
			result = q.getSingleResult();

		}
		catch ( NoResultException e )
		{
			em.close();
			return null;
		}
		
		em.close();
		
		return result;
	}
}
