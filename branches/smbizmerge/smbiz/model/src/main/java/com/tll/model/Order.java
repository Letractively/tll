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

	@Override
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
	public void setAccount(final Account account) {
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
	public void setBillToAddress(final Address billToAddress) {
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
	public void setCurrency(final Currency currency) {
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
	public void setCustomer(final Customer customer) {
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
	public void setNotes(final String notes) {
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
	public void setPaymentInfo(final PaymentInfo paymentInfo) {
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
	public void setShipToAddress(final Address shipToAddress) {
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
	public void setVisitor(final Visitor visitor) {
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
	public void setSiteCode(final String siteCode) {
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
	public void setStatus(final OrderStatus status) {
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
	public void setTransactions(final Set<OrderTrans> transactions) {
		this.transactions = transactions;
	}

	public OrderTrans getTransaction(final Object pk) {
		return findEntityInCollection(transactions, pk);
	}

	public void addTransaction(final OrderTrans e) {
		addEntityToCollection(transactions, e);
	}

	public void addTransactions(final Collection<OrderTrans> clc) {
		addEntitiesToCollection(clc, transactions);
	}

	public void removeTransaction(final OrderTrans e) {
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
	public void setOrderItems(final Set<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public OrderItem getOrderItem(final Object pk) {
		return findEntityInCollection(orderItems, pk);
	}

	public void addOrderItem(final OrderItem e) {
		addEntityToCollection(orderItems, e);
	}

	public void addOrderItems(final Collection<OrderItem> clc) {
		addEntitiesToCollection(clc, orderItems);
	}

	public void removeOrderItem(final OrderItem e) {
		removeEntityFromCollection(orderItems, e);
	}

	public int getNumOrderItems() {
		return getCollectionSize(orderItems);
	}

	public void clearOrderItems() {
		clearEntityCollection(orderItems);
	}

	@Override
	public Account getParent() {
		return getAccount();
	}

	@Override
	public void setParent(final Account e) {
		setAccount(e);
	}

	@Override
	public Long accountKey() {
		try {
			return getAccount().getId();
		}
		catch(final NullPointerException npe) {
			LOG.warn("Unable to provide related account id due to a NULL nested entity");
			return null;
		}
	}

	public Long customerId() {
		try {
			return getCustomer().getId();
		}
		catch(final NullPointerException npe) {
			return null;
		}
	}
}