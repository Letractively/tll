package com.tll.model.impl.key;

import com.tll.model.impl.ProductInventory;
import com.tll.model.key.BusinessKey;

public final class ProductSkuKey extends BusinessKey<ProductInventory> {

	private static final long serialVersionUID = -8464453352298806292L;

	private static final String[] FIELDS = new String[] { "account.id", "sku" };

	public ProductSkuKey() {
		super();
	}

	public ProductSkuKey(Integer accountId, String sku) {
		this();
		setAccountId(accountId);
		setSku(sku);
	}

	public Class<ProductInventory> getType() {
		return ProductInventory.class;
	}

	@Override
	protected String keyDescriptor() {
		return "Account Id and SKU";
	}

	@Override
	protected String[] getFields() {
		return FIELDS;
	}

	public void setEntity(ProductInventory e) {
		e.getAccount().setId(getAccountId());
		e.setSku(getSku());
	}

	public Integer getAccountId() {
		return (Integer) getValue(0);
	}

	public void setAccountId(Integer accountId) {
		setValue(0, accountId);
	}

	public String getSku() {
		return (String) getValue(1);
	}

	public void setSku(String sku) {
		setValue(1, sku);
	}

}