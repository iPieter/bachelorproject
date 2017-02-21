package model;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.util.Date;


/**
 * The persistent class for the ProcessedSensorData database table.
 * 
 */
@Entity
@NamedQuery(name="ProcessedSensorData.findAll", query="SELECT p FROM ProcessedSensorData p")
public class ProcessedSensorData implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	@Lob
	private String location;

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date time;

	@Lob
	private String track;

	@OneToOne(fetch = FetchType.LAZY)
	@NotNull
	private TrainCoach traincoach;
	
	public ProcessedSensorData() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Date getTime() {
		return this.time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getTrack() {
		return this.track;
	}

	public void setTrack(String track) {
		this.track = track;
	}

}