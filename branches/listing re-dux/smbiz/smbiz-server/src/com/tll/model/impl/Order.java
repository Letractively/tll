package com.tll.model.impl;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Valid;

import com.tll.client.model.DatePropertyValue;
import com.tll.client.model.IPropertyValue;
import com.tll.client.model.IntPropertyValue;
import com.tll.model.IChildEntity;
import com.tll.model.IEntity;
import com.tll.model.TimeStampEntity;
import com.tll.model.key.BusinessKey;

/**
 * The order entity
 * @author jpk
 */
@Entity
@Table(name = "orders")
public class Order extends TimeStampEntity implements IChildEntity<Account>, IAccountRelatedEntity {

	private static final long serialVersionUID = -8038786314177749578L;

	public static final int MAXLEN_NOTES = 255;
	public static final int MAXLEN_SITE_CODE = 32;

	private OrderStatus status;

	private String notes;

	private String siteCode;

	private Account account;

	private Visitor visitor;

	private Customer customer;

	private Currency currency;

	private PaymentInfo paymentInfo;

	private Address billToAddress;

	private Address shipToAddress;

	private Set<OrderItem> orderItems = new LinkedHashSet<OrderItem>();

	private Set<OrderTrans> transactions = new LinkedHashSet<OrderTrans>();

	public Class<? extends IEntity> entityClass() {
		return Order.class;
	}

	/**
	 * @return Returns the account.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "aid")
	@NotNull
	public Account getAccount() {
		return account;
	}

	/**
	 * @param account The account to set.
	 */
	public void setAccount(Account account) {
		this.account = account;
	}

	/**
	 * @return Returns the billToAddress.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "billto_adr_id")
	@Valid
	public Address getBillToAddress() {
		return billToAddress;
	}

	/**
	 * @param billToAddress The billToAddress to set.
	 */
	public void setBillToAddress(Address billToAddress) {
		this.billToAddress = billToAddress;
	}

	/**
	 * @return Returns the currency.
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "crncy_id")
	@NotNull
	public Currency getCurrency() {
		return currency;
	}

	/**
	 * @param currency The currency to set.
	 */
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	/**
	 * @return Returns the customer.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cust_id")
	public Customer getCustomer() {
		return customer;
	}

	/**
	 * @param customer The customer to set.
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	/**
	 * @return Returns the notes.
	 */
	@Column
	@Length(max = MAXLEN_NOTES)
	public String getNotes() {
		return notes;
	}

	/**
	 * @param notes The notes to set.
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}

	/**
	 * @return Returns the paymentInfo.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pymntinfo_id")
	@Valid
	public PaymentInfo getPaymentInfo() {
		return paymentInfo;
	}

	/**
	 * @param paymentInfo The paymentInfo to set.
	 */
	public void setPaymentInfo(PaymentInfo paymentInfo) {
		this.paymentInfo = paymentInfo;
	}

	/**
	 * @return Returns the shipToAddress.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "shipto_adr_id")
	@Valid
	public Address getShipToAddress() {
		return shipToAddress;
	}

	/**
	 * @param shipToAddress The shipToAddress to set.
	 */
	public void setShipToAddress(Address shipToAddress) {
		this.shipToAddress = shipToAddress;
	}

	/**
	 * @return Returns the visitor.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "visitor_id")
	public Visitor getVisitor() {
		return visitor;
	}

	/**
	 * @param visitor The visitor to set.
	 */
	public void setVisitor(Visitor visitor) {
		this.visitor = visitor;
	}

	/**
	 * @return Returns the siteCode.
	 */
	@Column(name = "site_code")
	@Length(max = MAXLEN_SITE_CODE)
	public String getSiteCode() {
		return siteCode;
	}

	/**
	 * @param siteCode The siteCode to set.
	 */
	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	/**
	 * @return Returns the status.
	 */
	@Column
	@NotNull
	public OrderStatus getStatus() {
		return status;
	}

	/**
	 * @param status The status to set.
	 */
	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	/**
	 * @return Returns the transactions.
	 */
	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "order")
	// @org.hibernate.annotations.Cascade(value =
	// org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	// we don't want to delete order transactions once they've been created
	public Set<OrderTrans> getTransactions() {
		return transactions;
	}

	/**
	 * @param transactions The transactions to set.
	 */
	public void setTransactions(Set<OrderTrans> transactions) {
		this.transactions = transactions;
	}

	@Transient
	public OrderTrans getTransaction(int id) {
		return findEntityInCollection(transactions, id);
	}

	@Transient
	public void addTransaction(OrderTrans e) {
		addEntityToCollection(transactions, e);
	}

	@Transient
	public void addTransactions(Collection<OrderTrans> clc) {
		addEntitiesToCollection(clc, transactions);
	}

	@Transient
	public void removeTransaction(OrderTrans e) {
		removeEntityFromCollection(transactions, e);
	}

	@Transient
	public void clearTransactions() {
		clearEntityCollection(transactions);
	}

	@Transient
	public int getNumTransactions() {
		return getCollectionSize(transactions);
	}

	/**
	 * @return Returns the orderItems.
	 */
	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "order")
	@org.hibernate.annotations.Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@Valid
	public Set<OrderItem> getOrderItems() {
		return orderItems;
	}

	/**
	 * @param orderItems The orderItems to set.
	 */
	public void setOrderItems(Set<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	@Transient
	public OrderItem getOrderItem(int id) {
		return findEntityInCollection(orderItems, id);
	}

	@Transient
	public void addOrderItem(OrderItem e) {
		addEntityToCollection(orderItems, e);
	}

	@Transient
	public void addOrderItems(Collection<OrderItem> clc) {
		addEntitiesToCollection(clc, orderItems);
	}

	@Transient
	public void removeOrderItem(OrderItem e) {
		removeEntityFromCollection(orderItems, e);
	}

	@Transient
	public int getNumOrderItems() {
		return getCollectionSize(orderItems);
	}

	@Transient
	public void clearOrderItems() {
		clearEntityCollection(orderItems);
	}

	@Transient
	public Account getParent() {
		return getAccount();
	}

	public void setParent(Account e) {
		setAccount(e);
	}

	public Integer accountId() {
		try {
			return getAccount().getId();
		}
		catch(NullPointerException npe) {
			LOG.warn("Unable to provide related account id due to a NULL nested entity");
			return null;
		}
	}

	public Integer customerId() {
		try {
			return getCustomer().getId();
		}
		catch(NullPointerException npe) {
			return null;
		}
	}

	@Override
	@Transient
	public BusinessKey[] getBusinessKeys() {
		return new BusinessKey[] { new BusinessKey(Order.class, "Order Key", new IPropertyValue[] {
			new DatePropertyValue("dateCreated", getDateCreated()),
			new IntPropertyValue("account.id", accountId()),
			new IntPropertyValue("customer.id", customerId()) }) };
	}
}