package com.tll.model.impl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Range;

import com.tll.model.IChildEntity;
import com.tll.model.IEntity;
import com.tll.model.TimeStampEntity;
import com.tll.model.key.BusinessKeyDefinition;
import com.tll.model.key.IBusinessKeyDefinition;

/**
 * Sales tax entity
 * @author jpk
 */
@Entity
@Table(name = "sales_tax")
public class SalesTax extends TimeStampEntity implements IChildEntity<Account>, IAccountRelatedEntity {

	private static final long serialVersionUID = -8285702989304183918L;

	public static final int MAXLEN_PROVINCE = 64;
	public static final int MAXLEN_COUNTY = 64;
	public static final int MAXLEN_POSTAL_CODE = 16;

	public static final IBusinessKeyDefinition nameBk =
			new BusinessKeyDefinition(SalesTax.class, "Province, County and Postal Code", new String[] {
				"account.id",
				"province",
				"county",
				"postalCode" });

	protected String province;

	protected String county;

	protected String postalCode;

	protected float tax;

	protected Account account;

	public Class<? extends IEntity> entityClass() {
		return SalesTax.class;
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
	 * @return Returns the county.
	 */
	@Column
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
	@Column
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
	@Column(precision = 7, scale = 3)
	@NotNull
	@Range(min = 0L, max = 1L)
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
	@Column(name = "postal_code")
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

	@Override
	protected ToStringBuilder toStringBuilder() {
		return super.toStringBuilder()

		.append("province", province).append("county", county).append("postalCode", postalCode).append("tax", tax).append(
				"account", account == null ? "NULL" : account.descriptor());
	}

}