/*
 * Created on - Mar 11, 2006
 * Coded by   - 'The Logic Lab' - jpk
 * Copywright - 2006 - All rights reserved.
 *
 */

package com.tll.model;

/**
 * Allows persistence frameworks to distinguish between like entities for proper
 * persistence
 * 
 * <p>IMPT: the version property should be <code>Integer</code> as opposed to <code>int</code>
 * to ensure valid transient object checking!
 * 
 * @author jpk
 */
public interface IVersionSupport {

  /**
   * @return the version number
   */
  Integer getVersion();

  /**
   * @param version
   *          the version number
   */
  void setVersion(Integer version);
}
