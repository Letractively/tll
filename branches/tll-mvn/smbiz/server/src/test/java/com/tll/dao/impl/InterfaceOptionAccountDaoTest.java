/*
 * The Logic Lab 
 */
package com.tll.dao.impl;

import javax.persistence.EntityNotFoundException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tll.dao.AbstractEntityDaoTest;
import com.tll.model.Account;
import com.tll.model.Asp;
import com.tll.model.Currency;
import com.tll.model.Interface;
import com.tll.model.InterfaceOption;
import com.tll.model.InterfaceOptionAccount;
import com.tll.model.InterfaceOptionParameterDefinition;
import com.tll.model.InterfaceSwitch;
import com.tll.model.key.PrimaryKey;

/**
 * InterfaceOptionAccountDaoTest
 * @author jpk
 */
@Test(groups = "dao")
public class InterfaceOptionAccountDaoTest extends AbstractEntityDaoTest<InterfaceOptionAccount> {

	PrimaryKey<Account> aKey;
	PrimaryKey<Interface> iKey;
	int numParameters = 0;
	String removedParamName;

	/**
	 * Constructor
	 */
	public InterfaceOptionAccountDaoTest() {
		super(InterfaceOptionAccount.class, false);
	}

	@Override
	protected void assembleTestEntity(InterfaceOptionAccount e) throws Exception {
		Account account;
		if(aKey == null) {
			account = getMockEntityProvider().getEntityCopy(Asp.class, true);
			account.setCurrency(getEntityDao().persist(getMockEntityProvider().getEntityCopy(Currency.class, true)));
			account.setPaymentInfo(null);
			account.setParent(null);
			account = getEntityDao().persist(account);
			aKey = new PrimaryKey<Account>(account);
		}
		else {
			account = getEntityDao().load(aKey);
		}
		Assert.assertNotNull(account);
		e.setAccount(account);

		// stub interface
		Interface intf;
		if(iKey == null) {
			intf = getMockEntityProvider().getEntityCopy(InterfaceSwitch.class, true);
			final InterfaceOption option = getMockEntityProvider().getEntityCopy(InterfaceOption.class, true);
			final InterfaceOptionParameterDefinition param =
					getMockEntityProvider().getEntityCopy(InterfaceOptionParameterDefinition.class, true);
			option.addParameter(param);
			intf.addOption(option);
			intf = getEntityDao().persist(intf);
			iKey = new PrimaryKey<Interface>(intf);
		}
		else {
			intf = getEntityDao().load(iKey);
		}
		Assert.assertNotNull(intf);

		final InterfaceOption option = intf.getOptions().iterator().next();
		final InterfaceOptionParameterDefinition param = option.getParameters().iterator().next();
		e.setOption(option);
		e.setParameter(param.getName(), "ioa_pvalue");
		numParameters = e.getNumParameters();
	}

	@Override
	protected void afterMethodHook() {
		if(aKey != null) {
			try {
				final Account account = getEntityDao().load(aKey);
				startNewTransaction();
				setComplete();
				getEntityDao().purge(account);
				getEntityDao().purge(account.getCurrency());
				endTransaction();
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			aKey = null;
		}

		if(iKey != null) {
			try {
				final Interface intf = getEntityDao().load(iKey);
				startNewTransaction();
				setComplete();
				getEntityDao().purge(intf);
				endTransaction();
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			iKey = null;
		}
	}

	@Override
	protected void verifyLoadedEntityState(InterfaceOptionAccount e) throws Exception {
		Assert.assertNotNull(e.getOption());
		Assert.assertTrue(e.getNumParameters() == numParameters);
		Assert.assertNotNull(e.getAccount());
	}

	@Override
	protected void alterEntity(InterfaceOptionAccount e) {
		removedParamName = e.getParameters().keySet().iterator().next();
		Assert.assertNotNull(removedParamName);
		e.removeParameter(removedParamName);
	}

	@Override
	protected void verifyEntityAlteration(InterfaceOptionAccount e) throws Exception {
		Assert.assertNotNull(removedParamName);
		Assert.assertTrue(e.getNumParameters() == numParameters - 1);
	}

}
