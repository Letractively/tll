package com.tll.model;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.Unique;
import javax.jdo.annotations.Uniques;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validation.constraints.NotEmpty;

import com.tll.model.schema.Nested;

/**
 * NestedEntity
 * @see NestedData For the actual field list.
 * @author jpk
 */
@PersistenceCapable(detachable = "true")
@Uniques(value = @Unique(name = "Name", members = { INamedEntity.NAME }))
public class NestedEntity extends NamedEntity {
	private static final long serialVersionUID = -4655882279629798747L;

	@Persistent(defaultFetchGroup = "true")
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
