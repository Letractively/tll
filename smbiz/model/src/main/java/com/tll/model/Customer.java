package com.tll.model;


/**
 * The Customer entity
 * @author jpk
 */
@Extended
public class Customer extends Account {

	private static final long serialVersionUID = -6558055971868370884L;

	@Override
	public Class<? extends IEntity> entityClass() {
		return Customer.class;
	}
}
