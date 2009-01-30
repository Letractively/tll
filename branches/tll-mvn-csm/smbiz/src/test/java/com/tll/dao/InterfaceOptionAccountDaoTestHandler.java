/*
 * The Logic Lab 
 */
package com.tll.dao;

import javax.persistence.EntityNotFoundException;

import org.testng.Assert;

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
 * InterfaceOptionAccountDaoTestHandler
 * @author jpk
 */
public class InterfaceOptionAccountDaoTestHandler extends AbstractEntityDaoTestHandler<InterfaceOptionAccount> {

	PrimaryKey<Account> aKey;
	PrimaryKey<Interface> iKey;
	int numParameters = 0;
	String removedParamName;

	@Override
	public Class<InterfaceOptionAccount> entityClass() {
		return InterfaceOptionAccount.class;
	}

	@Override
	public void assembleTestEntity(InterfaceOptionAccount e) throws Exception {
		Account account;
		if(aKey == null) {
			account = mockEntityFactory.getEntityCopy(Asp.class, true);
			account.setCurrency(entityDao.persist(mockEntityFactory.getEntityCopy(Currency.class, true)));
			account.setPaymentInfo(null);
			account.setParent(null);
			account = entityDao.persist(account);
			aKey = new PrimaryKey<Account>(account);
		}
		else {
			account = entityDao.load(aKey);
		}
		Assert.assertNotNull(account);
		e.setAccount(account);

		// stub interface
		Interface intf;
		if(iKey == null) {
			intf = mockEntityFactory.getEntityCopy(InterfaceSwitch.class, true);
			final InterfaceOption option = mockEntityFactory.getEntityCopy(InterfaceOption.class, true);
			final InterfaceOptionParameterDefinition param =
					mockEntityFactory.getEntityCopy(InterfaceOptionParameterDefinition.class, true);
			option.addParameter(param);
			intf.addOption(option);
			intf = entityDao.persist(intf);
			iKey = new PrimaryKey<Interface>(intf);
		}
		else {
			intf = entityDao.load(iKey);
		}
		Assert.assertNotNull(intf);

		final InterfaceOption option = intf.getOptions().iterator().next();
		final InterfaceOptionParameterDefinition param = option.getParameters().iterator().next();
		e.setOption(option);
		e.setParameter(param.getName(), "ioa_pvalue");
		numParameters = e.getNumParameters();
	}

	@Override
	public void teardownTestEntity(InterfaceOptionAccount e) {
		if(aKey != null) {
			try {
				final Account account = entityDao.load(aKey);
				entityDao.purge(account);
				entityDao.purge(account.getCurrency());
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			aKey = null;
		}

		if(iKey != null) {
			try {
				final Interface intf = entityDao.load(iKey);
				entityDao.purge(intf);
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			iKey = null;
		}
	}

	@Override
	public void verifyLoadedEntityState(InterfaceOptionAccount e) throws Exception {
		super.verifyLoadedEntityState(e);
		Assert.assertNotNull(e.getOption());
		Assert.assertTrue(e.getNumParameters() == numParameters);
		Assert.assertNotNull(e.getAccount());
	}

	@Override
	public void alterTestEntity(InterfaceOptionAccount e) {
		super.alterTestEntity(e);
		removedParamName = e.getParameters().keySet().iterator().next();
		Assert.assertNotNull(removedParamName);
		e.removeParameter(removedParamName);
	}

	@Override
	public void verifyEntityAlteration(InterfaceOptionAccount e) throws Exception {
		super.verifyEntityAlteration(e);
		Assert.assertNotNull(removedParamName);
		Assert.assertTrue(e.getNumParameters() == numParameters - 1);
	}

}
