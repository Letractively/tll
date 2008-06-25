package com.tll.model.impl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.NotNull;
import org.hibernate.validator.Range;

import com.tll.model.EntityBase;
import com.tll.model.IChildEntity;
import com.tll.model.IEntity;
import com.tll.model.key.BusinessKey;
import com.tll.model.key.BusinessKeyDefinition;
import com.tll.model.key.IBusinessKeyDefinition;

/**
 * Order item transaction entity
 * @author jpk
 */
@Entity
@Table(name = "order_item_trans")
public class OrderItemTrans extends EntityBase implements IChildEntity<OrderTrans>, IAccountRelatedEntity {

	private static final long serialVersionUID = -2106851598169919247L;

	private static final IBusinessKeyDefinition binderBk =
			new BusinessKeyDefinition(OrderItemTrans.class, "Order Item Trans Binder", new String[] {
				"orderItem.id",
				"orderTrans.id" });

	private OrderItem orderItem;

	private OrderTrans orderTrans;

	private OrderItemTransOp orderItemTransOp;

	private float amount = 0f;

	public Class<? extends IEntity> entityClass() {
		return OrderItemTrans.class;
	}

	/**
	 * @return Returns the orderItemTransOp.
	 */
	@Column(name = "order_item_trans_op")
	@NotNull
	public OrderItemTransOp getOrderItemTransOp() {
		return orderItemTransOp;
	}

	/**
	 * @param orderItemTransOp The orderItemTransOp to set.
	 */
	public void setOrderItemTransOp(OrderItemTransOp orderItemTransOp) {
		this.orderItemTransOp = orderItemTransOp;
	}

	/**
	 * @return Returns the amount.
	 */
	@Column(precision = 7, scale = 2)
	@Range(min = 0L, max = 99999L)
	public float getAmount() {
		return amount;
	}

	/**
	 * @param amount The amount to set.
	 */
	public void setAmount(float amount) {
		this.amount = amount;
	}

	/**
	 * @return Returns the orderItem.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "oi_id")
	@NotNull
	public OrderItem getOrderItem() {
		return orderItem;
	}

	/**
	 * @param orderItem The orderItem to set.
	 */
	public void setOrderItem(OrderItem orderItem) {
		this.orderItem = orderItem;
	}

	/**
	 * @return Returns the orderTrans.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ot_id")
	@NotNull
	public OrderTrans getOrderTrans() {
		return orderTrans;
	}

	/**
	 * @param orderTrans The orderTrans to set.
	 */
	public void setOrderTrans(OrderTrans orderTrans) {
		this.orderTrans = orderTrans;
	}

	@Transient
	public OrderTrans getParent() {
		return getOrderTrans();
	}

	public void setParent(OrderTrans e) {
		setOrderTrans(e);
	}

	public Integer accountId() {
		try {
			return getOrderItem().getOrder().getAccount().getId();
		}
		catch(NullPointerException npe) {
			LOG.warn("Unable to provide related account id due to a NULL nested entity");
			return null;
		}
	}

	public Integer orderItemId() {
		try {
			return getOrderItem().getId();
		}
		catch(NullPointerException npe) {
			return null;
		}
	}

	public Integer orderTransId() {
		try {
			return getOrderTrans().getId();
		}
		catch(NullPointerException npe) {
			return null;
		}
	}

	@Override
	@Transient
	public BusinessKey[] getBusinessKeys() {
		return new BusinessKey[] { new BusinessKey(binderBk, new Object[] {
			orderItemId(),
			orderTransId() }) };
	}
}
