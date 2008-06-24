package com.tll.model.impl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;

import com.tll.model.IEntity;
import com.tll.model.NamedEntity;
import com.tll.model.key.BusinessKey;
import com.tll.model.key.BusinessKeyDefinition;
import com.tll.model.key.IBusinessKeyDefinition;

/**
 * AppProperty
 * @author jpk TODO should we eliminate this and just put in config.properties
 *         file???
 */
@Entity
@Table(name = "app_property")
public class AppProperty extends NamedEntity {

	private static final long serialVersionUID = 601145261743504878L;

	public static final IBusinessKeyDefinition NameBk =
			new BusinessKeyDefinition(AppProperty.class, "Name", new String[] { "name" });

	public static final int MAXLEN_NAME = 128;
	public static final int MAXLEN_VALUE = 255;

	protected String value;

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

	@Override
	public BusinessKey[] getBusinessKeys() {
		return new BusinessKey[] { new BusinessKey(NameBk, new Object[] { getName() }) };
	}
}
