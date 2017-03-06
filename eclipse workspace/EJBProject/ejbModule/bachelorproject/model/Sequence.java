package bachelorproject.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the SEQUENCE database table.
 * 
 */
@Entity
@Table( name = "SEQUENCE" )
@NamedQuery( name = "Sequence.findAll", query = "SELECT s FROM Sequence s" )
public class Sequence implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Id
	@Column( name = "SEQ_NAME" )
	private String seqName;

	@Column( name = "SEQ_COUNT" )
	private BigDecimal seqCount;

	public Sequence()
	{
	}

	public String getSeqName()
	{
		return this.seqName;
	}

	public void setSeqName( String seqName )
	{
		this.seqName = seqName;
	}

	public BigDecimal getSeqCount()
	{
		return this.seqCount;
	}

	public void setSeqCount( BigDecimal seqCount )
	{
		this.seqCount = seqCount;
	}

}