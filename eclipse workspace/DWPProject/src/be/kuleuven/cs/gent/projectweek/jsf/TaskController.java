package be.kuleuven.cs.gent.projectweek.jsf;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import be.kuleuven.cs.gent.projectweek.data.Task;
import be.kuleuven.cs.gent.projectweek.data.TaskValidationException;
import be.kuleuven.cs.gent.projectweek.ejb.TaskEJBLocal;
import be.kuleuven.cs.gent.projectweek.logging.Loggable;

@Named
@ViewScoped
public class TaskController implements Serializable
{
	private static final long serialVersionUID = -3737221385235612830L;

	@EJB
	private TaskEJBLocal taskEJB;
	
	private Task task = new Task();

	@Loggable
	public String doCreateTask()
	{
		try
		{
			taskEJB.createTask( task );

			FacesContext.getCurrentInstance().addMessage( null,
					new FacesMessage( FacesMessage.SEVERITY_INFO, "Task created",
							"The task" + task.getDescription() + " has been created with id=" + task.getId() ) );
			return "viewTasks.xhtml?faces-redirect=true";

		}
		catch ( TaskValidationException e )
		{
			FacesContext.getCurrentInstance().addMessage( null, new FacesMessage( FacesMessage.SEVERITY_ERROR,
					"Failed to create task.", "Please enter correct/complete task details." ) );
			return null;
		}

	}

	@Loggable
	public void doFindTaskById()
	{
		task = taskEJB.findTaskById( task.getId() );
	}

	@Loggable
	public String doFileTaskReport()
	{
		try
		{
			taskEJB.fileTaskReport( task );

			FacesContext.getCurrentInstance().addMessage( null, new FacesMessage( FacesMessage.SEVERITY_INFO,
					"Task report filed", "The task report has been succesfully filed" ) );
			return "viewOpenTasks.xhtml?faces-redirect=true";

		}
		catch ( TaskValidationException e )
		{
			FacesContext.getCurrentInstance().addMessage( null, new FacesMessage( FacesMessage.SEVERITY_ERROR,
					"Failed to file task report.", "Please enter correct/complete task details." ) );
			return null;
		}

	}

	public Task getTask()
	{
		return task;
	}

	public void setTask( Task task )
	{
		this.task = task;
	}

	public List<Task> findAllTasks()
	{
		return taskEJB.findAllTasks();
	}

	public List<Task> findOpenTasks()
	{
		return taskEJB.findOpenTasks();
	}
}
