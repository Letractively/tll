package com.tll.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.tll.model.IEntity;

/**
 * The ISP entity
 * @author jpk
 */
@Entity
@DiscriminatorValue(Account.ISP_VALUE)
public class Isp extends Account {

	private static final long serialVersionUID = -1666954465162270432L;

	public Class<? extends IEntity> entityClass() {
		return Isp.class;
	}
}