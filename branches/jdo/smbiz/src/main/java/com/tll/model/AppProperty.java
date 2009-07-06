package com.tll.model;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import org.hibernate.validation.constraints.Length;
import org.hibernate.validation.constraints.NotEmpty;

import com.tll.model.schema.BusinessKeyDef;
import com.tll.model.schema.BusinessObject;

/**
 * AppProperty
 * @author jpk TODO should we eliminate this and just put in config.properties
 *         file???
 */
@PersistenceCapable
@BusinessObject(businessKeys = @BusinessKeyDef(name = "Name", properties = { "name" }))
public class AppProperty extends NamedEntity {

	private static final long serialVersionUID = 601145261743504878L;

	public static final int MAXLEN_NAME = 128;
	public static final int MAXLEN_VALUE = 255;

	@Persistent
	private String value;

	public Class<? extends IEntity> entityClass() {
		return AppProperty.class;
	}

	@NotEmpty
	@Length(max = MAXLEN_NAME)
	public String getName() {
		return name;
	}

	@NotEmpty
	@Length(max = MAXLEN_VALUE)
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
