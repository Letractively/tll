package com.tll.model;

import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.validation.constraints.NotNull;

import com.tll.model.schema.Managed;

/**
 * An entity that contains audit information. The information currently stored
 * is the create/modify date and the create/modify user.
 * @author jpk
 */
@PersistenceCapable
public abstract class TimeStampEntity extends EntityBase implements ITimeStampEntity {

	private static final long serialVersionUID = 1800355868972602348L;

	@Persistent
	@Managed
	@NotNull
	private Date dateCreated;

	@Persistent
	@Managed
	@NotNull
	private Date dateModified;

	/*
	 * NOTE: we don't enforce a <code>null</code> check since the
	 * {@link EntityTimeStamper} handles it automatically. I.e.: this is a managed
	 * property.
	 * @return The automatically set date of creation.
	 */
	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date date) {
		this.dateCreated = date;
	}

	/*
	 * NOTE: we don't enforce a <code>null</code> check since the
	 * {@link EntityTimeStamper} handles it automatically. I.e.: this is a managed
	 * property.
	 * @return The automatically set date of modification.
	 */
	public Date getDateModified() {
		return dateModified;
	}

	public void setDateModified(Date date) {
		dateModified = date;
	}
}
