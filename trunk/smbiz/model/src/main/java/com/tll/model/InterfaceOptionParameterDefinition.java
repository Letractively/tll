package com.tll.model;


/**
 * Interface option parameter definition entity
 * @author jpk
 */
@Extended
@BusinessObject(businessKeys = @BusinessKeyDef(name = "Code", properties = { "code" }))
public class InterfaceOptionParameterDefinition extends InterfaceOptionBase {

	private static final long serialVersionUID = -5035826060156754280L;

	@Override
	public Class<? extends IEntity> entityClass() {
		return InterfaceOptionParameterDefinition.class;
	}
}