package controllers;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import bachelorproject.ejb.IssueEJB;
import bachelorproject.model.Issue;
import bachelorproject.model.IssueStatus;
import bachelorproject.services.UserService;

@Named
@SessionScoped
public class IndexController implements Serializable{

	private static final long serialVersionUID = 426047598496441124L;
	
	@EJB
	private IssueEJB issueEJB;
	
	@Inject
	private UserService userService;
	
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
		return issueEJB.findOperatorIssues( userService.getUser().getId(), IssueStatus.CLOSED );
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
		return issueEJB.findOperatorIssues( userService.getUser().getId(), IssueStatus.IN_PROGRESS  );
	}
	
	// GETTERS & SETTERS	
	/**
	 * @return the userService
	 */
	public UserService getUserService() {
		return userService;
	}

	/**
	 * @param userService the userService to set
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
