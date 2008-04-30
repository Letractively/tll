package com.tll.model.impl;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.tll.model.IEntity;

/**
 * The Customer entity
 * @author jpk
 */
@Entity
@DiscriminatorValue(Account.CUSTOMER_VALUE)
public class Customer extends Account {

	private static final long serialVersionUID = -6558055971868370884L;

	public Class<? extends IEntity> entityClass() {
		return Customer.class;
	}
}
