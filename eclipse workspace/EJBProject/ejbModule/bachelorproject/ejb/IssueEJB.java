package bachelorproject.ejb;

import javax.ejb.Stateless;
import javax.inject.Named;

/**
 * 	Defines the Entity Java Bean for the Issue Entity.
 *  <p>
 *  This class allows for the controller to manipulate and fetch specific
 *  Issue instances. It validates new objects and handles 
 *  errors when, for example, no entries in the database exist.
 *  
 * */
@Named
@Stateless
public class IssueEJB
{

}
