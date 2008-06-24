package com.tll.model.impl;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.Digits;
import org.hibernate.validator.Min;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Valid;

import com.tll.model.IEntity;
import com.tll.model.key.BusinessKeyDefinition;
import com.tll.model.key.IBusinessKeyDefinition;

/**
 * The interface option entity
 * @author jpk
 */
@Entity
@DiscriminatorValue("option")
public class InterfaceOption extends InterfaceOptionBase {

	private static final long serialVersionUID = -3858516767622503827L;

	public static final IBusinessKeyDefinition CodeBk =
			new BusinessKeyDefinition(InterfaceOption.class, "Code", new String[] { "code" });

	protected boolean isDefault = false;

	protected float setUpCost = 0f;

	protected float monthlyCost = 0f;

	protected float annualCost = 0f;

	protected float baseSetupPrice = 0f;

	protected float baseMonthlyPrice = 0f;

	protected float baseAnnualPrice = 0f;

	protected Set<InterfaceOptionParameterDefinition> parameters =
			new LinkedHashSet<InterfaceOptionParameterDefinition>();

	public Class<? extends IEntity> entityClass() {
		return InterfaceOption.class;
	}

	/**
	 * @return Returns the isDefault.
	 */
	@Column(name = "is_default", nullable = false)
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
	@Column(name = "set_up_cost", nullable = false, precision = 8, scale = 2)
	@Digits(integerDigits = 6, fractionalDigits = 2)
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
	@Column(name = "monthly_cost", nullable = false, precision = 8, scale = 2)
	@Digits(integerDigits = 6, fractionalDigits = 2)
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
	@Column(name = "annual_cost", nullable = false, precision = 8, scale = 2)
	@Digits(integerDigits = 6, fractionalDigits = 2)
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
	@Column(name = "base_annual_price", nullable = false, precision = 8, scale = 2)
	@Digits(integerDigits = 6, fractionalDigits = 2)
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
	@Column(name = "base_monthly_price", nullable = false, precision = 8, scale = 2)
	@Digits(integerDigits = 6, fractionalDigits = 2)
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
	@Column(name = "base_setup_price", nullable = false, precision = 8, scale = 2)
	@Digits(integerDigits = 6, fractionalDigits = 2)
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
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "option_id")
	@org.hibernate.annotations.Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@Valid
	public Set<InterfaceOptionParameterDefinition> getParameters() {
		return parameters;
	}

	/**
	 * @param parameters The parameters to set.
	 */
	public void setParameters(Set<InterfaceOptionParameterDefinition> parameters) {
		this.parameters = parameters;
	}

	@Transient
	public InterfaceOptionParameterDefinition getParameter(int id) {
		return findEntityInCollection(parameters, id);
	}

	@Transient
	public InterfaceOptionParameterDefinition getParameter(String name) {
		return findNamedEntityInCollection(parameters, name);
	}

	@Transient
	public void addParameter(InterfaceOptionParameterDefinition e) {
		addEntityToCollection(parameters, e);
	}

	@Transient
	public void addParameters(Collection<InterfaceOptionParameterDefinition> clc) {
		addEntitiesToCollection(clc, parameters);
	}

	@Transient
	public void removeParameter(InterfaceOptionParameterDefinition e) {
		removeEntityFromCollection(parameters, e);
	}

	@Transient
	public void removeParameters() {
		clearEntityCollection(parameters);
	}

	@Transient
	public int getNumParameters() {
		return getCollectionSize(parameters);
	}

	@Override
	protected ToStringBuilder toStringBuilder() {

		return super.toStringBuilder()

		.append("isDefault", isDefault).append("setUpCost", setUpCost).append("monthlyCost", monthlyCost).append(
				"annualCost", annualCost).append("baseSetupPrice", baseSetupPrice).append("baseMonthlyPrice", baseMonthlyPrice)
				.append("baseAnnualPrice", baseAnnualPrice).append("parameters.size()",
						parameters == null ? "NULL" : Integer.toString(parameters.size()));
	}

}
