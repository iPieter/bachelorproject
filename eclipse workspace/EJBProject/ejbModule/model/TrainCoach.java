package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the TrainCoach database table.
 * 
 */
@Entity
@NamedQuery(name="TrainCoach.findAll", query="SELECT t FROM TrainCoach t")
public class TrainCoach implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	@Lob
	private String conductor;

	@Lob
	private String name;

	@Lob
	private String type;

	public TrainCoach() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getConductor() {
		return this.conductor;
	}

	public void setConductor(String conductor) {
		this.conductor = conductor;
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