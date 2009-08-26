package com.tll.model;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.Unique;
import javax.jdo.annotations.Uniques;
import javax.validation.constraints.NotNull;

import org.hibernate.validation.constraints.Length;
import org.hibernate.validation.constraints.NotEmpty;

/**
 * Sales tax entity
 * @author jpk
 */
@PersistenceCapable
@Uniques(value =
	@Unique(name = "Account Id, Province, Country and Postal Code",
			members = { "account.id", "province", "county", "postalCode" }))
public class SalesTax extends TimeStampEntity implements IChildEntity<Account>, IAccountRelatedEntity {

	private static final long serialVersionUID = -8285702989304183918L;

	public static final int MAXLEN_PROVINCE = 64;
	public static final int MAXLEN_COUNTY = 64;
	public static final int MAXLEN_POSTAL_CODE = 16;

	@Persistent
	private String province;

	@Persistent
	private String county;

	@Persistent
	private String postalCode;

	@Persistent
	private float tax;

	@Persistent
	private Account account;

	public Class<? extends IEntity> entityClass() {
		return SalesTax.class;
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
	 * @return Returns the county.
	 */
	@NotEmpty
	@Length(max = MAXLEN_COUNTY)
	public String getCounty() {
		return county;
	}

	/**
	 * @param county The county to set.
	 */
	public void setCounty(String county) {
		this.county = county;
	}

	/**
	 * @return Returns the province.
	 */
	@NotEmpty
	@Length(max = MAXLEN_PROVINCE)
	public String getProvince() {
		return province;
	}

	/**
	 * @param state The state to set.
	 */
	public void setProvince(String state) {
		this.province = state;
	}

	/**
	 * @return Returns the tax.
	 */
	@NotNull
	// @Size(min = 0, max = 1)
	public float getTax() {
		return tax;
	}

	/**
	 * @param tax The tax to set.
	 */
	public void setTax(float tax) {
		this.tax = tax;
	}

	/**
	 * @return Returns the postalCode.
	 */
	@NotEmpty
	@Length(max = MAXLEN_POSTAL_CODE)
	public String getPostalCode() {
		return postalCode;
	}

	/**
	 * @param zip The zip to set.
	 */
	public void setPostalCode(String zip) {
		this.postalCode = zip;
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
}