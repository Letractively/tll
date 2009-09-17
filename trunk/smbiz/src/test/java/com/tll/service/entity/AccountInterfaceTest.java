/**
 * The Logic Lab
 * @author jpk
 * @since Sep 8, 2009
 */
package com.tll.service.entity;

import org.apache.commons.lang.math.RandomUtils;
import org.testng.annotations.Test;

import com.tll.model.Account;
import com.tll.model.AccountInterface;
import com.tll.model.AccountInterfaceOption;
import com.tll.model.AccountInterfaceOptionParameter;
import com.tll.model.IEntityFactory;
import com.tll.model.Interface;
import com.tll.model.InterfaceOption;
import com.tll.model.InterfaceOptionParameterDefinition;
import com.tll.model.key.NameKey;
import com.tll.service.entity.account.IAccountService;
import com.tll.service.entity.intf.IInterfaceService;


/**
 * AccountInterfaceTest - Tests the account related interface methods on
 * {@link IInterfaceService}.
 * @author jpk
 */
@Test(groups = "service.entity")
public class AccountInterfaceTest extends AbstractEntityServiceTest {

	@Override
	protected void beforeMethod() {
		getDbShell().restub();
	}

	private IInterfaceService getInterfaceService() {
		return injector.getInstance(IInterfaceService.class);
	}

	private IAccountService getAccountService() {
		return injector.getInstance(IAccountService.class);
	}

	public void testLoadAccountInterface() throws Exception {
		final IInterfaceService svc = getInterfaceService();
		final Interface intf = svc.load(new NameKey<Interface>(Interface.class, "Payment Processor"));
		final IAccountService asvc = getAccountService();
		final Account a = asvc.load(new NameKey<Account>(Account.class, "asp"));
		final AccountInterface ai = svc.loadAccountInterface(a.getId(), intf.getId());
		assert ai != null;
		assert ai.getId() != null;
	}

	public void testSetAccountInterface() throws Exception {
		final IEntityFactory efactory = injector.getInstance(IEntityFactory.class);
		final AccountInterface ai = efactory.createEntity(AccountInterface.class, false);
		final IInterfaceService svc = getInterfaceService();
		final Interface intf = svc.load(new NameKey<Interface>(Interface.class, "Payment Processor"));
		final IAccountService asvc = getAccountService();
		final Account a = asvc.load(new NameKey<Account>(Account.class, "asp"));
		ai.setAccountId(a.getId());
		ai.setInterfaceId(intf.getId());
		for(final InterfaceOption io : intf.getOptions()) {
			final AccountInterfaceOption aio = efactory.createEntity(AccountInterfaceOption.class, false);
			aio.setId(io.getId());
			aio.setName(io.getName());
			aio.setCode(io.getCode());
			aio.setDescription(io.getDescription());
			aio.setAnnualPrice(1f);
			aio.setMonthlyPrice(1f);
			aio.setSetUpPrice(1f);
			for(final InterfaceOptionParameterDefinition iopd : io.getParameters()) {
				final AccountInterfaceOptionParameter aiop =
					efactory.createEntity(AccountInterfaceOptionParameter.class, false);
				aiop.setId(iopd.getId());
				aiop.setName(iopd.getName());
				aiop.setCode(iopd.getCode());
				aiop.setDescription(iopd.getDescription());
				aiop.setValue(Integer.toString(RandomUtils.nextInt(100)));
				aio.addParameter(aiop);
			}
			ai.addOption(aio);
		}
		svc.setAccountInterface(ai);
		assert ai.getId() != null;
	}

	public void testPurgeAccountInterface() throws Exception {
		final IInterfaceService svc = getInterfaceService();
		final Interface intf = svc.load(new NameKey<Interface>(Interface.class, "Payment Processor"));
		final IAccountService asvc = getAccountService();
		final Account a = asvc.load(new NameKey<Account>(Account.class, "asp"));
		svc.purgeAccountInterface(a.getId(), intf.getId());
		// TODO verify no account interface options
	}

	public void testSetAccountInterfaces() throws Exception {
		// TODO impl
	}

	public void testPurgeAccountInterfacess() throws Exception {
		// TODO impl
	}
}
