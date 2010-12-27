package com.tll.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.tll.model.bk.BusinessKeyDef;
import com.tll.model.bk.BusinessObject;

/**
 * The customer account entity
 * @author jpk
 */
@BusinessObject(businessKeys = @BusinessKeyDef(name = "Customer Id and Account Id", properties = {
	"customer.id", "account.id" }))
public class CustomerAccount extends TimeStampEntity implements IChildEntity<Account>, IAccountRelatedEntity {

	private static final long serialVersionUID = 7262902363821073379L;

	private Customer customer;

	private Account account;

	private AccountSource source;

	private AccountStatus status;

	private Visitor initialVisitorRecord;

	@Override
	public Class<? extends IEntity> entityClass() {
		return CustomerAccount.class;
	}

	/**
	 * @return Returns the customer.
	 */
	@NotNull
	@Valid
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
	 * @return Returns the source.
	 */
	@NotNull
	public AccountSource getSource() {
		return source;
	}

	/**
	 * @param source The source to set.
	 */
	public void setSource(final AccountSource source) {
		this.source = source;
	}

	/**
	 * @return Returns the status.
	 */
	@NotNull
	public AccountStatus getStatus() {
		return status;
	}

	/**
	 * @param status The status to set.
	 */
	public void setStatus(final AccountStatus status) {
		this.status = status;
	}

	/**
	 * @return Returns the initialVisitorRecord.
	 */
	public Visitor getInitialVisitorRecord() {
		return initialVisitorRecord;
	}

	/**
	 * @param initialVisitorRecord The initialVisitorRecord to set.
	 */
	public void setInitialVisitorRecord(final Visitor initialVisitorRecord) {
		this.initialVisitorRecord = initialVisitorRecord;
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
	public Object accountKey() {
		try {
			return getAccount().getId();
		}
		catch(final NullPointerException npe) {
			LOG.warn("Unable to provide related account id due to a NULL nested entity");
			return null;
		}
	}

	public Object customerId() {
		try {
			return getCustomer().getId();
		}
		catch(final NullPointerException npe) {
			return null;
		}
	}
}
