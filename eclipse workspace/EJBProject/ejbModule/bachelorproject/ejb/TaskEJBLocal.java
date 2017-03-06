package bachelorproject.ejb;

import java.util.List;

import javax.ejb.Local;

import bachelorproject.data.Task;
import bachelorproject.data.TaskValidationException;

@Local
public interface TaskEJBLocal
{

	public void createTask( Task task ) throws TaskValidationException;

	public List<Task> findAllTasks();

	public Task findTaskById( Long id );

	public void fileTaskReport( Task task ) throws TaskValidationException;

	public List<Task> findOpenTasks();

	public void remove( Task task );
}
