package com.tll.model.impl.key;

import com.tll.model.impl.ShipMode;
import com.tll.model.key.NameKey;

public final class ShipModeNameKey extends NameKey<ShipMode> {

	private static final long serialVersionUID = 257939853377025421L;

	private static final String[] FIELDS = new String[] { "account.id", "name" };

	public ShipModeNameKey() {
		super(ShipMode.class);
	}

	public ShipModeNameKey(Integer accountId, String name) {
		this();
		setAccountId(accountId);
		setName(name);
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
	protected String keyDescriptor() {
		return "Account Id and Name";
	}

	@Override
	protected String[] getFields() {
		return FIELDS;
	}

	@Override
	public void setEntity(ShipMode e) {
		super.setEntity(e);
		e.getAccount().setId(getAccountId());
	}

}