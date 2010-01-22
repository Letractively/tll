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
import com.tll.model.PrimaryKey;

/**
 * InterfaceOptionAccountDaoTestHandler
 * @author jpk
 */
public class InterfaceOptionAccountDaoTestHandler extends AbstractEntityDaoTestHandler<InterfaceOptionAccount> {

	private PrimaryKey<Currency> pkC;
	private PrimaryKey<Asp> pkA;
	private PrimaryKey<Interface> pkI;

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
		pkC = new PrimaryKey<Currency>(currency);

		Asp account = create(Asp.class, true);
		account.setCurrency(currency);
		account = persist(account);
		pkA = new PrimaryKey<Asp>(account);

		Interface intf = create(InterfaceSwitch.class, true);
		final InterfaceOption option = create(InterfaceOption.class, true);
		final InterfaceOptionParameterDefinition param = create(InterfaceOptionParameterDefinition.class, true);
		option.addParameter(param);
		intf.addOption(option);
		intf = persist(intf);
		pkI = new PrimaryKey<Interface>(intf);
	}

	@Override
	public void purgeDependentEntities() {
		purge(pkI); pkI = null;
		purge(pkA); pkA = null;
		purge(pkC); pkC = null;
	}

	@Override
	public void assembleTestEntity(InterfaceOptionAccount e) throws Exception {
		e.setAccount(load(pkA));
		final Interface inter = load(pkI);
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
