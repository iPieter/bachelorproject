package bachelorproject.model.constraint_engine;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 	A ConstraintElement is a specific constraint that should be tested.
 *  @author Anton Danneels
 * */
@Entity
@Table( name = "constraintelement_table" )
public abstract class ConstraintElement
{
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	protected int id;
}
