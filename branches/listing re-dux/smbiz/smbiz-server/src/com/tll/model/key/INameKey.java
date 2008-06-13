/*
 * The Logic Lab 
 */
package com.tll.model.key;

import com.tll.model.INamedEntity;

/**
 * INameKey
 * @author jpk
 */
public interface INameKey<N extends INamedEntity> extends IEntityKey<N> {

  /**
   * @return the name (field value).
   */
  String getName();
  
  /**
   * Sets the name property.
   * @param name
   */
  void setName(String name);
  
  /**
   * @return the field name
   */
  String getFieldName();
}
