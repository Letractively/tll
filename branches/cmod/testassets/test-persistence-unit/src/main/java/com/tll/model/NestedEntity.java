package com.tll.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Valid;

import com.tll.model.schema.BusinessKeyDef;
import com.tll.model.schema.BusinessObject;
import com.tll.model.schema.Nested;

/**
 * NestedEntity
 * @see NestedData For the actual field list.
 * @author jpk
 */
@Entity
@Table(name = "nested_entity")
@BusinessObject(businessKeys = @BusinessKeyDef(name = "Name", properties = { INamedEntity.NAME }))
public class NestedEntity extends NamedEntity {
	private static final long serialVersionUID = -4655882279629798747L;
	
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

	@Column
	@NotEmpty
	public String getName() {
		return name;
	}

	@Column(name = "data")
	@Type(type = "encobj")
	@NotNull
	@Valid
	@Nested
	public NestedData getNestedData() {
		return nestedData;
	}

	public void setNestedData(NestedData paymentData) {
		this.nestedData = paymentData;
	}

	@Transient
	public void clearPaymentData() {
		this.nestedData = null;
	}
}
