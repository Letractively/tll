package com.tll.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.tll.model.schema.BusinessKeyDef;
import com.tll.model.schema.BusinessObject;

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
	public void setCustomer(Customer customer) {
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
	public void setAccount(Account account) {
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
	public void setSource(AccountSource source) {
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
	public void setStatus(AccountStatus status) {
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
	public void setInitialVisitorRecord(Visitor initialVisitorRecord) {
		this.initialVisitorRecord = initialVisitorRecord;
	}

	public Account getParent() {
		return getAccount();
	}

	public void setParent(Account e) {
		setAccount(e);
	}

	public String accountId() {
		try {
			return getAccount().getId();
		}
		catch(final NullPointerException npe) {
			LOG.warn("Unable to provide related account id due to a NULL nested entity");
			return null;
		}
	}

	public String customerId() {
		try {
			return getCustomer().getId();
		}
		catch(final NullPointerException npe) {
			return null;
		}
	}
}
