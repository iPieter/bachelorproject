package be.kuleuven.cs.gent.projectweek.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


/**
 * The persistent class for the Workplace database table.
 * 
 */
@Entity
@NamedQueries(
{
	@NamedQuery(name="Workplace.findAll", query="SELECT w FROM Workplace w"),
	@NamedQuery(name="Workplace.findWorkplaceByTraincoachID", query= "SELECT w FROM Workplace w JOIN w.traincoaches t WHERE t.id = :id" ),
	@NamedQuery(name="Workplace.findWorkers", query = "SELECT m FROM Workplace w JOIN w.mechanics m WHERE w.id = :id"),
	@NamedQuery(name="Workplace.findByData", query = "SELECT w FROM Workplace w WHERE w.name = :name" )
})
public class Workplace implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Lob
	@NotNull
	private String name;

	@OneToMany(fetch = FetchType.EAGER)
	private List<TrainCoach> traincoaches = new ArrayList<TrainCoach>();
	
	@OneToMany(fetch = FetchType.LAZY)
	private List<User> mechanics;
	
	public Workplace() {
	}

	//GETTERS & SETTTERS
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public List<TrainCoach> getTraincoaches() {
		return this.traincoaches;
	}

	public void setTraincoaches(List<TrainCoach> traincoaches) {
		this.traincoaches = traincoaches;
	}
	
	
}