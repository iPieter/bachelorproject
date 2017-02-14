package be.kuleuven.cs.gent.projectweek.ejb;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.LocalBean;

import be.kuleuven.cs.gent.projectweek.data.Task;
import be.kuleuven.cs.gent.projectweek.data.TaskValidationException;

@Local
public interface TaskEJBLocal {

	public void createTask(Task task) throws TaskValidationException;
	
	public List<Task> findAllTasks();
	
	public Task findTaskById(Long id);
	
	public void fileTaskReport(Task task) throws TaskValidationException;
	
	public List<Task> findOpenTasks();
	
	public void remove(Task task);
}
