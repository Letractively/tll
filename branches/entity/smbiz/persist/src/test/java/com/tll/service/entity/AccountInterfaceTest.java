/**
 * The Logic Lab
 * @author jpk
 * @since Sep 8, 2009
 */
package com.tll.service.entity;

import org.apache.commons.lang.math.RandomUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.tll.dao.EntityNotFoundException;
import com.tll.model.Account;
import com.tll.model.AccountInterface;
import com.tll.model.AccountInterfaceOption;
import com.tll.model.AccountInterfaceOptionParameter;
import com.tll.model.Asp;
import com.tll.model.EntityFactory;
import com.tll.model.Interface;
import com.tll.model.InterfaceOption;
import com.tll.model.InterfaceOptionAccount;
import com.tll.model.InterfaceOptionParameterDefinition;
import com.tll.model.InterfaceSwitch;
import com.tll.model.bk.BusinessKeyFactory;
import com.tll.model.bk.IBusinessKey;
import com.tll.service.entity.intf.IInterfaceService;

/**
 * AccountInterfaceTest - Tests the account related interface methods on
 * {@link IInterfaceService}.
 * @author jpk
 */
@Test(groups = "service.entity")
public class AccountInterfaceTest extends AbstractEntityServiceTest {

	public void testSetAccountInterface() throws Exception {
		final Interface intf = stubInterface(true);
		final InterfaceOption io = intf.getOptions().iterator().next();
		final Account a = stub(Asp.class, true);
		final AccountInterface ai = stubAccountInterface(intf, a, false);

		getInterfaceService().setAccountInterface(ai);

		final IBusinessKey<InterfaceOptionAccount> bk = BusinessKeyFactory.create(InterfaceOptionAccount.class, "Option Id and Account Id");
		bk.setPropertyValue("option.id", io.getPrimaryKey());
		bk.setPropertyValue("account.id", a.getPrimaryKey());
		final InterfaceOptionAccount ioa = getDao().load(bk);
		Assert.assertNotNull(ioa);
	}

	public void testLoadAccountInterface() throws Exception {
		final Interface intf = stubInterface(true);
		final Account a = stub(Asp.class, true);
		AccountInterface ai = stubAccountInterface(intf, a, true);

		ai = getInterfaceService().loadAccountInterface(a.getPrimaryKey(), intf.getPrimaryKey());

		Assert.assertNotNull(ai);
	}

	public void testPurgeAccountInterface() throws Exception {
		final Interface intf = stubInterface(true);
		InterfaceOptionAccount ioa = stubIoa(intf, true);

		getInterfaceService().purgeAccountInterface(ioa.accountKey(), intf.getPrimaryKey());

		try {
			ioa = getDao().load(InterfaceOptionAccount.class, ioa.getPrimaryKey());
			Assert.fail("Not purged: " + ioa);
		}
		catch(final EntityNotFoundException e) {
			// expected
		}
	}

	private Interface stubInterface(boolean persist) {
		if(persist) startNewTransaction();
		final Interface intf = stub(InterfaceSwitch.class, false);
		final InterfaceOption io = stub(InterfaceOption.class, false);
		final InterfaceOptionParameterDefinition iopd = stub(InterfaceOptionParameterDefinition.class, false);
		io.addParameter(iopd);
		intf.addOption(io);
		if(persist) {
			setComplete();
			getDao().persist(intf);
			endTransaction();
		}
		return intf;
	}

	private InterfaceOptionAccount stubIoa(Interface intf, boolean persist) {
		if(persist) startNewTransaction();
		final Account a = stub(Asp.class, false);
		final InterfaceOptionAccount ioa = stub(InterfaceOptionAccount.class, false);
		ioa.setAccount(a);
		ioa.setOption(intf.getOptions().iterator().next());
		if(persist) {
			setComplete();
			getDao().persist(intf);
			endTransaction();
		}
		return ioa;
	}

	/**
	 * @return A newly created {@link AccountInterface} instance.
	 * @param intf The associated {@link Interface}
	 * @param a The associated {@link Account}
	 * @param persist
	 */
	private AccountInterface stubAccountInterface(Interface intf, Account a, boolean persist) {
		final EntityFactory efactory = injector.getInstance(EntityFactory.class);
		final AccountInterface ai = efactory.createEntity(AccountInterface.class, false);
		ai.setAccountKey(a.getPrimaryKey());
		ai.setInterfaceKey(intf.getPrimaryKey());
		for(final InterfaceOption io : intf.getOptions()) {
			final AccountInterfaceOption aio = efactory.createEntity(AccountInterfaceOption.class, false);
			aio.setPrimaryKey(io.getPrimaryKey());
			aio.setName(io.getName());
			aio.setCode(io.getCode());
			aio.setDescription(io.getDescription());
			aio.setAnnualPrice(1f);
			aio.setMonthlyPrice(1f);
			aio.setSetUpPrice(1f);
			for(final InterfaceOptionParameterDefinition iopd : io.getParameters()) {
				final AccountInterfaceOptionParameter aiop =
					efactory.createEntity(AccountInterfaceOptionParameter.class, false);
				aiop.setPrimaryKey(iopd.getPrimaryKey());
				aiop.setName(iopd.getName());
				aiop.setCode(iopd.getCode());
				aiop.setDescription(iopd.getDescription());
				aiop.setValue(Integer.toString(RandomUtils.nextInt(100)));
				aio.addParameter(aiop);
			}
			ai.addOption(aio);
		}
		if(persist) {
			startNewTransaction();
			getDao().persist(ai);
			setComplete();
			endTransaction();
		}
		return ai;
	}

	private IInterfaceService getInterfaceService() {
		return injector.getInstance(IInterfaceService.class);
	}
}
