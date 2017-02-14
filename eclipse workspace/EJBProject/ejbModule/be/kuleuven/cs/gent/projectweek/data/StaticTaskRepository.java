package be.kuleuven.cs.gent.projectweek.data;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class StaticTaskRepository {
	private static StaticTaskRepository taskRepo = null;

	private List<Task> tasks;

	private StaticTaskRepository()
	{
		tasks = new ArrayList<>();

		DateFormat formatter = new SimpleDateFormat("dd/MM/yy");

		try{

			tasks.add(new Task((long) 0, "Hilde", "Gent", "medicatie geven", formatter.parse("01/1/2016")));
			tasks.add(new Task((long) 1, "Bert", "Lokeren", "wondverzorging", formatter.parse("05/1/2016")));
			tasks.add(new Task((long) 2, "Jos", "Waregem", "boodschappen doen", formatter.parse("07/6/2016")));
			tasks.add(new Task((long) 3, "Dirk", "Nieuwpoort", "koken", formatter.parse("07/6/2016")));

		}catch(ParseException pe)
		{
			pe.printStackTrace();
			System.out.println("Invalid static input data error.");
		}
	}

	public static StaticTaskRepository getInstance()
	{
		if(taskRepo == null)
			taskRepo = new StaticTaskRepository();
		return taskRepo;
	}

	public Task find(Long id)
	{
		Task task = null;
		for(Task taskIt : tasks){
			if(taskIt.getId().equals(id))
				task = taskIt;
		}
		return new Task(task);
	}

	public void createTask(Task task)
	{
		task.setId((long) tasks.size());
		tasks.add(task);
	}

	public void merge(Task task)
	{
		for(Task taskIt : tasks){
			if(taskIt.getId().equals(task.getId()))
			{
				taskIt.setDescription(task.getDescription());
				taskIt.setCompleted(task.isCompleted());
				taskIt.setCreatedOn(task.getCreatedOn());
				taskIt.setDate(task.getDate());
				taskIt.setLocation(task.getLocation());
				taskIt.setPatient(task.getPatient());
				taskIt.setReport(task.getReport());
				taskIt.setCompletedBy(task.getCompletedBy());
				
			}
		}
	}

	public void remove(Task task)
	{
		for(Task taskIt : tasks){
			if(taskIt.getId().equals(task.getId()))
				tasks.remove(taskIt);
		}
	}

	public List<Task> getAllTasks()
	{
		ArrayList<Task> tsks = new ArrayList<>();
		for(Task task : tasks)
			tsks.add(new Task(task));
		return tsks;
	}
}
