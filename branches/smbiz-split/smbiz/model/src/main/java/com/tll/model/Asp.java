package com.tll.model;

import com.tll.model.schema.Extended;

/**
 * The ASP entity
 * @author jpk
 */
@Extended
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
