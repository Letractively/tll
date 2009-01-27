/*
 * The Logic Lab 
 */
package com.tll.dao;

import org.testng.annotations.Test;

import com.tll.model.Isp;

/**
 * AccountDaoTest_Isp
 * @author jpk
 */
@Test(groups = "dao")
public class AccountDaoTest_Isp extends AbstractAccountDaoTest<Isp> {

  /**
   * Constructor
   */
  public AccountDaoTest_Isp() {
    super(Isp.class);
  }

}
