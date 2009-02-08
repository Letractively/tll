package com.tll.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.tll.model.IEntity;

/**
 * Single interface entity
 * @author jpk
 */
@Entity
@DiscriminatorValue(Interface.SINGLE_VALUE)
public class InterfaceSingle extends Interface {

	private static final long serialVersionUID = -6871345929021882937L;

	public Class<? extends IEntity> entityClass() {
		return InterfaceSingle.class;
	}
}