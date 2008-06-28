package com.tll.model.impl;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.tll.model.IEntity;

/**
 * Multi-interface entity
 * @author jpk
 */
@Entity
@DiscriminatorValue(Interface.MULTI_VALUE)
public class InterfaceMulti extends Interface {

	private static final long serialVersionUID = 918801894381663849L;

	public Class<? extends IEntity> entityClass() {
		return InterfaceMulti.class;
	}
}
