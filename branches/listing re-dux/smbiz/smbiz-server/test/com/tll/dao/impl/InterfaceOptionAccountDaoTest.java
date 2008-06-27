/*
 * The Logic Lab 
 */
package com.tll.dao.impl;

import javax.persistence.EntityNotFoundException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tll.dao.AbstractDaoTest;
import com.tll.model.impl.Account;
import com.tll.model.impl.Asp;
import com.tll.model.impl.Currency;
import com.tll.model.impl.Interface;
import com.tll.model.impl.InterfaceOption;
import com.tll.model.impl.InterfaceOptionAccount;
import com.tll.model.impl.InterfaceOptionParameterDefinition;
import com.tll.model.impl.InterfaceSwitch;
import com.tll.model.key.PrimaryKey;

/**
 * InterfaceOptionAccountDaoTest
 * @author jpk
 */
@Test(groups = "dao")
public class InterfaceOptionAccountDaoTest extends AbstractDaoTest<InterfaceOptionAccount> {

	PrimaryKey<Account> aKey;
	PrimaryKey<Interface> iKey;
	int numParameters = 0;
	String removedParamName;

	/**
	 * Constructor
	 */
	public InterfaceOptionAccountDaoTest() {
		super(InterfaceOptionAccount.class, IInterfaceOptionAccountDao.class, false);
	}

	@Override
	protected void assembleTestEntity(InterfaceOptionAccount e) throws Exception {
		Account account;
		if(aKey == null) {
			account = getMockEntityProvider().getEntityCopy(Asp.class);
			account.setCurrency(getDao(ICurrencyDao.class).persist(getMockEntityProvider().getEntityCopy(Currency.class)));
			account.setPaymentInfo(null);
			account.setParent(null);
			account = getDao(IAccountDao.class).persist(account);
			aKey = new PrimaryKey<Account>(account);
		}
		else {
			account = getDao(IAccountDao.class).load(aKey);
		}
		Assert.assertNotNull(account);
		e.setAccount(account);

		// stub interface
		Interface intf;
		if(iKey == null) {
			intf = getMockEntityProvider().getEntityCopy(InterfaceSwitch.class);
			final InterfaceOption option = getMockEntityProvider().getEntityCopy(InterfaceOption.class);
			final InterfaceOptionParameterDefinition param =
					getMockEntityProvider().getEntityCopy(InterfaceOptionParameterDefinition.class);
			option.addParameter(param);
			intf.addOption(option);
			intf = getDao(IInterfaceDao.class).persist(intf);
			iKey = new PrimaryKey<Interface>(intf);
		}
		else {
			intf = getDao(IInterfaceDao.class).load(iKey);
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
				final Account account = getDao(IAccountDao.class).load(aKey);
				startNewTransaction();
				setComplete();
				getDao(IAccountDao.class).purge(account);
				getDao(ICurrencyDao.class).purge(account.getCurrency());
				endTransaction();
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			aKey = null;
		}

		if(iKey != null) {
			try {
				final Interface intf = getDao(IInterfaceDao.class).load(iKey);
				startNewTransaction();
				setComplete();
				getDao(IInterfaceDao.class).purge(intf);
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
