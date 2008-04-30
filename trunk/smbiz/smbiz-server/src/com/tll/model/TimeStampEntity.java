package com.tll.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.NotNull;

/**
 * An entity that contains audit information. The information currently stored
 * is the create/modify date and the create/modify user.
 * 
 * @author jpk
 */
@MappedSuperclass
public abstract class TimeStampEntity extends EntityBase implements ITimeStampEntity {
  private Date dateCreated;
  private Date dateModified;

  @Column(name = "date_created", updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  @NotNull
  public Date getDateCreated() {
    return dateCreated;
  }

  public void setDateCreated(Date date) {
    this.dateCreated = date;
  }

  @Column(name = "date_last_modified")
  @Temporal(TemporalType.TIMESTAMP)
  @NotNull
  public Date getDateModified() {
    return dateModified;
  }

  public void setDateModified(Date date) {
    dateModified = date;
  }

  @Override
  protected ToStringBuilder toStringBuilder() {
    return super.toStringBuilder()
    .append("dateCreated", getDateCreated())
    .append("dateModified", getDateModified());
  }
}
