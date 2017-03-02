package be.kuleuven.cs.gent.projectweek.model;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;


/**
 * The persistent class for the TrainCoach database table.
 * 
 */
@Entity
@NamedQueries(
{
	@NamedQuery(name="TrainCoach.findAll", query="SELECT t FROM TrainCoach t"),
	@NamedQuery(name="TrainCoach.findByData", query = "SELECT t FROM TrainCoach t WHERE t.name = :name AND t.type = :type AND t.conductor = :constructor ")
})

public class TrainCoach implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Lob
	private String constructor;

	@Lob
	@NotNull
	private String name;

	@Lob
	@NotNull
	private String type;

	public TrainCoach() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getConstructor() {
		return this.constructor;
	}

	public void setConstructor(String conductor) {
		this.constructor = conductor;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

}