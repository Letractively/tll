package com.tll.model;

import com.tll.model.schema.Extended;

/**
 * Multi-interface entity
 * @author jpk
 */
@Extended
public class InterfaceMulti extends Interface {

	private static final long serialVersionUID = 918801894381663849L;

	public Class<? extends IEntity> entityClass() {
		return InterfaceMulti.class;
	}
}
