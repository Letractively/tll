/**
 * The Logic Lab
 * @author jpk
 * @since Sep 6, 2009
 */
package com.tll.model;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.tll.model.schema.Transient;

/**
 * AccountOptionPanel - Pseudo-entity to facilitate ui interaction.
 * @author jpk
 */
@Transient
public class AccountInterfaceOption extends InterfaceOptionBase {

	private static final long serialVersionUID = -6577796307065099973L;

	/* InterfaceOption properties */

	private boolean isDefault = false;

	private float setUpCost = 0f;

	private float monthlyCost = 0f;

	private float annualCost = 0f;

	private float baseSetupPrice = 0f;

	private float baseMonthlyPrice = 0f;

	private float baseAnnualPrice = 0f;

	/* InterfaceOptionAccount properties */

	private float setUpPrice = 0f;

	private float monthlyPrice = 0f;

	private float annualPrice = 0f;

	/**
	 * I.e. is there a binding record between this account and interface option
	 * which signifies the account is "subscribed" to this interface option.
	 */
	private boolean subscribed;

	private Set<AccountInterfaceOptionParameter> parameters = new LinkedHashSet<AccountInterfaceOptionParameter>();

	public Class<? extends IEntity> entityClass() {
		return AccountInterfaceOption.class;
	}

	/**
	 * @return Returns the isDefault.
	 */
	@NotNull
	public boolean isDefault() {
		return isDefault;
	}

	/**
	 * @param isDefault The isDefault to set.
	 */
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	/**
	 * @return Returns the setUpCost.
	 */
	@Digits(integer = 6, fraction = 2)
	@NotNull
	@Min(value = 0)
	public float getSetUpCost() {
		return setUpCost;
	}

	/**
	 * @param setUpCost The setUpCost to set.
	 */
	public void setSetUpCost(float setUpCost) {
		this.setUpCost = setUpCost;
	}

	/**
	 * @return Returns the monthlyCost.
	 */
	@Digits(integer = 6, fraction = 2)
	@NotNull
	@Min(value = 0)
	public float getMonthlyCost() {
		return monthlyCost;
	}

	/**
	 * @param monthlyCost The monthlyCost to set.
	 */
	public void setMonthlyCost(float monthlyCost) {
		this.monthlyCost = monthlyCost;
	}

	/**
	 * @return Returns the annualCost.
	 */
	@Digits(integer = 6, fraction = 2)
	@NotNull
	@Min(value = 0)
	public float getAnnualCost() {
		return annualCost;
	}

	/**
	 * @param annualCost The annualCost to set.
	 */
	public void setAnnualCost(float annualCost) {
		this.annualCost = annualCost;
	}

	/**
	 * @return Returns the baseAnnualPrice.
	 */
	@Digits(integer = 6, fraction = 2)
	@NotNull
	@Min(value = 0)
	public float getBaseAnnualPrice() {
		return baseAnnualPrice;
	}

	/**
	 * @param baseAnnualPrice The baseAnnualPrice to set.
	 */
	public void setBaseAnnualPrice(float baseAnnualPrice) {
		this.baseAnnualPrice = baseAnnualPrice;
	}

	/**
	 * @return Returns the baseMonthlyPrice.
	 */
	@Digits(integer = 6, fraction = 2)
	@NotNull
	@Min(value = 0)
	public float getBaseMonthlyPrice() {
		return baseMonthlyPrice;
	}

	/**
	 * @param baseMonthlyPrice The baseMonthlyPrice to set.
	 */
	public void setBaseMonthlyPrice(float baseMonthlyPrice) {
		this.baseMonthlyPrice = baseMonthlyPrice;
	}

	/**
	 * @return Returns the baseSetupPrice.
	 */
	@Digits(integer = 6, fraction = 2)
	@NotNull
	@Min(value = 0)
	public float getBaseSetupPrice() {
		return baseSetupPrice;
	}

	/**
	 * @param baseSetupPrice The baseSetupPrice to set.
	 */
	public void setBaseSetupPrice(float baseSetupPrice) {
		this.baseSetupPrice = baseSetupPrice;
	}

	/**
	 * @return Returns the parameters.
	 */
	@Valid
	public Set<AccountInterfaceOptionParameter> getParameters() {
		return parameters;
	}

	/**
	 * @param parameters The parameters to set.
	 */
	public void setParameters(Set<AccountInterfaceOptionParameter> parameters) {
		this.parameters = parameters;
	}

	public AccountInterfaceOptionParameter getParameter(IPrimaryKey pk) {
		return findEntityInCollection(parameters, pk);
	}

	public AccountInterfaceOptionParameter getParameter(String nme) {
		return findNamedEntityInCollection(parameters, nme);
	}

	public void addParameter(AccountInterfaceOptionParameter e) {
		addEntityToCollection(parameters, e);
	}

	public void addParameters(Collection<AccountInterfaceOptionParameter> clc) {
		addEntitiesToCollection(clc, parameters);
	}

	public void removeParameter(AccountInterfaceOptionParameter e) {
		removeEntityFromCollection(parameters, e);
	}

	public void removeParameters() {
		clearEntityCollection(parameters);
	}

	public int getNumParameters() {
		return getCollectionSize(parameters);
	}

	public boolean isSubscribed() {
		return subscribed;
	}

	public void setSubscribed(boolean subscribed) {
		this.subscribed = subscribed;
	}

	@NotNull
	// @Size(min = 1, max = 999999)
	public float getSetUpPrice() {
		return setUpPrice;
	}

	public void setSetUpPrice(float setUpPrice) {
		this.setUpPrice = setUpPrice;
	}

	@NotNull
	// @Size(min = 1, max = 999999)
	public float getMonthlyPrice() {
		return monthlyPrice;
	}

	public void setMonthlyPrice(float monthlyPrice) {
		this.monthlyPrice = monthlyPrice;
	}

	@NotNull
	// @Size(min = 1, max = 999999)
	public float getAnnualPrice() {
		return annualPrice;
	}

	public void setAnnualPrice(float annualPrice) {
		this.annualPrice = annualPrice;
	}
}
