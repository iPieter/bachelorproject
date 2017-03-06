package bachelorproject.data;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

public class Task implements Serializable
{
	private Long id;

	@NotNull
	@Size( max = 15, min = 2 )
	private String patient;

	@NotNull
	@Size( max = 30, min = 2 )
	private String location;

	@NotNull
	@Size( min = 10 )
	private String description;

	@NotNull
	@Past
	private Date createdOn;

	@NotNull
	@Past( groups = FinishedTask.class )
	private Date date;

	@AssertFalse( groups = UnfinishedTask.class )
	@AssertTrue( groups = FinishedTask.class )
	private boolean completed;

	@NotNull( groups = FinishedTask.class )
	@Size( min = 2, groups = FinishedTask.class )
	@Null( groups = UnfinishedTask.class )
	private String completedBy;

	@NotNull( groups = FinishedTask.class )
	@Size( min = 10, groups = FinishedTask.class )
	@Null( groups = UnfinishedTask.class )
	private String report;

	public Task(Task task)
	{
		this.id = task.id;
		this.patient = task.patient;
		this.location = task.location;
		this.description = task.description;
		this.createdOn = task.createdOn;
		this.date = task.date;
		this.completed = task.completed;
		this.completedBy = task.completedBy;
		this.report = task.report;
	}

	public Task(Long id, String patient, String location, String description, Date date)
	{
		super();
		this.id = id;
		this.patient = patient;
		this.location = location;
		this.description = description;
		this.date = date;
		completed = false;
		createdOn = new Date( System.currentTimeMillis() );
	}

	public Task(String patient, String location, String description, Date date)
	{
		super();
		this.patient = patient;
		this.location = location;
		this.description = description;
		this.date = date;
		completed = false;
		createdOn = new Date( System.currentTimeMillis() );
	}

	public Task()
	{
		super();
		completed = false;
		createdOn = new Date( System.currentTimeMillis() );
	}

	public Long getId()
	{
		return id;
	}

	public void setId( Long id )
	{
		this.id = id;
	}

	public String getPatient()
	{
		return patient;
	}

	public void setPatient( String patient )
	{
		this.patient = patient;
	}

	public String getLocation()
	{
		return location;
	}

	public void setLocation( String location )
	{
		this.location = location;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription( String description )
	{
		this.description = description;
	}

	public Date getCreatedOn()
	{
		return createdOn;
	}

	public void setCreatedOn( Date createdOn )
	{
		this.createdOn = createdOn;
	}

	public Date getDate()
	{
		return date;
	}

	public void setDate( Date date )
	{
		this.date = date;
	}

	public boolean isCompleted()
	{
		return completed;
	}

	public void setCompleted( boolean completed )
	{
		this.completed = completed;
	}

	public String getReport()
	{
		return report;
	}

	public void setReport( String report )
	{
		this.report = report;
	}

	public String getCompletedBy()
	{
		return completedBy;
	}

	public void setCompletedBy( String completedBy )
	{
		completed = true;
		this.completedBy = completedBy;
	}
}
