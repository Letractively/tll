/*
 * The Logic Lab 
 */
package com.tll.dao;

import org.testng.annotations.Test;

import com.tll.model.Customer;

/**
 * AccountDaoTest_Customer
 * @author jpk
 */
@Test(groups = "dao")
public class AccountDaoTest_Customer extends AbstractAccountDaoTest<Customer> {

  /**
   * Constructor
   */
  public AccountDaoTest_Customer() {
    super(Customer.class);
  }

}
