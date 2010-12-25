/**
 * The Logic Lab
 * @author jpk
 * @since Oct 4, 2009
 */
package com.tll.server.rpc.entity;

import java.lang.annotation.ElementType;
import java.util.List;

import javax.validation.ConstraintViolation;

import org.hibernate.validator.engine.ConstraintViolationImpl;
import org.hibernate.validator.engine.PathImpl;
import org.testng.annotations.Test;

import com.google.inject.Module;
import com.tll.AbstractConfigAwareTest;
import com.tll.dao.db4o.test.TestDb4oPersistenceUnitModule;
import com.tll.model.egraph.EntityBeanFactory;
import com.tll.model.test.Account;
import com.tll.model.test.AccountAddress;
import com.tll.model.test.TestEntityFactory;

/**
 * ClientizePropertyPathTest
 * @author jpk
 */
@Test(groups = {
	"server", "client-persist"
}, enabled = false)
public class ClientizePropertyPathTest extends AbstractConfigAwareTest {

	@Override
	protected void addModules(List<Module> modules) {
		modules.add(new TestDb4oPersistenceUnitModule(null, TestEntityFactory.class));
	}

	private Account getTestEntity() {
		final EntityBeanFactory ebf = injector.getInstance(EntityBeanFactory.class);
		final Account account = ebf.getEntityCopy(Account.class, true);
		final Account parent = ebf.getEntityCopy(Account.class, true);
		final AccountAddress aa1 = ebf.getEntityCopy(AccountAddress.class, true);
		final AccountAddress aa2 = ebf.getEntityCopy(AccountAddress.class, true);

		account.setParent(parent);
		account.addAccountAddress(aa1);
		account.addAccountAddress(aa2);

		return account;
	}

	@SuppressWarnings("unused")
	private ConstraintViolation<Account> getTestConstraintViolation() {
		final Account account = getTestEntity();
		final AccountAddress accountAddress = account.getAddresses().iterator().next();

		final PathImpl path = PathImpl.createPathFromString("addresses[].address.lastName");

		return
		new ConstraintViolationImpl<Account>(null, "error msg", Account.class, account, accountAddress, null,
				path, null, ElementType.METHOD);
	}

	public void test() throws Exception {
		// TODO
	}
}
