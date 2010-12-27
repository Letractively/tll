package com.tll.model;

import java.util.Date;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.tll.model.bk.BusinessKeyDef;

/**
 * Defines site codes (online "coupons")
 * @author jpk
 */
@BusinessObject(businessKeys = @BusinessKeyDef(name = "Code", properties = { "code" }))
public class SiteCode extends NamedTimeStampEntity implements IChildEntity<Account>, IAccountRelatedEntity {

	private static final long serialVersionUID = 9132511822830985704L;

	public static final int MAXLEN_CODE = 16;
	public static final int MAXLEN_NAME = 64;

	private String code; // unique

	private Date expirationDate;

	private Account account;

	@Override
	public Class<? extends IEntity> entityClass() {
		return SiteCode.class;
	}

	@NotEmpty
	@Length(max = MAXLEN_NAME)
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @return Returns the account.
	 */
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
	 * @return Returns the code.
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code The code to set.
	 */
	public void setCode(final String code) {
		this.code = code;
	}

	/**
	 * @return Returns the expirationDate.
	 */
	public Date getExpirationDate() {
		return expirationDate;
	}

	/**
	 * @param expirationDate The expirationDate to set.
	 */
	public void setExpirationDate(final Date expirationDate) {
		this.expirationDate = expirationDate;
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
}