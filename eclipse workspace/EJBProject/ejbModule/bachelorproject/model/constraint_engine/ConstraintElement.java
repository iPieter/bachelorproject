package bachelorproject.model.constraint_engine;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import bachelorproject.constraint_engine.ConstraintEngine;

/**
 * 	A ConstraintElement is a specific constraint that should be tested.
 *  @author Anton Danneels
 * */
@Entity
@Table( name = "constraintelement_table" )
public abstract class ConstraintElement  implements Serializable
{
	private static final long serialVersionUID = -548271107464821551L;
	
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	protected int id;
	
	/**
	 *  The ConstraintEngine uses a visitor pattern to implemented evaluating ConstraintElements.
	 * */
	@Transient
	public abstract boolean visit( ConstraintEngine ceVisitor );
	
	@Transient
	public abstract String toString();
	
	/**
	 * @return the id
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId( int id )
	{
		this.id = id;
	}

}
