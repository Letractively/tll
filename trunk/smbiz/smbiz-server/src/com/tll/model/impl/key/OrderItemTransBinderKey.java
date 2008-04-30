package com.tll.model.impl.key;

import com.tll.model.impl.OrderItemTrans;
import com.tll.model.key.BusinessKey;

/**
 * Business key for {@link OrderItemTrans} holding the account id and the
 * address id.
 * @author jpk
 */
public final class OrderItemTransBinderKey extends BusinessKey<OrderItemTrans> {

	private static final long serialVersionUID = -9056960183548687947L;

	private static final String[] FIELDS = new String[] { "orderItem.id", "orderTrans.id" };

	public OrderItemTransBinderKey() {
		super();
	}

	public OrderItemTransBinderKey(Integer orderItemId, Integer orderTransId) {
		this();
		setOrderItemId(orderItemId);
		setOrderTransId(orderTransId);
	}

	public Class<OrderItemTrans> getType() {
		return OrderItemTrans.class;
	}

	@Override
	protected String keyDescriptor() {
		return "Order Item Trans Binder";
	}

	public Integer getOrderItemId() {
		return (Integer) getValue(0);
	}

	public void setOrderItemId(Integer orderItemId) {
		setValue(0, orderItemId);
	}

	public Integer getOrderTransId() {
		return (Integer) getValue(1);
	}

	public void setOrderTransId(Integer orderTransId) {
		setValue(1, orderTransId);
	}

	@Override
	protected String[] getFields() {
		return FIELDS;
	}

	public void setEntity(OrderItemTrans entity) {
		entity.getOrderItem().setId(getOrderItemId());
		entity.getOrderTrans().setId(getOrderTransId());
	}

}