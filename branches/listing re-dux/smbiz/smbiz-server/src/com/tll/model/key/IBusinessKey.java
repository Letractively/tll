package com.tll.model.key;

import com.tll.model.IEntity;

/**
 * Represents a business key for a particular entity type.
 * <p>
 * 
 * @see IBusinessKeyDefinition for a business key definition
 * @author jpk
 */
public interface IBusinessKey<E extends IEntity> extends IBusinessKeyDefinition<E> {

  /**
   * Provides a way to generically interrogate a business key used in
   * conjunction with {@link IBusinessKeyDefinition#getFieldNames()}. Returns
   * <code>null</code> when the field is not set.
   * 
   * @see IBusinessKeyDefinition#getFieldNames()
   * @param fieldName
   * @return the field value
   */
  Object getFieldValue(String fieldName);
  
  /**
   * Provision to generically alter the state of the business key
   * @param fieldName
   * @param fieldValue
   */
  void setFieldValue(String fieldName, Object fieldValue);
}