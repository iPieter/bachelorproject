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
 * The persistent class for the TrainCoach database table.
 * 
 */
@Entity
@NamedQueries(
{ @NamedQuery( name = TrainCoach.FIND_ALL, query = "SELECT t FROM TrainCoach t" ),
		@NamedQuery( name = TrainCoach.FIND_ALL_NEEDS_REVIEW, query = "SELECT t FROM Workplace w JOIN w.traincoaches t WHERE t.needsReview = true AND w.id = :id" ),
		@NamedQuery( name = TrainCoach.FIND_BY_DATA, query = "SELECT t FROM TrainCoach t WHERE t.name = :name AND t.type = :type AND t.constructor = :constructor" ) } )
public class TrainCoach implements Serializable
{

	public static final String FIND_ALL = "TrainCoach.findAll";
	public static final String FIND_BY_DATA = "TrainCoach.findByData";
	public static final String FIND_ALL_NEEDS_REVIEW = "TrainCoach.findAllNeedsReview";

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	private int id;

	@Lob
	private String constructor;

	@Lob
	@NotNull
	private String name;

	@Lob
	@NotNull
	private String type;

	@NotNull
	private boolean needsReview;

	public TrainCoach()
	{
	}

	/**
	 * @return the id
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId( int id )
	{
		this.id = id;
	}

	/**
	 * @return the constructor
	 */
	public String getConstructor()
	{
		return constructor;
	}

	/**
	 * @param constructor
	 *            the constructor to set
	 */
	public void setConstructor( String constructor )
	{
		this.constructor = constructor;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName( String name )
	{
		this.name = name;
	}

	/**
	 * @return the type
	 */
	public String getType()
	{
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType( String type )
	{
		this.type = type;
	}

	/**
	 * @return the needsReview
	 */
	public boolean isNeedsReview()
	{
		return needsReview;
	}

	/**
	 * @param needsReview
	 *            the needsReview to set
	 */
	public void setNeedsReview( boolean needsReview )
	{
		this.needsReview = needsReview;
	}
}