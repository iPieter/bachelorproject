package bachelorproject.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the IssueAssets database table.
 * 
 */
@Entity
@NamedQuery(name="IssueAsset.findAll", query="SELECT i FROM IssueAsset i")
public class IssueAsset implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private int descr;

	@Lob
	private String location;

	public IssueAsset() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDesc() {
		return this.descr;
	}

	public void setDesc(int desc) {
		this.descr = desc;
	}

	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}