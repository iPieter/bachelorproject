package be.kuleuven.cs.gent.projectweek.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;


/**
 * The persistent class for the Issue database table.
 * 
 */
@Entity
@NamedQuery(name="Issue.findAll", query="SELECT i FROM Issue i")
public class Issue implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	@Lob
	private String descr;

	@Enumerated(EnumType.STRING)
	private IssueStatus status;

	@OneToMany(fetch = FetchType.LAZY)
	private List<IssueAsset> assets;
	
	@OneToOne(fetch = FetchType.LAZY)
	private User mechanic;
	
	@OneToOne(fetch = FetchType.LAZY)
	private User Operator;
	
	@OneToOne(fetch = FetchType.LAZY)
	private ProcessedSensorData data;
	
	public Issue() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDesc() {
		return this.descr;
	}

	public void setDesc(String desc) {
		this.descr = desc;
	}

	public IssueStatus getStatus() {
		return this.status;
	}

	public void setStatus(IssueStatus status) {
		this.status = status;
	}

}