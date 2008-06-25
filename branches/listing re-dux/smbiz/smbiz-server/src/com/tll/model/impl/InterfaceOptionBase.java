package com.tll.model.impl;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;

import com.tll.model.NamedTimeStampEntity;

/**
 * Base class for interface options and interface option parameter definition
 * classes.
 * @author jpk
 */
@Entity
@Table(name = "iopd")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "record_type", discriminatorType = DiscriminatorType.STRING)
public abstract class InterfaceOptionBase extends NamedTimeStampEntity {

	public static final int MAXLEN_CODE = 50;
	public static final int MAXLEN_NAME = 50;
	public static final int MAXLEN_DESCRIPTION = 50;

	protected String code;
	protected String description;

	@Column
	@NotEmpty
	@Length(max = MAXLEN_NAME)
	public String getName() {
		return name;
	}

	/**
	 * @return Returns the code.
	 */
	@Column
	@NotEmpty
	@Length(max = MAXLEN_CODE)
	public String getCode() {
		return code;
	}

	/**
	 * @param code The code to set.
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return Returns the description.
	 */
	@Column
	@Length(max = MAXLEN_DESCRIPTION)
	public String getDescription() {
		return description;
	}

	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
}
