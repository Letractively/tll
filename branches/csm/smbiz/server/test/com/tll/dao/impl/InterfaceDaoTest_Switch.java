/*
 * The Logic Lab 
 */
package com.tll.dao.impl;

import org.testng.annotations.Test;

import com.tll.model.impl.InterfaceSwitch;

/**
 * InterfaceDaoTest_Single
 * @author jpk
 */
@Test(groups = "dao")
public class InterfaceDaoTest_Switch extends AbstractInterfaceDaoTest<InterfaceSwitch> {

  /**
   * Constructor
   */
  public InterfaceDaoTest_Switch() {
    super(InterfaceSwitch.class);
  }

}
