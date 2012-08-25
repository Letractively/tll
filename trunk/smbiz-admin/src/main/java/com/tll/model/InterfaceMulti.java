package com.tll.model;


/**
 * Multi-interface entity
 * @author jpk
 */
@Extended
public class InterfaceMulti extends Interface {

	private static final long serialVersionUID = 918801894381663849L;

	@Override
	public Class<? extends IEntity> entityClass() {
		return InterfaceMulti.class;
	}
}
