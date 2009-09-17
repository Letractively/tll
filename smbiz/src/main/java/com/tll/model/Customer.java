package com.tll.model;

import com.tll.model.schema.Extended;

/**
 * The Customer entity
 * @author jpk
 */
@Extended
public class Customer extends Account {

	private static final long serialVersionUID = -6558055971868370884L;

	public Class<? extends IEntity> entityClass() {
		return Customer.class;
	}
}
