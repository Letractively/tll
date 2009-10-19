/*
 * The Logic Lab 
 */
package com.tll.dao;

import org.testng.Assert;

import com.tll.model.Account;
import com.tll.model.Currency;
import com.tll.model.Interface;
import com.tll.model.InterfaceOption;
import com.tll.model.InterfaceOptionAccount;
import com.tll.model.InterfaceOptionParameterDefinition;
import com.tll.model.InterfaceSwitch;

/**
 * InterfaceOptionAccountDaoTestHandler
 * @author jpk
 */
public class InterfaceOptionAccountDaoTestHandler extends AbstractEntityDaoTestHandler<InterfaceOptionAccount> {

	Currency currency;
	Account account;
	Interface intf;
	int numParameters = 0;
	String removedParamName;

	@Override
	public Class<InterfaceOptionAccount> entityClass() {
		return InterfaceOptionAccount.class;
	}

	@Override
	public boolean supportsPaging() {
		// since we can't (currently) create unique multiple instances since the bk
		// is the binding between account/interface option only
		return false;
	}

	@Override
	public void persistDependentEntities() {
		currency = createAndPersist(Currency.class, true);

		account = create(Account.class, true);
		account.setCurrency(currency);
		account = persist(account);

		intf = create(InterfaceSwitch.class, true);
		InterfaceOption option = create(InterfaceOption.class, true);
		InterfaceOptionParameterDefinition param = create(InterfaceOptionParameterDefinition.class, true);
		option.addParameter(param);
		intf.addOption(option);
		intf = persist(intf);
	}

	@Override
	public void purgeDependentEntities() {
		purge(intf);
		purge(account);
		purge(currency);
	}

	@Override
	public void assembleTestEntity(InterfaceOptionAccount e) throws Exception {
		e.setAccount(account);
		InterfaceOption option = intf.getOptions().iterator().next();
		InterfaceOptionParameterDefinition param = option.getParameters().iterator().next();
		e.setOption(option);
		e.setParameter(param.getName(), "ioa_pvalue");
		numParameters = e.getNumParameters();
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
