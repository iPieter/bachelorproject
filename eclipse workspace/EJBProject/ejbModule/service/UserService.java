package service;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class UserService
{
	@PersistenceContext(unitName="EJBProject")
	private EntityManager em;
}
