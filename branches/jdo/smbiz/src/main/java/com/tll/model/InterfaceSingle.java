package com.tll.model;

import javax.jdo.annotations.Discriminator;
import javax.jdo.annotations.PersistenceCapable;

/**
 * Single interface entity
 * @author jpk
 */
@PersistenceCapable
@Discriminator(value = Interface.SINGLE_VALUE)
public class InterfaceSingle extends Interface {

	private static final long serialVersionUID = -6871345929021882937L;

	public Class<? extends IEntity> entityClass() {
		return InterfaceSingle.class;
	}
}