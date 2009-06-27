package com.tll.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.validation.constraints.Length;
import org.hibernate.validation.constraints.NotEmpty;

import com.tll.model.schema.BusinessKeyDef;
import com.tll.model.schema.BusinessObject;

/**
 * Defines site codes (online "coupons")
 * @author jpk
 */
@Entity
@Table(name = "site_code")
@BusinessObject(businessKeys = @BusinessKeyDef(name = "Code", properties = { "code" }))
public class SiteCode extends NamedTimeStampEntity implements IChildEntity<Account>, IAccountRelatedEntity {

	private static final long serialVersionUID = 9132511822830985704L;

	public static final int MAXLEN_CODE = 16;
	public static final int MAXLEN_NAME = 64;

	private String code; // unique

	private Date expirationDate;

	private Account account;

	public Class<? extends IEntity> entityClass() {
		return SiteCode.class;
	}

	@Column
	@NotEmpty
	@Length(max = MAXLEN_NAME)
	public String getName() {
		return name;
	}

	/**
	 * @return Returns the account.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "aid")
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
	 * @return Returns the code.
	 */
	@Column(length = 16, nullable = false, unique = true)
	public String getCode() {
		return code;
	}

	/**
	 * @param code The code to set.
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return Returns the expirationDate.
	 */
	@Column(name = "expiration_date", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getExpirationDate() {
		return expirationDate;
	}

	/**
	 * @param expirationDate The expirationDate to set.
	 */
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
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
		catch(final NullPointerException npe) {
			LOG.warn("Unable to provide related account id due to a NULL nested entity");
			return null;
		}
	}
}