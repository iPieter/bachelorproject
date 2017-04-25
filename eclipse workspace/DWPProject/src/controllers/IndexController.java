package controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import bachelorproject.ejb.IssueEJB;
import bachelorproject.ejb.WorkplaceEJB;
import bachelorproject.model.Workplace;
import bachelorproject.model.issue.Issue;
import bachelorproject.model.issue.IssueStatus;
import bachelorproject.model.user.User;
import bachelorproject.services.UserService;

@Named
@SessionScoped
public class IndexController implements Serializable
{

	private static final long serialVersionUID = 426047598496441124L;

	@EJB
	private IssueEJB issueEJB;

	@EJB
	private WorkplaceEJB workplaceEJB;

	@Inject
	private UserService userService;

	private Issue currentIssue;

	private User currentIssueMechanic;
	
	/**
	 * Returnes a list with all the issue objects for the current user, assuming
	 * he/she is a operator.
	 * 
	 * If no issues are found, an empty list is returned.
	 * 
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @return A list object with all issues for an operator.
	 */
	public List<Issue> findOperatorAllIssues()
	{
		List<Issue> issues = new ArrayList<>();

		for (IssueStatus is : IssueStatus.values())
		{
			issues.addAll(issueEJB.findOperatorIssues(userService.getUser().getId(), is));
		}

		return issues;
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
	public List<Issue> findOperatorClosedIssues()
	{
		return issueEJB.findOperatorIssues(userService.getUser().getId(), IssueStatus.CLOSED);
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
	public List<Issue> findOperatorInProgressIssues()
	{
		return issueEJB.findOperatorIssues(userService.getUser().getId(), IssueStatus.IN_PROGRESS);
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
	public List<Issue> findOperatorAssignedIssues()
	{
		return issueEJB.findOperatorIssues(userService.getUser().getId(), IssueStatus.ASSIGNED);
	}

	/**
	 * The user avatar is a non-guessable file, this function returns the name
	 * of the file. Appending it with it's image format (".png", ".jpg") will
	 * result in the correct file.
	 * <p>
	 * The REST api will do this for you. So just call
	 * <code>/rest/assets/user/#</code>.
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
	public UserService getUserService()
	{
		return userService;
	}

	/**
	 * @param userService
	 *            the userService to set
	 */
	public void setUserService(UserService userService)
	{
		this.userService = userService;
	}

	/**
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @return the currentIssue
	 */
	public Issue getCurrentIssue()
	{
		return currentIssue;
	}

	/**
	 * Sets the current issue and also sets a currentIssueMechanic.
	 * 
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @param currentIssue the currentIssue to set
	 */
	public void setCurrentIssue(Issue currentIssue)
	{
		this.currentIssue = currentIssue;
		List<User> mechanics = getMechanicsOfWorkplaceByCurrentIssue();
		
		if (mechanics.size() > 0 ) 
		{
			this.currentIssueMechanic = getMechanicsOfWorkplaceByCurrentIssue().get(0);
			
		}
	}

	/**
	 * This function lists all the mechanics in a workplace, by using the
	 * traincoach of the CurrentIssue object.
	 * 
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @return A List of mechanics.
	 */
	public List<User> getMechanicsOfWorkplaceByCurrentIssue()
	{

		if (currentIssue != null)
		{
			try
			{
				Workplace w = workplaceEJB.findWorkplaceByTraincoachID(currentIssue.getData().getTraincoach().getId())
						.get(0);
				return w.getMechanics();
			} catch (IndexOutOfBoundsException ex)
			{
				System.out.println("Current issue is null.");
			}
		}
		return new LinkedList<>();
	}
	
	/**
	 * Changes the issue status of the currentIssue.
	 * 
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 */
	public void assignIssue()
	{
		
		System.out.println(currentIssue.getMechanic());
		//set assigned status
		this.currentIssue.setStatus(IssueStatus.ASSIGNED);
		
		//set assigned time to now
		this.currentIssue.setAssignedTime(new Date());
		
		//set the mechanic
		this.currentIssue.setMechanic(currentIssueMechanic);
		
		//persist issue
		issueEJB.updateIssue(this.currentIssue);
	}

	/**
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @return the currentIssueMechanic
	 */
	public User getCurrentIssueMechanic()
	{
		return currentIssueMechanic;
	}

	/**
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @param currentIssueMechanic the currentIssueMechanic to set
	 */
	public void setCurrentIssueMechanic(User currentIssueMechanic)
	{
		this.currentIssueMechanic = currentIssueMechanic;
	}

	
}
