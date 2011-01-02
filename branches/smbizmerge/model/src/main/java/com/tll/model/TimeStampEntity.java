package com.tll.model;

import java.util.Date;


/**
 * An entity that contains audit information. The information currently stored
 * is the create/modify date and the create/modify user.
 * @author jpk
 */
public abstract class TimeStampEntity extends EntityBase implements ITimeStampEntity {

	private static final long serialVersionUID = 1800355868972602348L;

	private Date dateCreated;

	private Date dateModified;

	/*
	 * NOTE: we don't enforce a <code>null</code> check since the
	 * {@link EntityTimeStamper} handles it automatically. I.e.: this is a managed
	 * property.
	 * @return The automatically set date of creation.
	 */
	@Override
	@Managed
	public Date getDateCreated() {
		return dateCreated;
	}

	@Override
	public void setDateCreated(Date date) {
		this.dateCreated = date;
	}

	/*
	 * NOTE: we don't enforce a <code>null</code> check since the
	 * {@link EntityTimeStamper} handles it automatically. I.e.: this is a managed
	 * property.
	 * @return The automatically set date of modification.
	 */
	@Override
	@Managed
	public Date getDateModified() {
		return dateModified;
	}

	@Override
	public void setDateModified(Date date) {
		dateModified = date;
	}
}
