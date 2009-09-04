package com.tll.model;

import javax.jdo.annotations.Discriminator;
import javax.jdo.annotations.PersistenceCapable;

/**
 * Switch type interface
 * @author jpk
 */
@PersistenceCapable
@Discriminator(value = Interface.SWITCH_VALUE)
public class InterfaceSwitch extends Interface {

	private static final long serialVersionUID = 1751342467693070340L;

	public Class<? extends IEntity> entityClass() {
		return InterfaceSwitch.class;
	}
}
