/*
 * The Logic Lab 
 */
package com.tll.dao;

import org.testng.annotations.Test;

import com.tll.model.InterfaceMulti;

/**
 * InterfaceDaoTest_Single
 * @author jpk
 */
@Test(groups = "dao")
public class InterfaceDaoTest_Multi extends AbstractInterfaceDaoTest<InterfaceMulti> {

  /**
   * Constructor
   */
  public InterfaceDaoTest_Multi() {
    super(InterfaceMulti.class);
  }

}
