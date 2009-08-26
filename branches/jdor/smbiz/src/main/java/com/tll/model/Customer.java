package com.tll.model;

import javax.jdo.annotations.Discriminator;
import javax.jdo.annotations.PersistenceCapable;

/**
 * The Customer entity
 * @author jpk
 */
@PersistenceCapable
@Discriminator(value = Account.CUSTOMER_VALUE)
public class Customer extends Account {

	private static final long serialVersionUID = -6558055971868370884L;

	public Class<? extends IEntity> entityClass() {
		return Customer.class;
	}
}
