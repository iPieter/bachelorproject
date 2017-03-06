package bachelorproject.ejb;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import bachelorproject.data.FinishedTask;
import bachelorproject.data.StaticTaskRepository;
import bachelorproject.data.Task;
import bachelorproject.data.TaskValidationException;
import bachelorproject.data.UnfinishedTask;

@Stateless
@Local( TaskEJBLocal.class )
public class TaskEJB implements TaskEJBLocal
{

	@Resource
	private SessionContext ctx;

	@Inject
	private Validator validator;

	private StaticTaskRepository taskRepo = StaticTaskRepository.getInstance();

	// @RolesAllowed({"manager"})
	public void createTask( Task task ) throws TaskValidationException
	{

		Set<ConstraintViolation<Task>> violations = validator.validate( task, Default.class, UnfinishedTask.class );
		for ( ConstraintViolation<Task> violation : violations )
		{
			System.out.println( "Task validation error: " + violation.getMessage() );
		}
		if ( !violations.isEmpty() ) throw new TaskValidationException();

		taskRepo.createTask( task );
	}

	// @RolesAllowed({"manager"})
	public List<Task> findAllTasks()
	{
		return taskRepo.getAllTasks();
	}

	// @PermitAll
	public Task findTaskById( Long id )
	{
		// employee can only view open tasks
		Task task = taskRepo.find( id );
		if ( task.isCompleted() && ctx.isCallerInRole( "employee" ) )
			throw new SecurityException( "Employees can only view open tasks." );
		return taskRepo.find( id );
	}

	// @RolesAllowed({"employee"})
	public void fileTaskReport( Task task ) throws TaskValidationException
	{
		String employee = ctx.getCallerPrincipal().getName();
		System.out.println( "employee: " + employee );
		task.setCompletedBy( employee );

		Set<ConstraintViolation<Task>> violations = validator.validate( task, Default.class, FinishedTask.class );
		for ( ConstraintViolation<Task> violation : violations )
		{
			System.out.println( "Task validation error: " + violation.getMessage() );
		}
		if ( !violations.isEmpty() ) throw new TaskValidationException();

		taskRepo.merge( task );
	}

	public List<Task> findOpenTasks()
	{
		List<Task> openTasks = new ArrayList<>( 15 );
		List<Task> allTasks = taskRepo.getAllTasks();
		for ( Task task : allTasks )
		{
			if ( !task.isCompleted() ) openTasks.add( task );
		}
		return openTasks;

	}

	@Override
	public void remove( Task task )
	{
		taskRepo.remove( task );
	}
}
