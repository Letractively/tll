package com.tll.model.impl.key;

import java.util.Date;

import com.tll.model.impl.Order;
import com.tll.model.key.BusinessKey;

/**
 * @author jpk
 */
public final class OrderKey extends BusinessKey<Order> {

	private static final long serialVersionUID = -4095422175865619639L;

	private static final String[] FIELDS = new String[] { "dateCreated", "account.id", "customer.id" };

	public OrderKey() {
		super();
	}

	public OrderKey(Date dateCreated, Integer accountId, Integer customerId) {
		this();
		setDateCreated(dateCreated);
		setAccountId(accountId);
		setCustomerId(customerId);
	}

	public Class<Order> getType() {
		return Order.class;
	}

	@Override
	protected String keyDescriptor() {
		return "Date Created, Account Id and Customer Id";
	}

	public Date getDateCreated() {
		return (Date) getValue(0);
	}

	public void setDateCreated(Date dateCreated) {
		setValue(0, dateCreated);
	}

	public Integer getAccountId() {
		return (Integer) getValue(1);
	}

	public void setAccountId(Integer accountId) {
		setValue(1, accountId);
	}

	public Integer getCustomerId() {
		return (Integer) getValue(2);
	}

	public void setCustomerId(Integer customerId) {
		setValue(2, customerId);
	}

	@Override
	protected String[] getFields() {
		return FIELDS;
	}

	public void setEntity(Order e) {
		e.setDateCreated(getDateCreated());
		e.getAccount().setId(getAccountId());
		e.getCustomer().setId(getCustomerId());
	}

}