package controllers;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import bachelorproject.ejb.IssueEJB;
import bachelorproject.model.issue.Issue;
import bachelorproject.model.issue.IssueStatus;
import bachelorproject.model.user.User;
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
	
	/**
	 * Returns a List of Issue objects for the current User(UserRole=OPERATOR).
	 * The list contains an overview of the Issues that the Operator assigned.
	 * 
	 * @author Matthias De Lange
	 * @version 0.0.1
	 * @return List of Issue objects
	 * @see IssueEJB
	 */
	public List<Issue> findOperatorAssignedIssues(){
		return issueEJB.findOperatorIssues( userService.getUser().getId(), IssueStatus.ASSIGNED  );
	}
	
	/**
	 * The user avatar is a non-guessable file, this function returns the name
	 * of the file. Appending it with it's image format (".png", ".jpg") will 
	 * result in the correct file.
	 * <p>
	 * The REST api will do this for you. So just call <code>/rest/assets/user/#</code>.
	 * 
	 * @author Pieter Delobelle
	 * @version 0.0.1
	 * @return String with the non-guessable location of the user avatar. 
	 */
	public String getAvatar()
	{
		User u = userService.getUser();
		if (u != null)
		{
			return u.getImageHash();
		}
		return "";		
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
