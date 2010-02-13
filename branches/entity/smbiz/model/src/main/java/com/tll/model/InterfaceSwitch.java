package com.tll.model;

import com.tll.schema.Extended;

/**
 * Switch type interface
 * @author jpk
 */
@Extended
public class InterfaceSwitch extends Interface {

	private static final long serialVersionUID = 1751342467693070340L;

	public Class<? extends IEntity> entityClass() {
		return InterfaceSwitch.class;
	}
}
