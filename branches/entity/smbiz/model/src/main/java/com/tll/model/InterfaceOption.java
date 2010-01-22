package com.tll.model;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.tll.model.schema.BusinessKeyDef;
import com.tll.model.schema.BusinessObject;
import com.tll.model.schema.Extended;

/**
 * The interface option entity
 * @author jpk
 */
@Extended
@BusinessObject(businessKeys = @BusinessKeyDef(name = "Code", properties = { "code" }))
public class InterfaceOption extends InterfaceOptionBase {

	private static final long serialVersionUID = -3858516767622503827L;

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
	public Set<InterfaceOptionParameterDefinition> getParameters() {
		return parameters;
	}

	/**
	 * @param parameters The parameters to set.
	 */
	public void setParameters(Set<InterfaceOptionParameterDefinition> parameters) {
		this.parameters = parameters;
	}

	public InterfaceOptionParameterDefinition getParameter(IPrimaryKey pk) {
		return findEntityInCollection(parameters, pk);
	}

	public InterfaceOptionParameterDefinition getParameter(String nme) {
		return findNamedEntityInCollection(parameters, nme);
	}

	public void addParameter(InterfaceOptionParameterDefinition e) {
		addEntityToCollection(parameters, e);
	}

	public void addParameters(Collection<InterfaceOptionParameterDefinition> clc) {
		addEntitiesToCollection(clc, parameters);
	}

	public void removeParameter(InterfaceOptionParameterDefinition e) {
		removeEntityFromCollection(parameters, e);
	}

	public void removeParameters() {
		clearEntityCollection(parameters);
	}

	public int getNumParameters() {
		return getCollectionSize(parameters);
	}
}
