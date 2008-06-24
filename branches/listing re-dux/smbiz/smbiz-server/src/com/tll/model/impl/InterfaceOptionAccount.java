package com.tll.model.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Range;
import org.hibernate.validator.Valid;

import com.tll.model.IChildEntity;
import com.tll.model.IEntity;
import com.tll.model.TimeStampEntity;
import com.tll.model.key.BusinessKeyDefinition;
import com.tll.model.key.IBusinessKeyDefinition;

/**
 * Binder entity between interface options and an account
 * @author jpk
 */
@Entity
@Table(name = "ioa")
public class InterfaceOptionAccount extends TimeStampEntity implements IChildEntity<Account>, IAccountRelatedEntity {

	private static final long serialVersionUID = 1185305612828685906L;

	public static final int MAXLEN_PARAM_NAME = 50;
	public static final int MAXLEN_PARAM_VALUE = 255;

	public static final IBusinessKeyDefinition BinderBk =
			new BusinessKeyDefinition(InterfaceOptionAccount.class, "Binder", new String[] {
				"option.id",
				"account.id" });

	protected InterfaceOption option;

	protected Account account;

	protected InterfaceStatus status;

	protected float setUpPrice = 0f;

	protected float monthlyPrice = 0f;

	protected float annualPrice = 0f;

	protected Map<String, String> parameters = new LinkedHashMap<String, String>();

	public Class<? extends IEntity> entityClass() {
		return InterfaceOptionAccount.class;
	}

	/**
	 * @return Returns the option.
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "option_id")
	@NotNull
	public InterfaceOption getOption() {
		return option;
	}

	/**
	 * @param option The option to set.
	 */
	public void setOption(InterfaceOption option) {
		this.option = option;
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
	 * @return Returns the status.
	 */
	@Column
	@NotNull
	public InterfaceStatus getStatus() {
		return status;
	}

	/**
	 * @param status The status to set.
	 */
	public void setStatus(InterfaceStatus status) {
		this.status = status;
	}

	/**
	 * @return Returns the setUpPrice.
	 */
	@Column(name = "set_up_price", precision = 8, scale = 2)
	@NotNull
	@Range(min = 1L, max = 999999L)
	public float getSetUpPrice() {
		return setUpPrice;
	}

	/**
	 * @param setUpPrice The setUpPrice to set.
	 */
	public void setSetUpPrice(float setUpPrice) {
		this.setUpPrice = setUpPrice;
	}

	/**
	 * @return Returns the monthlyPrice.
	 */
	@Column(name = "monthly_price", precision = 8, scale = 2)
	@NotNull
	@Range(min = 1L, max = 999999L)
	public float getMonthlyPrice() {
		return monthlyPrice;
	}

	/**
	 * @param monthlyPrice The monthlyPrice to set.
	 */
	public void setMonthlyPrice(float monthlyPrice) {
		this.monthlyPrice = monthlyPrice;
	}

	/**
	 * @return Returns the annualPrice.
	 */
	@Column(name = "annual_price", precision = 8, scale = 2)
	@NotNull
	@Range(min = 1L, max = 999999L)
	public float getAnnualPrice() {
		return annualPrice;
	}

	/**
	 * @param annualPrice The annualPrice to set.
	 */
	public void setAnnualPrice(float annualPrice) {
		this.annualPrice = annualPrice;
	}

	/**
	 * @return Returns the parameters.
	 */
	@CollectionOfElements(fetch = FetchType.EAGER)
	@JoinTable(name = "ioap", joinColumns = @JoinColumn(name = "ioaid"))
	@Column(name = "value")
	@org.hibernate.annotations.MapKey(columns = { @Column(name = "name") })
	@Valid
	public Map<String, String> getParameters() {
		return parameters;
	}

	/**
	 * @param parameters The parameters to set.
	 */
	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	@Transient
	public void setParameter(String name, String value) {
		if(name != null && value != null) {
			parameters.put(name, value);
		}
	}

	@Transient
	public void removeParameter(String name) {
		if(name != null && parameters != null) {
			parameters.remove(name);
		}
	}

	@Transient
	public void clearParameters() {
		if(parameters != null) {
			parameters.clear();
		}
	}

	@Transient
	public int getNumParameters() {
		return parameters == null ? 0 : parameters.size();
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

		.append("option", option == null ? "NULL" : option.descriptor()).append("account",
				account == null ? "NULL" : account.descriptor()).append("status", status).append("setUpPrice", setUpPrice)
				.append("monthlyPrice", monthlyPrice).append("annualPrice", annualPrice).append("parameters.size()",
						parameters == null ? "NULL" : Integer.toString(parameters.size()));
	}
}