/*
 * The Logic Lab
 */
package com.tll.dao;

import org.testng.Assert;

import com.tll.model.Asp;
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

	private Object pkC, pkA, pkI;

	private int numParameters = 0;
	private String removedParamName;

	@Override
	public Class<InterfaceOptionAccount> entityClass() {
		return InterfaceOptionAccount.class;
	}

	@Override
	public boolean supportsPaging() {
		// since we can't (currently) create unique multiple instances since the bk
		// is the binding between pkA/interface option only
		return false;
	}

	@Override
	public void persistDependentEntities() {
		final Currency currency = createAndPersist(Currency.class, true);
		pkC = currency.getId();

		Asp account = create(Asp.class, true);
		account.setCurrency(currency);
		account = persist(account);
		pkA = account.getId();

		Interface intf = create(InterfaceSwitch.class, true);
		final InterfaceOption option = create(InterfaceOption.class, true);
		final InterfaceOptionParameterDefinition param = create(InterfaceOptionParameterDefinition.class, true);
		option.addParameter(param);
		intf.addOption(option);
		intf = persist(intf);
		pkI = intf.getId();
	}

	@Override
	public void purgeDependentEntities() {
		purge(Interface.class, pkI); pkI = null;
		purge(Asp.class, pkA); pkA = null;
		purge(Currency.class, pkC); pkC = null;
	}

	@Override
	public void assembleTestEntity(InterfaceOptionAccount e) throws Exception {
		e.setAccount(load(Asp.class, pkA));
		final Interface inter = load(Interface.class, pkI);
		final InterfaceOption option = inter.getOptions().iterator().next();
		final InterfaceOptionParameterDefinition param = option.getParameters().iterator().next();
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
