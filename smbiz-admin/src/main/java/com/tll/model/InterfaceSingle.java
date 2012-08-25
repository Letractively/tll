package com.tll.model;


/**
 * Single interface entity
 * @author jpk
 */
@Extended
public class InterfaceSingle extends Interface {

	private static final long serialVersionUID = -6871345929021882937L;

	@Override
	public Class<? extends IEntity> entityClass() {
		return InterfaceSingle.class;
	}
}