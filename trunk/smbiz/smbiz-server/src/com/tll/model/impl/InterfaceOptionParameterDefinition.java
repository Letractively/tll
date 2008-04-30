package com.tll.model.impl;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.tll.model.IEntity;

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

}