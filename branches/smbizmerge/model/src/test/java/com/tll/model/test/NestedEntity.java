package com.tll.model.test;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.tll.model.IEntity;
import com.tll.model.INamedEntity;
import com.tll.model.NamedEntity;
import com.tll.model.Nested;
import com.tll.model.bk.BusinessKeyDef;
import com.tll.model.bk.BusinessObject;

/**
 * NestedEntity
 * @see NestedData For the actual field list.
 * @author jpk
 */
@BusinessObject(businessKeys = @BusinessKeyDef(name = "Name", properties = { INamedEntity.NAME }))
public class NestedEntity extends NamedEntity {
	private static final long serialVersionUID = -4655882279629798747L;

	private NestedData nestedData;

	@Override
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

	@Override
	public String getEntityType() {
		return "Nested Entity";
	}

	@Override
	@NotEmpty
	public String getName() {
		return name;
	}

	@NotNull
	@Valid
	@Nested
	public NestedData getNestedData() {
		return nestedData;
	}

	public void setNestedData(NestedData nestedData) {
		this.nestedData = nestedData;
	}

	public void clearNestedData() {
		this.nestedData = null;
	}
}
