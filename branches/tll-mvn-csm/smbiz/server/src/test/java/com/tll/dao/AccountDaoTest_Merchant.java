/*
 * The Logic Lab 
 */
package com.tll.dao;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tll.model.Merchant;

/**
 * AccountDaoTest_Merchant
 * @author jpk
 */
@Test(groups = "dao")
public class AccountDaoTest_Merchant extends AbstractAccountDaoTest<Merchant> {

  /**
   * Constructor
   */
  public AccountDaoTest_Merchant() {
    super(Merchant.class);
  }

  @Override
  protected void verifyLoadedEntityState(Merchant e) throws Exception {
    super.verifyLoadedEntityState(e);
    Assert.assertNotNull(e.getStoreName(), "Merchant store name is null");
  }

}
