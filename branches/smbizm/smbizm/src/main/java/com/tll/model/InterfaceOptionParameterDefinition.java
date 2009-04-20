package com.tll.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.tll.model.IEntity;
import com.tll.model.schema.BusinessKeyDef;
import com.tll.model.schema.BusinessObject;

/**
 * Interface option parameter definition entity
 * @author jpk
 */
@Entity
@DiscriminatorValue("paramdef")
@BusinessObject(businessKeys = @BusinessKeyDef(name = "Code", properties = { "code" }))
public class InterfaceOptionParameterDefinition extends InterfaceOptionBase {

	private static final long serialVersionUID = -5035826060156754280L;

	public Class<? extends IEntity> entityClass() {
		return InterfaceOptionParameterDefinition.class;
	}
}