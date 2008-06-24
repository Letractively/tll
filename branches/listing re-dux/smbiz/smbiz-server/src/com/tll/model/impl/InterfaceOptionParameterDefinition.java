package com.tll.model.impl;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.tll.model.IEntity;
import com.tll.model.key.BusinessKeyDefinition;
import com.tll.model.key.IBusinessKeyDefinition;

/**
 * Interface option parameter definition entity
 * @author jpk
 */
@Entity
@DiscriminatorValue("paramdef")
public class InterfaceOptionParameterDefinition extends InterfaceOptionBase {

	private static final long serialVersionUID = -5035826060156754280L;

	public static final IBusinessKeyDefinition CodeBk =
			new BusinessKeyDefinition(InterfaceOptionParameterDefinition.class, "Code", new String[] { "code" });

	public Class<? extends IEntity> entityClass() {
		return InterfaceOptionParameterDefinition.class;
	}

}