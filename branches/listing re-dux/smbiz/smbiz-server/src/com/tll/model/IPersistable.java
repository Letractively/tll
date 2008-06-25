package com.tll.model;

import java.io.Serializable;

/**
 * Indicates the ability for an <code>Object</code> to be persisted.
 * 
 * @author jpk
 */
public interface IPersistable extends Serializable {

  /**
   * @return true if the object has been persisted, false otherwise
   */
  boolean isNew();

  /**
   * @return true if the object's current loaded state is not-consistent w/ that
   *         previously persisted
   */
  boolean isDirty();
}
