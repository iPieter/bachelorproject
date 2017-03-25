package bachelorproject.model.constraint_engine;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import bachelorproject.model.User;

/**
 * 	This class is the general collection of constraints.
 *  When the train is riding, certain conditions may not be met 
 *  or the driving is unsafe.
 *  @author Anton Danneels
 * */

@Entity
@NamedQueries(
{
	@NamedQuery( name= Constraint.FIND_ALL, query = "SELECT c FROM Constraint c" )
})
@Table( name = "constraint_table" )
public class Constraint implements Serializable
{
	private static final long serialVersionUID = -4700824709631898631L;

	public static final String FIND_ALL = "Constraint.findAll";
	
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	private int id;
	
	@NotNull
	@Lob
	public String name;
	
	@NotNull
	public User creator;
	
	@OneToMany(  fetch = FetchType.LAZY  )
	public List<ConstraintElement> constraints;
	
	public Constraint()
	{
	}

	public Constraint( String name, User creator )
	{
		this.name = name;
		this.creator = creator;
	}

	/**
	 * @return the id
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId( int id )
	{
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName( String name )
	{
		this.name = name;
	}

	/**
	 * @return the creator
	 */
	public User getCreator()
	{
		return creator;
	}

	/**
	 * @param creator the creator to set
	 */
	public void setCreator( User creator )
	{
		this.creator = creator;
	}
	
}
