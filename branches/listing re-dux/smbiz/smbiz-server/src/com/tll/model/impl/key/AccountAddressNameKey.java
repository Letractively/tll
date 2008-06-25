package com.tll.model.impl.key;

import com.tll.model.impl.AccountAddress;
import com.tll.model.key.NameKey;

public final class AccountAddressNameKey extends NameKey<AccountAddress> {

	private static final long serialVersionUID = 4730557855914169064L;

	private static final String[] FIELDS = new String[] {
		"account.id", "name" };

	public AccountAddressNameKey() {
		super(AccountAddress.class);
	}

	public AccountAddressNameKey(Integer accountId, String name) {
		this();
		setAccountId(accountId);
		setName(name);
	}

	@Override
	protected String[] getFields() {
		return FIELDS;
	}

	@Override
	public void setEntity(AccountAddress entity) {
		super.setEntity(entity);
		entity.getAccount().setId(getAccountId());
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
		return "Account Id and Address Name";
	}

}