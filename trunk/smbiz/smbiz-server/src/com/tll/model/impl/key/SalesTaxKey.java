package com.tll.model.impl.key;

import com.tll.model.impl.SalesTax;
import com.tll.model.key.BusinessKey;

public final class SalesTaxKey extends BusinessKey<SalesTax> {

	private static final long serialVersionUID = 5148470233652001947L;

	private static final String[] FIELDS = new String[] { "account.id", "province", "county", "postalCode" };

	public SalesTaxKey() {
		super();
	}

	public SalesTaxKey(Integer accountId, String province, String county, String postalCode) {
		this();
		setAccountId(accountId);
		setProvince(province);
		setCounty(county);
		setPostalCode(postalCode);
	}

	public Class<SalesTax> getType() {
		return SalesTax.class;
	}

	@Override
	protected String keyDescriptor() {
		return "Province, County and Postal Code";
	}

	public Integer getAccountId() {
		return (Integer) getValue(0);
	}

	public void setAccountId(Integer accountId) {
		setValue(0, accountId);
	}

	public String getProvince() {
		return (String) getValue(1);
	}

	public void setProvince(String province) {
		setValue(1, province);
	}

	public String getCounty() {
		return (String) getValue(2);
	}

	public void setCounty(String county) {
		setValue(2, county);
	}

	public String getPostalCode() {
		return (String) getValue(3);
	}

	public void setPostalCode(String postalCode) {
		setValue(3, postalCode);
	}

	@Override
	protected String[] getFields() {
		return FIELDS;
	}

	public void setEntity(SalesTax e) {
		e.getAccount().setId(getAccountId());
		e.setProvince(getProvince());
		e.setCounty(getCounty());
		e.setPostalCode(getPostalCode());
	}

}