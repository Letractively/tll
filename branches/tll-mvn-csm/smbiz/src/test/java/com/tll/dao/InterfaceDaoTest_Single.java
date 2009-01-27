/*
 * The Logic Lab 
 */
package com.tll.dao;

import org.testng.annotations.Test;

import com.tll.model.InterfaceSingle;

/**
 * InterfaceDaoTest_Single
 * @author jpk
 */
@Test(groups = "dao")
public class InterfaceDaoTest_Single extends AbstractInterfaceDaoTest<InterfaceSingle> {

  /**
   * Constructor
   */
  public InterfaceDaoTest_Single() {
    super(InterfaceSingle.class);
  }

}
