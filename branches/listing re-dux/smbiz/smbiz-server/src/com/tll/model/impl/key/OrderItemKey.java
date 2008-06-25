package com.tll.model.impl.key;

import com.tll.model.impl.OrderItem;
import com.tll.model.key.BusinessKey;

public final class OrderItemKey extends BusinessKey<OrderItem> {

	private static final long serialVersionUID = -3043680663589223503L;

	private static final String[] FIELDS = new String[] { "order.id", "sku" };

	public OrderItemKey() {
		super();
	}

	public OrderItemKey(Integer orderId, String sku) {
		this();
		setOrderId(orderId);
		setSku(sku);
	}

	public Class<OrderItem> getType() {
		return OrderItem.class;
	}

	@Override
	protected String keyDescriptor() {
		return "Order Id and Product SKU";
	}

	@Override
	protected String[] getFields() {
		return FIELDS;
	}

	public void setEntity(OrderItem e) {
		e.getOrder().setId(getOrderId());
		e.setSku(getSku());
	}

	public Integer getOrderId() {
		return (Integer) getValue(0);
	}

	public void setOrderId(Integer orderId) {
		setValue(0, orderId);
	}

	public String getSku() {
		return (String) getValue(1);
	}

	public void setSku(String sku) {
		setValue(1, sku);
	}

}