/*
 * The Logic Lab 
 */
package com.tll.dao.impl;

import org.testng.annotations.Test;

import com.tll.model.impl.Asp;

/**
 * AccountDaoTest_Asp
 * @author jpk
 */
@Test(groups = "dao")
public class AccountDaoTest_Asp extends AbstractAccountDaoTest<Asp> {

	/**
	 * Constructor
	 */
	public AccountDaoTest_Asp() {
		super(Asp.class);
	}

	/*
	public void testAccountTypePropertyImmutability() throws Exception {
		Asp asp = getTestEntity();
		asp.setAccountType(2);
		dao.persist(asp);
		Asp pasp = getEntityFromDb(dao, KeyFactory.getPrimaryKey(asp));
		assert pasp.getAccountType() == 0 : "Account type is mutable!";
	}
	*/
}