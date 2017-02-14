package be.kuleuven.cs.gent.projectweek.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement
@XmlSeeAlso( Task.class )
public class Tasks extends ArrayList<Task>
{

	public Tasks()
	{
		super();
	}

	public Tasks(Collection<? extends Task> c)
	{
		super( c );
	}

	@XmlElement( name = "task" )
	public List<Task> getTasks()
	{
		return this;
	}

	public void setTasks( List<Task> tasks )
	{
		this.addAll( tasks );
	}
}