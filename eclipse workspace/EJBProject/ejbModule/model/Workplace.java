package model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;


/**
 * The persistent class for the Workplace database table.
 * 
 */
@Entity
@NamedQuery(name="Workplace.findAll", query="SELECT w FROM Workplace w")
public class Workplace implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	@Lob
	private String name;

	@OneToMany(fetch = FetchType.LAZY)
	private List<TrainCoach> traincoaches;
	
	public Workplace() {
	}

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

}