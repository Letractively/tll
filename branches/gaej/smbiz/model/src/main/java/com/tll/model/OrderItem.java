package com.tll.model;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.tll.model.schema.BusinessKeyDef;
import com.tll.model.schema.BusinessObject;

/**
 * Order item entity
 * @author jpk
 */
@BusinessObject(businessKeys =
	@BusinessKeyDef(name = "Order Id and SKU", properties = { "order.id", "sku" })
)
public class OrderItem extends NamedTimeStampEntity implements IChildEntity<Order>, IAccountRelatedEntity {

	private static final long serialVersionUID = 5728694308136658158L;

	public static final int MAXLEN_SKU = 64;
	public static final int MAXLEN_NAME = 128;
	public static final int MAXLEN_DESCRIPTION = 255;
	public static final int MAXLEN_IMAGE = 32;

	private Order order;

	private String sku;

	private OrderItemStatus itemStatus;

	private PaymentItemStatus payStatus;

	private int qty = 0;

	private float price = 0f;

	private float weight = 0f;

	private String description;

	private String image;

	private Set<OrderItemTrans> transactions = new LinkedHashSet<OrderItemTrans>();

	public Class<? extends IEntity> entityClass() {
		return OrderItem.class;
	}

	@NotEmpty
	@Length(max = MAXLEN_NAME)
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @return Returns the description.
	 */
	@NotEmpty
	@Length(max = MAXLEN_DESCRIPTION)
	public String getDescription() {
		return description;
	}

	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return Returns the image.
	 */
	@Length(max = MAXLEN_IMAGE)
	public String getImage() {
		return image;
	}

	/**
	 * @param image The image to set.
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * @return Returns the itemStatus.
	 */
	@NotNull
	public OrderItemStatus getItemStatus() {
		return itemStatus;
	}

	/**
	 * @param itemStatus The itemStatus to set.
	 */
	public void setItemStatus(OrderItemStatus itemStatus) {
		this.itemStatus = itemStatus;
	}

	/**
	 * @return Returns the order.
	 */
	@NotNull
	public Order getOrder() {
		return order;
	}

	/**
	 * @param order The order to set.
	 */
	public void setOrder(Order order) {
		this.order = order;
	}

	/**
	 * @return Returns the payStatus.
	 */
	@NotNull
	public PaymentItemStatus getPayStatus() {
		return payStatus;
	}

	/**
	 * @param payStatus The payStatus to set.
	 */
	public void setPayStatus(PaymentItemStatus payStatus) {
		this.payStatus = payStatus;
	}

	/**
	 * @return Returns the price.
	 */
	// @Size(min = 0, max = 99999)
	public float getPrice() {
		return price;
	}

	/**
	 * @param price The price to set.
	 */
	public void setPrice(float price) {
		this.price = price;
	}

	/**
	 * @return Returns the qty.
	 */
	// @Size(min = 0, max = 999999)
	public int getQty() {
		return qty;
	}

	/**
	 * @param qty The qty to set.
	 */
	public void setQty(int qty) {
		this.qty = qty;
	}

	/**
	 * @return Returns the sku.
	 */
	@NotEmpty
	@Length(max = MAXLEN_SKU)
	public String getSku() {
		return sku;
	}

	/**
	 * @param sku The sku to set.
	 */
	public void setSku(String sku) {
		this.sku = sku;
	}

	/**
	 * @return Returns the weight.
	 */
	// @Size(min = 0, max = 999999)
	public float getWeight() {
		return weight;
	}

	/**
	 * @param weight The weight to set.
	 */
	public void setWeight(float weight) {
		this.weight = weight;
	}

	/**
	 * @return Returns the transactions.
	 */
	public Set<OrderItemTrans> getTransactions() {
		return transactions;
	}

	/**
	 * @param transactions The transactions to set.
	 */
	public void setTransactions(Set<OrderItemTrans> transactions) {
		this.transactions = transactions;
	}

	public OrderItemTrans getItemTransaction(int id) {
		return findEntityInCollection(transactions, id);
	}

	public void addItemTransaction(OrderItemTrans e) {
		addEntityToCollection(transactions, e);
	}

	public void addItemTransactions(Collection<OrderItemTrans> clc) {
		addEntitiesToCollection(clc, transactions);
	}

	public void removeTransaction(OrderItemTrans e) {
		removeEntityFromCollection(transactions, e);
	}

	public void clearTransactions() {
		clearEntityCollection(transactions);
	}

	public int getNumTransactions() {
		return getCollectionSize(transactions);
	}

	public Order getParent() {
		return getOrder();
	}

	public void setParent(Order e) {
		setOrder(e);
	}

	public Long accountId() {
		try {
			return getOrder().getAccount().getId();
		}
		catch(final NullPointerException npe) {
			LOG.warn("Unable to provide related account id due to a NULL nested entity");
			return null;
		}
	}

	public Long orderId() {
		try {
			return getOrder().getId();
		}
		catch(final NullPointerException npe) {
			return null;
		}
	}
}