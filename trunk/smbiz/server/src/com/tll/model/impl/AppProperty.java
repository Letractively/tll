package com.tll.model.impl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;

import com.tll.model.IEntity;
import com.tll.model.NamedEntity;

/**
 * AppProperty
 * @author jpk TODO should we eliminate this and just put in config.properties
 *         file???
 */
@Entity
@Table(name = "app_property")
public class AppProperty extends NamedEntity {

	private static final long serialVersionUID = 601145261743504878L;

	public static final int MAXLEN_NAME = 128;
	public static final int MAXLEN_VALUE = 255;

	private String value;

	public Class<? extends IEntity> entityClass() {
		return AppProperty.class;
	}

	@Column
	@NotEmpty
	@Length(max = MAXLEN_NAME)
	public String getName() {
		return name;
	}

	@Column
	@NotEmpty
	@Length(max = MAXLEN_VALUE)
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}