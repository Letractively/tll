package com.tll.model.impl.key;

import com.tll.key.IKey;
import com.tll.model.impl.AccountAddress;
import com.tll.model.key.BusinessKey;
import com.tll.model.key.EntityKey;
import com.tll.model.key.INameKey;
import com.tll.model.key.NameKey;

public final class AccountAddressNameKey extends BusinessKey<AccountAddress> implements INameKey<AccountAddress> {

	private static final long serialVersionUID = 4730557855914169064L;

	private static final String[] FIELDS = new String[] {
		"account.id",
		"name" };

	private Integer accountId;
	private String name;

	public AccountAddressNameKey() {
		super(AccountAddress.class);
	}

	public AccountAddressNameKey(Integer accountId, String name) {
		this();
		setAccountId(accountId);
		setName(name);
	}

	@Override
	public void setEntity(AccountAddress entity) {
		super.setEntity(entity);
		entity.getAccount().setId(getAccountId());
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	protected String keyDescriptor() {
		return "Account Id and Address Name";
	}

	@Override
	protected EntityKey<AccountAddress> clone() throws CloneNotSupportedException {
		AccountAddressNameKey cln = (AccountAddressNameKey) super.clone();
		cln.accountId = accountId;
		cln.name = name;
		return cln;
	}

	@Override
	public String getFieldName() {
		return NameKey.DEFAULT_FIELDNAME;
	}

	@Override
	public void clear() {
		accountId = null;
		name = null;
	}

	@Override
	public boolean isSet() {
		return accountId != null && name != null;
	}

	@Override
	public int compareTo(IKey<AccountAddress> o) {
		// TODO impl
		return 0;
	}

	@Override
	public String[] getFieldNames() {
		return FIELDS;
	}
}