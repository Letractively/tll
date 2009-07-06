package com.tll.model;

import javax.jdo.annotations.Discriminator;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import org.hibernate.validation.constraints.Length;
import org.hibernate.validation.constraints.NotEmpty;

/**
 * The merchant entity
 * @author jpk
 */
@PersistenceCapable
@Discriminator(value = Account.MERCHANT_VALUE)
public class Merchant extends Account {

	private static final long serialVersionUID = 3776963948997029172L;

	public static final int MAXLEN_STORE_NAME = 128;

	@Persistent
	protected String storeName;

	public Class<? extends IEntity> entityClass() {
		return Merchant.class;
	}

	/**
	 * @return Returns the storeName.
	 */
	@NotEmpty
	@Length(max = MAXLEN_STORE_NAME)
	public String getStoreName() {
		return storeName;
	}

	/**
	 * @param storeName The storeName to set.
	 */
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
}