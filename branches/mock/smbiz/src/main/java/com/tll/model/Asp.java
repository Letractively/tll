package com.tll.model;

import javax.jdo.annotations.Discriminator;
import javax.jdo.annotations.PersistenceCapable;

/**
 * The ASP entity
 * @author jpk
 */
@PersistenceCapable
@Discriminator(value = Account.ASP_VALUE)
// @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
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
