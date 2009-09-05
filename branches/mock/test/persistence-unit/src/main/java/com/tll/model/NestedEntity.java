package com.tll.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validation.constraints.NotEmpty;

import com.tll.model.schema.BusinessKeyDef;
import com.tll.model.schema.BusinessObject;
import com.tll.model.schema.Nested;

/**
 * NestedEntity
 * @see NestedData For the actual field list.
 * @author jpk
 */
@BusinessObject(businessKeys = @BusinessKeyDef(name = "Name", properties = { INamedEntity.NAME }))
public class NestedEntity extends NamedEntity {
	private static final long serialVersionUID = -4655882279629798747L;

	@Nested
	private transient NestedData nestedData;

	public Class<? extends IEntity> entityClass() {
		return NestedEntity.class;
	}

	/**
	 * Constructor
	 */
	public NestedEntity() {
		super();
		nestedData = new NestedData();
	}

	@NotEmpty
	public String getName() {
		return name;
	}

	@NotNull
	@Valid
	public NestedData getNestedData() {
		return nestedData;
	}

	public void setNestedData(NestedData paymentData) {
		this.nestedData = paymentData;
	}

	public void clearPaymentData() {
		this.nestedData = null;
	}
}
