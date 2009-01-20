package com.tll.model.impl;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Valid;

import com.tll.model.NamedTimeStampEntity;
import com.tll.model.schema.BusinessKeyDef;
import com.tll.model.schema.BusinessObject;
import com.tll.model.validate.BusinessKeyUniqueness;

/**
 * The Interface entity
 * @author jpk
 */
@Entity
@Table(name = "interface")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.INTEGER)
@BusinessObject(businessKeys = @BusinessKeyDef(name = "Code", properties = { "code" }))
public abstract class Interface extends NamedTimeStampEntity {

	private static final long serialVersionUID = 5959712644331302508L;

	static final String SWITCH_VALUE = "0";
	static final String SINGLE_VALUE = "1";
	static final String MULTI_VALUE = "2";

	public static final int MAXLEN_CODE = 50;
	public static final int MAXLEN_NAME = 50;
	public static final int MAXLEN_DESCRIPTION = 128;

	// stock (out of the box) interface codes
	// NOTE: admins may still add new interfaces.
	public static final String CODE_PAYMENT_PROCESSOR = "pymntproc";
	public static final String CODE_SHIP_METHOD = "shipmethod";
	public static final String CODE_SALES_TAX = "salestax";
	public static final String CODE_PAYMENT_METHOD = "pymntmethod";
	public static final String CODE_CROSS_SELL = "crosssell";

	protected String code;
	protected String description;

	protected boolean isAvailableAsp = false;
	protected boolean isAvailableIsp = false;
	protected boolean isAvailableMerchant = false;
	protected boolean isAvailableCustomer = false;

	protected boolean isRequiredAsp = false;
	protected boolean isRequiredIsp = false;
	protected boolean isRequiredMerchant = false;
	protected boolean isRequiredCustomer = false;

	protected Set<InterfaceOption> options = new LinkedHashSet<InterfaceOption>();

	@Column
	@NotEmpty
	@Length(max = MAXLEN_NAME)
	public String getName() {
		return name;
	}

	/**
	 * @return Returns the code.
	 */
	@Column
	@NotEmpty
	@Length(max = MAXLEN_CODE)
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
	 * @return Returns the description.
	 */
	@Column
	@NotEmpty
	@Length(max = MAXLEN_DESCRIPTION)
	public String getDescription() {
		return description;
	}

	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return Returns the isAvailableAsp.
	 */
	@Column(name = "is_available_asp")
	@NotNull
	public boolean getIsAvailableAsp() {
		return isAvailableAsp;
	}

	/**
	 * @param isAvailableAsp The isAvailableAsp to set.
	 */
	public void setIsAvailableAsp(boolean isAvailableAsp) {
		this.isAvailableAsp = isAvailableAsp;
	}

	/**
	 * @return Returns the isAvailableCustomer.
	 */
	@Column(name = "is_available_customer")
	@NotNull
	public boolean getIsAvailableCustomer() {
		return isAvailableCustomer;
	}

	/**
	 * @param isAvailableCustomer The isAvailableCustomer to set.
	 */
	public void setIsAvailableCustomer(boolean isAvailableCustomer) {
		this.isAvailableCustomer = isAvailableCustomer;
	}

	/**
	 * @return Returns the isAvailableIsp.
	 */
	@Column(name = "is_available_isp")
	@NotNull
	public boolean getIsAvailableIsp() {
		return isAvailableIsp;
	}

	/**
	 * @param isAvailableIsp The isAvailableIsp to set.
	 */
	public void setIsAvailableIsp(boolean isAvailableIsp) {
		this.isAvailableIsp = isAvailableIsp;
	}

	/**
	 * @return Returns the isAvailableMerchant.
	 */
	@Column(name = "is_available_merchant")
	@NotNull
	public boolean getIsAvailableMerchant() {
		return isAvailableMerchant;
	}

	/**
	 * @param isAvailableMerchant The isAvailableMerchant to set.
	 */
	public void setIsAvailableMerchant(boolean isAvailableMerchant) {
		this.isAvailableMerchant = isAvailableMerchant;
	}

	/**
	 * @return Returns the isRequiredAsp.
	 */
	@Column(name = "is_required_asp")
	@NotNull
	public boolean getIsRequiredAsp() {
		return isRequiredAsp;
	}

	/**
	 * @param isRequiredAsp The isRequiredAsp to set.
	 */
	public void setIsRequiredAsp(boolean isRequiredAsp) {
		this.isRequiredAsp = isRequiredAsp;
	}

	/**
	 * @return Returns the isRequiredCustomer.
	 */
	@Column(name = "is_required_customer")
	@NotNull
	public boolean getIsRequiredCustomer() {
		return isRequiredCustomer;
	}

	/**
	 * @param isRequiredCustomer The isRequiredCustomer to set.
	 */
	public void setIsRequiredCustomer(boolean isRequiredCustomer) {
		this.isRequiredCustomer = isRequiredCustomer;
	}

	/**
	 * @return Returns the isRequiredIsp.
	 */
	@Column(name = "is_required_isp")
	@NotNull
	public boolean getIsRequiredIsp() {
		return isRequiredIsp;
	}

	/**
	 * @param isRequiredIsp The isRequiredIsp to set.
	 */
	public void setIsRequiredIsp(boolean isRequiredIsp) {
		this.isRequiredIsp = isRequiredIsp;
	}

	/**
	 * @return Returns the isRequiredMerchant.
	 */
	@Column(name = "is_required_merchant")
	@NotNull
	public boolean getIsRequiredMerchant() {
		return isRequiredMerchant;
	}

	/**
	 * @param isRequiredMerchant The isRequiredMerchant to set.
	 */
	public void setIsRequiredMerchant(boolean isRequiredMerchant) {
		this.isRequiredMerchant = isRequiredMerchant;
	}

	/**
	 * @return Returns the options.
	 */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "interface_id", nullable = false)
	@org.hibernate.annotations.Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@BusinessKeyUniqueness(type = "option")
	@Valid
	public Set<InterfaceOption> getOptions() {
		return options;
	}

	/**
	 * @param options The options to set.
	 */
	public void setOptions(Set<InterfaceOption> options) {
		this.options = options;
	}

	@Transient
	public InterfaceOption getOption(int id) {
		return findEntityInCollection(options, id);
	}

	@Transient
	public InterfaceOption getOption(String name) {
		return findNamedEntityInCollection(options, name);
	}

	@Transient
	public void addOption(InterfaceOption e) {
		addEntityToCollection(options, e);
	}

	@Transient
	public void addOptions(Collection<InterfaceOption> clc) {
		addEntitiesToCollection(clc, options);
	}

	@Transient
	public void removeOption(InterfaceOption e) {
		removeEntityFromCollection(options, e);
	}

	@Transient
	public void clearOptions() {
		clearEntityCollection(options);
	}

	@Transient
	public int getNumOptions() {
		return getCollectionSize(options);
	}
}