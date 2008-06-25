package com.tll.model.impl.key;

import com.tll.model.impl.ProductCategory;
import com.tll.model.key.NameKey;

public final class ProductCategoryNameKey extends NameKey<ProductCategory> {

	private static final long serialVersionUID = 6997889871801708540L;

	private static final String[] FIELDS = new String[] { "account.id", "name" };

	/**
	 * Constructor
	 */
	public ProductCategoryNameKey() {
		super(ProductCategory.class);
	}

	/**
	 * Constructor
	 * @param name
	 */
	public ProductCategoryNameKey(String name) {
		this();
		setName(name);
	}

	@Override
	protected String keyDescriptor() {
		return "Account Id and Name";
	}

	public Integer getAccountId() {
		return (Integer) getValue(0);
	}

	public void setAccountId(Integer accountId) {
		setValue(0, accountId);
	}

	@Override
	public String getName() {
		return (String) getValue(1);
	}

	@Override
	public void setName(String name) {
		setValue(1, name);
	}

	@Override
	protected String[] getFields() {
		return FIELDS;
	}

	@Override
	public void setEntity(ProductCategory e) {
		super.setEntity(e);
		e.getAccount().setId(getAccountId());
	}

}