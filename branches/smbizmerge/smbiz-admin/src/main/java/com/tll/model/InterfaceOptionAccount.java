package com.tll.model;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.tll.model.bk.BusinessKeyDef;
import com.tll.model.bk.BusinessObject;

/**
 * Binder entity between interface options and an account
 * @author jpk
 */
@BusinessObject(businessKeys = @BusinessKeyDef(name = "Option Id and Account Id", properties = {
	"option.id", "account.id" }))
public class InterfaceOptionAccount extends TimeStampEntity implements IChildEntity<Account>, IAccountRelatedEntity {

	private static final long serialVersionUID = 1185305612828685906L;

	public static final int MAXLEN_PARAM_NAME = 50;
	public static final int MAXLEN_PARAM_VALUE = 255;

	protected InterfaceOption option;

	protected Account account;

	protected float setUpPrice = 0f;

	protected float monthlyPrice = 0f;

	protected float annualPrice = 0f;

	protected Map<String, String> parameters = new LinkedHashMap<String, String>();

	@Override
	public Class<? extends IEntity> entityClass() {
		return InterfaceOptionAccount.class;
	}

	/**
	 * @return Returns the option.
	 */
	@NotNull
	public InterfaceOption getOption() {
		return option;
	}

	/**
	 * @param option The option to set.
	 */
	public void setOption(final InterfaceOption option) {
		this.option = option;
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
	 * @return Returns the setUpPrice.
	 */
	@NotNull
	// @Size(min = 1, max = 999999)
	public float getSetUpPrice() {
		return setUpPrice;
	}

	/**
	 * @param setUpPrice The setUpPrice to set.
	 */
	public void setSetUpPrice(final float setUpPrice) {
		this.setUpPrice = setUpPrice;
	}

	/**
	 * @return Returns the monthlyPrice.
	 */
	@NotNull
	// @Size(min = 1, max = 999999)
	public float getMonthlyPrice() {
		return monthlyPrice;
	}

	/**
	 * @param monthlyPrice The monthlyPrice to set.
	 */
	public void setMonthlyPrice(final float monthlyPrice) {
		this.monthlyPrice = monthlyPrice;
	}

	/**
	 * @return Returns the annualPrice.
	 */
	@NotNull
	// @Size(min = 1, max = 999999)
	public float getAnnualPrice() {
		return annualPrice;
	}

	/**
	 * @param annualPrice The annualPrice to set.
	 */
	public void setAnnualPrice(final float annualPrice) {
		this.annualPrice = annualPrice;
	}

	/**
	 * @return Returns the parameters.
	 */
	@Valid
	public Map<String, String> getParameters() {
		return parameters;
	}

	/**
	 * @param parameters The parameters to set.
	 */
	public void setParameters(final Map<String, String> parameters) {
		this.parameters = parameters;
	}

	public void setParameter(final String name, final String value) {
		if(name != null && value != null) {
			parameters.put(name, value);
		}
	}

	public void removeParameter(final String name) {
		if(name != null && parameters != null) {
			parameters.remove(name);
		}
	}

	public void clearParameters() {
		if(parameters != null) {
			parameters.clear();
		}
	}

	public int getNumParameters() {
		return parameters == null ? 0 : parameters.size();
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

	public Object optionKey() {
		try {
			return getOption().getId();
		}
		catch(final NullPointerException npe) {
			return null;
		}
	}
}