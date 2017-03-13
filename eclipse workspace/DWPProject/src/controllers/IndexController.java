package controllers;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

import bachelorproject.ejb.IssueEJB;
import bachelorproject.model.Issue;
import bachelorproject.services.UserService;

public class IndexController implements Serializable{

	private static final long serialVersionUID = 426047598496441124L;
	
	@EJB
	private IssueEJB issueEJB;
	
	@Inject
	private UserService userService;
	
	//Returns how many actions the user contributed to: #solved issues, #assigned tasks
	public Map<String,String> getUserHistoryStatistics(){
		HashMap result=new HashMap<String,String>();
		//TODO manier om statistics door te geven
		return result;
	}
	
	//Returns recent activity of the user
	public Map<String,String> getUserRecentStatistics(){
		HashMap result=new HashMap<String,String>();
		//TODO manier om statistics door te geven
		return result;
	}
	
	/**
	 * Returns a List of Issue objects for the current User(UserRole=OPERATOR).
	 * The list contains an overview of the Issues that the Operator assigned, 
	 * that are recently closed by a Mechanic. 
	 * 
	 * @author Matthias De Lange
	 * @version 0.0.1
	 * @return List of Issue objects
	 * @see IssueEJB
	 */
	public List<Issue> findOperatorClosedIssues(){
		return issueEJB.findOperatorClosedIssues( userService.getUser().getId() );
	}
	
	/**
	 * Returns a List of Issue objects for the current User(UserRole=OPERATOR).
	 * The list contains an overview of the Issues that the Operator assigned, 
	 * that are recently in progress by a Mechanic. 
	 * 
	 * @author Matthias De Lange
	 * @version 0.0.1
	 * @return List of Issue objects
	 * @see IssueEJB
	 */
	public List<Issue> findOperatorInProgressIssues(){
		return issueEJB.findOperatorInProgressIssues( userService.getUser().getId() );
	}
	
	// GETTERS & SETTERS

}
