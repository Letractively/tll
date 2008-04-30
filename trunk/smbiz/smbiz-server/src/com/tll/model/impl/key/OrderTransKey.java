package com.tll.model.impl.key;

import java.util.Date;

import com.tll.model.impl.OrderTrans;
import com.tll.model.key.BusinessKey;

/**
 * OrderTransKey
 * @author jpk
 */
public final class OrderTransKey extends BusinessKey<OrderTrans> {

	private static final long serialVersionUID = -1146047709569617376L;
	private static final String[] FIELDS = new String[] { "order.id", "dateCreated", "username" };

	public OrderTransKey() {
		super();
	}

	public OrderTransKey(Integer orderId, Date dateCreated, String username) {
		this();
		setOrderId(orderId);
		setDateCreated(dateCreated);
		setUsername(username);
	}

	public Class<OrderTrans> getType() {
		return OrderTrans.class;
	}

	@Override
	protected String keyDescriptor() {
		return "Order Id, Date Created and Username";
	}

	public Integer getOrderId() {
		return (Integer) getValue(0);
	}

	public void setOrderId(Integer orderId) {
		setValue(0, orderId);
	}

	public Date getDateCreated() {
		return (Date) getValue(1);
	}

	public void setDateCreated(Date dateCreated) {
		setValue(1, dateCreated);
	}

	public String getUsername() {
		return (String) getValue(2);
	}

	public void setUsername(String username) {
		setValue(2, username);
	}

	@Override
	protected String[] getFields() {
		return FIELDS;
	}

	public void setEntity(OrderTrans e) {
		e.getOrder().setId(getOrderId());
		e.setDateCreated(getDateCreated());
		e.setUsername(getUsername());
	}

}