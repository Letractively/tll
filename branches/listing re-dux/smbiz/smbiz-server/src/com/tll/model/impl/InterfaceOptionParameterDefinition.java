package com.tll.model.impl;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import com.tll.client.model.IPropertyValue;
import com.tll.client.model.StringPropertyValue;
import com.tll.model.IEntity;
import com.tll.model.key.BusinessKey;

/**
 * Interface option parameter definition entity
 * @author jpk
 */
@Entity
@DiscriminatorValue("paramdef")
public class InterfaceOptionParameterDefinition extends InterfaceOptionBase {

	private static final long serialVersionUID = -5035826060156754280L;

	public Class<? extends IEntity> entityClass() {
		return InterfaceOptionParameterDefinition.class;
	}

	@Override
	@Transient
	public BusinessKey[] getBusinessKeys() {
		return new BusinessKey[] { new BusinessKey(InterfaceOptionParameterDefinition.class, "Code",
				new IPropertyValue[] { new StringPropertyValue("code", getCode()) }) };
	}
}