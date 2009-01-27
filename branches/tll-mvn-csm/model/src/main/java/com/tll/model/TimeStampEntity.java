package com.tll.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.NotNull;

import com.tll.model.schema.Managed;

/**
 * An entity that contains audit information. The information currently stored
 * is the create/modify date and the create/modify user.
 * @author jpk
 */
@MappedSuperclass
@EntityListeners(value = TimeStampEntity.EntityTimeStamper.class)
public abstract class TimeStampEntity extends EntityBase implements ITimeStampEntity {

	private static final long serialVersionUID = 1800355868972602348L;

	/**
	 * EntityTimeStamper - JPA entity listener for setting the timestamping
	 * related entity fields.
	 * @author jpk
	 */
	public static final class EntityTimeStamper {

		@PrePersist
		@PreUpdate
		public void onCreate(ITimeStampEntity e) {
			final Date now = new Date();
			if(e.isNew()) e.setDateCreated(now);
			e.setDateModified(now);
		}

	}

	private Date dateCreated;
	private Date dateModified;

	@Column(name = "date_created", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	@Managed
	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date date) {
		this.dateCreated = date;
	}

	@Column(name = "date_last_modified")
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	@Managed
	public Date getDateModified() {
		return dateModified;
	}

	public void setDateModified(Date date) {
		dateModified = date;
	}
}
