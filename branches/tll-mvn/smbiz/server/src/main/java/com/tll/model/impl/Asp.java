package com.tll.model.impl;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.tll.model.IEntity;

/**
 * The ASP entity
 * @author jpk
 */
@Entity
@DiscriminatorValue(Account.ASP_VALUE)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Asp extends Account {

	private static final long serialVersionUID = -2609367843672689386L;

	/**
	 * The name of the one and only asp account.
	 */
	public static final String ASP_NAME = "asp";

	public Class<? extends IEntity> entityClass() {
		return Asp.class;
	}
}
