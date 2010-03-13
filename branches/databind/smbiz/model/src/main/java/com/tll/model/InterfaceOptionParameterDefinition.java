package com.tll.model;

import com.tll.schema.BusinessKeyDef;
import com.tll.schema.BusinessObject;
import com.tll.schema.Extended;

/**
 * Interface option parameter definition entity
 * @author jpk
 */
@Extended
@BusinessObject(businessKeys = @BusinessKeyDef(name = "Code", properties = { "code" }))
public class InterfaceOptionParameterDefinition extends InterfaceOptionBase {

	private static final long serialVersionUID = -5035826060156754280L;

	public Class<? extends IEntity> entityClass() {
		return InterfaceOptionParameterDefinition.class;
	}
}