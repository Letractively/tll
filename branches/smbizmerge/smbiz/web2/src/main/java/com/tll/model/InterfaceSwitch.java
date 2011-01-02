package com.tll.model;


/**
 * Switch type interface
 * @author jpk
 */
@Extended
public class InterfaceSwitch extends Interface {

	private static final long serialVersionUID = 1751342467693070340L;

	@Override
	public Class<? extends IEntity> entityClass() {
		return InterfaceSwitch.class;
	}
}
