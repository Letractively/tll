package com.tll.model;

import javax.jdo.annotations.Discriminator;
import javax.jdo.annotations.PersistenceCapable;

/**
 * The ISP entity
 * @author jpk
 */
@PersistenceCapable
@Discriminator(value = Account.ISP_VALUE)
public class Isp extends Account {

	private static final long serialVersionUID = -1666954465162270432L;

	public Class<? extends IEntity> entityClass() {
		return Isp.class;
	}
}