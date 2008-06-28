package com.tll.model;

import java.util.Date;

/**
 * Base interface for entities that require date created/modified properties.
 * @author jpk
 */
public interface ITimeStampEntity extends IEntity {

	/**
	 * @return The date created
	 */
	Date getDateCreated();

	/**
	 * @param date
	 */
	void setDateCreated(Date date);

	/**
	 * @return The date modified
	 */
	Date getDateModified();

	/**
	 * @param date
	 */
	void setDateModified(Date date);
}
