package com.tll.model.impl.key;

import com.tll.key.IKey;
import com.tll.model.impl.AccountAddress;
import com.tll.model.key.BusinessKey;

/**
 * Business key for {@link AccountAddress} holding the account id and the
 * address id.
 * @author jpk
 */
public final class AccountAddressBinderKey extends BusinessKey<AccountAddress> {

	private static final long serialVersionUID = -6492082054153232053L;

	private static final String[] FIELDS = new String[] {
		"account.id",
		"address.id" };

	private Integer accountId;
	private Integer addressId;

	public AccountAddressBinderKey() {
		super(AccountAddress.class);
	}

	public AccountAddressBinderKey(Integer accountId, Integer addressId) {
		this();
		setAccountId(accountId);
		setAddressId(addressId);
	}

	@Override
	protected String keyDescriptor() {
		return "Binder";
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public Integer getAddressId() {
		return addressId;
	}

	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}

	@Override
	public void setEntity(AccountAddress entity) {
		entity.getAccount().setId(getAccountId());
		entity.getAddress().setId(getAddressId());
	}

	@Override
	public void clear() {
		accountId = addressId = null;
	}

	@Override
	public boolean isSet() {
		return accountId != null && addressId != null;
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