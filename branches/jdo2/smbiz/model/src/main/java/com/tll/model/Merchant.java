package com.tll.model;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.tll.model.schema.Extended;

/**
 * The merchant entity
 * @author jpk
 */
@Extended
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