package com.tll.model.impl;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;

import com.tll.model.IEntity;

/**
 * The merchant entity
 * @author jpk
 */
@Entity
@DiscriminatorValue(Account.MERCHANT_VALUE)
public class Merchant extends Account {

	private static final long serialVersionUID = 3776963948997029172L;

	public static final int MAXLEN_STORE_NAME = 128;

	protected String storeName;

	public Class<? extends IEntity> entityClass() {
		return Merchant.class;
	}

	/**
	 * @return Returns the storeName.
	 */
	@Column(name = "store_name")
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

	@Override
	protected ToStringBuilder toStringBuilder() {
		return super.toStringBuilder().append("storeName", storeName);
	}

}