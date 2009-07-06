package com.tll.model;

import javax.jdo.annotations.Discriminator;
import javax.jdo.annotations.PersistenceCapable;

import com.tll.model.schema.BusinessKeyDef;
import com.tll.model.schema.BusinessObject;

/**
 * Interface option parameter definition entity
 * @author jpk
 */
@PersistenceCapable
@Discriminator(value = "paramdef")
@BusinessObject(businessKeys = @BusinessKeyDef(name = "Code", properties = { "code" }))
public class InterfaceOptionParameterDefinition extends InterfaceOptionBase {

	private static final long serialVersionUID = -5035826060156754280L;

	public Class<? extends IEntity> entityClass() {
		return InterfaceOptionParameterDefinition.class;
	}
}