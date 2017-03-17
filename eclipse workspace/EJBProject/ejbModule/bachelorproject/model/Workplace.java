package bachelorproject.model;

import java.io.Serializable;
import java.util.ArrayList;
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
import javax.validation.constraints.NotNull;

/**
 * The persistent class for the Workplace database table.
 * 
 */
@Entity
@NamedQueries(
{
	@NamedQuery( name = Workplace.FIND_ALL, query = "SELECT w FROM Workplace w" ),
	@NamedQuery( name = Workplace.FIND_BY_TRAINCOACH_ID, query = "SELECT w FROM Workplace w JOIN w.traincoaches t WHERE t.id = :id" ),
	@NamedQuery( name = Workplace.FIND_BY_WORKPLACE_ID, query = "SELECT m FROM Workplace w JOIN w.mechanics m WHERE w.id = :id " ),
	@NamedQuery( name = Workplace.FIND_BY_DATA, query = "SELECT w FROM Workplace w WHERE w.name = :name" ) 
} )
public class Workplace implements Serializable
{
	public static final String FIND_ALL = "Workplace.findAll";
	public static final String FIND_BY_TRAINCOACH_ID = "Workplace.findWorkplaceByTraincoachID";
	public static final String FIND_BY_WORKPLACE_ID = "Workplace.findMechanicsByWorkplaceId";
	public static final String FIND_BY_DATA = "Workplace.findByData";
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	private int id;

	@Lob
	@NotNull
	private String name;

	@OneToMany( fetch = FetchType.EAGER )
	private List<TrainCoach> traincoaches = new ArrayList<TrainCoach>();

	@OneToMany( fetch = FetchType.LAZY )
	private List<User> mechanics;

	public Workplace()
	{
	}

	// GETTERS & SETTTERS
	public int getId()
	{
		return this.id;
	}

	public void setId( int id )
	{
		this.id = id;
	}

	public String getName()
	{
		return this.name;
	}

	public void setName( String name )
	{
		this.name = name;
	}

	public List<TrainCoach> getTraincoaches()
	{
		return this.traincoaches;
	}

	public void setTraincoaches( List<TrainCoach> traincoaches )
	{
		this.traincoaches = traincoaches;
	}

	/**
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @return the mechanics
	 */
	public List<User> getMechanics()
	{
		return mechanics;
	}

	/**
	 * @author Pieter Delobelle
	 * @version 1.0.0
	 * @param mechanics the mechanics to set
	 */
	public void setMechanics(List<User> mechanics)
	{
		this.mechanics = mechanics;
	}
	
	

}