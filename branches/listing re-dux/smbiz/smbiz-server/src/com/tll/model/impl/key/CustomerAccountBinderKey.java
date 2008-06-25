package com.tll.model.impl.key;

import com.tll.model.impl.CustomerAccount;
import com.tll.model.key.BusinessKey;

/**
 * Business key for {@link CustomerAccount} holding the account id and the
 * address id.
 * @author jpk
 */
public final class CustomerAccountBinderKey extends BusinessKey<CustomerAccount> {

	private static final long serialVersionUID = -6492082054153232053L;
	private static final String[] FIELDS = new String[] { "customer.id", "account.id" };

	public CustomerAccountBinderKey() {
		super();
	}

	public CustomerAccountBinderKey(Integer accountId, Integer customerId) {
		this();
		setCustomerId(customerId);
		setAccountId(accountId);
	}

	public Class<CustomerAccount> getType() {
		return CustomerAccount.class;
	}

	@Override
	protected String keyDescriptor() {
		return "Binder";
	}

	public Integer getCustomerId() {
		return (Integer) getValue(0);
	}

	public void setCustomerId(Integer customerId) {
		setValue(0, customerId);
	}

	public Integer getAccountId() {
		return (Integer) getValue(1);
	}

	public void setAccountId(Integer accountId) {
		setValue(1, accountId);
	}

	@Override
	protected String[] getFields() {
		return FIELDS;
	}

	public void setEntity(CustomerAccount entity) {
		entity.getCustomer().setId(getCustomerId());
		entity.getAccount().setId(getAccountId());
	}

}