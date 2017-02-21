package model;

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

	@Lob
	private String status;

	@OneToMany(fetch = FetchType.LAZY)
	private List<IssueAsset> assets;
	
	@OneToOne
	private User mechanic;
	
	@OneToOne
	private User Operator;
	
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

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}