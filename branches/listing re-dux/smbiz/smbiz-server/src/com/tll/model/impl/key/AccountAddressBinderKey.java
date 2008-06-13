package com.tll.model.impl.key;

import com.tll.model.impl.AccountAddress;
import com.tll.model.key.BusinessKey;

/**
 * Business key for {@link AccountAddress} holding the account id and the
 * address id.
 * @author jpk
 */
public final class AccountAddressBinderKey extends BusinessKey<AccountAddress> {

	private static final long serialVersionUID = -6492082054153232053L;
	private static final String[] FIELDS = new String[] { "account.id", "address.id" };

	public AccountAddressBinderKey() {
		super();
	}

	public AccountAddressBinderKey(Integer accountId, Integer addressId) {
		this();
		setAccountId(accountId);
		setAddressId(addressId);
	}

	public Class<AccountAddress> getType() {
		return AccountAddress.class;
	}

	@Override
	protected String keyDescriptor() {
		return "Binder";
	}

	public Integer getAccountId() {
		return (Integer) getValue(0);
	}

	public void setAccountId(Integer accountId) {
		setValue(0, accountId);
	}

	public Integer getAddressId() {
		return (Integer) getValue(1);
	}

	public void setAddressId(Integer addressId) {
		setValue(1, addressId);
	}

	@Override
	protected String[] getFields() {
		return FIELDS;
	}

	public void setEntity(AccountAddress entity) {
		entity.getAccount().setId(getAccountId());
		entity.getAddress().setId(getAddressId());
	}

}