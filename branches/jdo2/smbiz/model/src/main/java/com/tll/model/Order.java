package com.tll.model;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

/**
 * The order entity
 * @author jpk
 */
// We can't guarantee this with enough certainity! So we won't have any bks for
// orders then.
/*
@BusinessObject(value =
	@BusinessKeyDef(name = "Date Created, Account Id and Customer Id",
			members = { "dateCreated", "account.id", "customer.id" }))
 */
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
	public Set<OrderTrans> getTransactions() {
		return transactions;
	}

	/**
	 * @param transactions The transactions to set.
	 */
	public void setTransactions(Set<OrderTrans> transactions) {
		this.transactions = transactions;
	}

	public OrderTrans getTransaction(int id) {
		return findEntityInCollection(transactions, id);
	}

	public void addTransaction(OrderTrans e) {
		addEntityToCollection(transactions, e);
	}

	public void addTransactions(Collection<OrderTrans> clc) {
		addEntitiesToCollection(clc, transactions);
	}

	public void removeTransaction(OrderTrans e) {
		removeEntityFromCollection(transactions, e);
	}

	public void clearTransactions() {
		clearEntityCollection(transactions);
	}

	public int getNumTransactions() {
		return getCollectionSize(transactions);
	}

	/**
	 * @return Returns the orderItems.
	 */
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

	public OrderItem getOrderItem(int id) {
		return findEntityInCollection(orderItems, id);
	}

	public void addOrderItem(OrderItem e) {
		addEntityToCollection(orderItems, e);
	}

	public void addOrderItems(Collection<OrderItem> clc) {
		addEntitiesToCollection(clc, orderItems);
	}

	public void removeOrderItem(OrderItem e) {
		removeEntityFromCollection(orderItems, e);
	}

	public int getNumOrderItems() {
		return getCollectionSize(orderItems);
	}

	public void clearOrderItems() {
		clearEntityCollection(orderItems);
	}

	public Account getParent() {
		return getAccount();
	}

	public void setParent(Account e) {
		setAccount(e);
	}

	public long accountId() {
		try {
			return getAccount().getId();
		}
		catch(final NullPointerException npe) {
			LOG.warn("Unable to provide related account id due to a NULL nested entity");
			return -1;
		}
	}

	public long customerId() {
		try {
			return getCustomer().getId();
		}
		catch(final NullPointerException npe) {
			return -1;
		}
	}
}