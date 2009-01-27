package com.tll.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.tll.model.IEntity;

/**
 * Switch type interface
 * @author jpk
 */
@Entity
@DiscriminatorValue(Interface.SWITCH_VALUE)
public class InterfaceSwitch extends Interface {

	private static final long serialVersionUID = 1751342467693070340L;

	public Class<? extends IEntity> entityClass() {
		return InterfaceSwitch.class;
	}
}
