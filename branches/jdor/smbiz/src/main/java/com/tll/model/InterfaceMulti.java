package com.tll.model;

import javax.jdo.annotations.Discriminator;
import javax.jdo.annotations.PersistenceCapable;

/**
 * Multi-interface entity
 * @author jpk
 */
@PersistenceCapable
@Discriminator(value = Interface.MULTI_VALUE)
public class InterfaceMulti extends Interface {

	private static final long serialVersionUID = 918801894381663849L;

	public Class<? extends IEntity> entityClass() {
		return InterfaceMulti.class;
	}
}
