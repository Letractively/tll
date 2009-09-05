package com.tll.model;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.hibernate.validation.constraints.Length;
import org.hibernate.validation.constraints.NotEmpty;

import com.tll.model.schema.BusinessKeyDef;
import com.tll.model.schema.BusinessObject;

/**
 * Order trans entity
 * @author jpk
 */
@BusinessObject(businessKeys =
	@BusinessKeyDef(name = "Order Id, Date Created and Username", properties = { "order.id", "dateCreated", "username" })
)
public class OrderTrans extends TimeStampEntity implements IChildEntity<Order>, IAccountRelatedEntity {

	private static final long serialVersionUID = 8026809773722347843L;

	public static final int MAXLEN_USERNAME = 32;
	public static final int MAXLEN_SHIP_MODE_NAME = 64;
	public static final int MAXLEN_SHIP_ROUTING_NUM = 64;

	private Order order;

	private String username; // author of this transaction

	private OrderTransOp orderTransOp;

	private OrderTransOpResult orderTransResult;

	private String shipModeName;

	private String shipRoutingNum;

	private float itemTotal = 0f;

	private float salesTax = 0f;

	private float shipCost = 0f;

	private float total = 0f;

	private Address billToAddress;

	private Address shipToAddress;

	private PaymentInfo pymntInfo;

	private PaymentTrans pymntTrans;

	private Set<OrderItemTrans> itemTransactions = new LinkedHashSet<OrderItemTrans>();

	public Class<? extends IEntity> entityClass() {
		return OrderTrans.class;
	}

	public Address getBillToAddress() {
		return billToAddress;
	}

	public void setBillToAddress(Address billToAddress) {
		this.billToAddress = billToAddress;
	}

	// @Size(min = 0, max = 99999)
	public float getItemTotal() {
		return itemTotal;
	}

	public void setItemTotal(float itemTotal) {
		this.itemTotal = itemTotal;
	}

	public Set<OrderItemTrans> getItemTransactions() {
		return itemTransactions;
	}

	public void setItemTransactions(Set<OrderItemTrans> itemTransactions) {
		this.itemTransactions = itemTransactions;
	}

	@NotNull
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	@NotNull
	public OrderTransOp getOrderTransOp() {
		return orderTransOp;
	}

	public void setOrderTransOp(OrderTransOp orderTransOp) {
		this.orderTransOp = orderTransOp;
	}

	@NotNull
	public OrderTransOpResult getOrderTransResult() {
		return orderTransResult;
	}

	public void setOrderTransResult(OrderTransOpResult orderTransResult) {
		this.orderTransResult = orderTransResult;
	}

	public PaymentInfo getPymntInfo() {
		return pymntInfo;
	}

	public void setPymntInfo(PaymentInfo pymntInfo) {
		this.pymntInfo = pymntInfo;
	}

	public PaymentTrans getPymntTrans() {
		return pymntTrans;
	}

	public void setPymntTrans(PaymentTrans pymntTrans) {
		this.pymntTrans = pymntTrans;
	}

	// @Size(min = 0, max = 999999)
	public float getSalesTax() {
		return salesTax;
	}

	public void setSalesTax(float salesTax) {
		this.salesTax = salesTax;
	}

	// @Column(name = "ship_cost", precision = 7, scale = 2)
	// @Size(min = 0, max = 999999)
	public float getShipCost() {
		return shipCost;
	}

	public void setShipCost(float shipCost) {
		this.shipCost = shipCost;
	}

	@Length(max = MAXLEN_SHIP_MODE_NAME)
	public String getShipModeName() {
		return shipModeName;
	}

	public void setShipModeName(String shipModeName) {
		this.shipModeName = shipModeName;
	}

	@Length(max = MAXLEN_SHIP_ROUTING_NUM)
	public String getShipRoutingNum() {
		return shipRoutingNum;
	}

	public void setShipRoutingNum(String shipRoutingNum) {
		this.shipRoutingNum = shipRoutingNum;
	}

	public Address getShipToAddress() {
		return shipToAddress;
	}

	public void setShipToAddress(Address shipToAddress) {
		this.shipToAddress = shipToAddress;
	}

	// @Size(min = 0, max = 9999999)
	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	@NotEmpty
	@Length(max = MAXLEN_USERNAME)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public OrderItemTrans getOrderItemTrans(int id) {
		return findEntityInCollection(itemTransactions, id);
	}

	public void addOrderItemTrans(OrderItemTrans e) {
		addEntityToCollection(itemTransactions, e);
	}

	public void removeOrderItemTrans(OrderItemTrans e) {
		removeEntityFromCollection(itemTransactions, e);
	}

	public void clearOrderItemTransactions() {
		clearEntityCollection(itemTransactions);
	}

	public int getNumItemTransactions() {
		return getCollectionSize(itemTransactions);
	}

	public Order getParent() {
		return getOrder();
	}

	public void setParent(Order e) {
		setOrder(e);
	}

	public String accountId() {
		try {
			return getOrder().getAccount().getId();
		}
		catch(final NullPointerException npe) {
			LOG.warn("Unable to provide related account id due to a NULL nested entity");
			return null;
		}
	}

	public String orderId() {
		try {
			return getOrder().getId();
		}
		catch(final NullPointerException npe) {
			return null;
		}
	}
}
