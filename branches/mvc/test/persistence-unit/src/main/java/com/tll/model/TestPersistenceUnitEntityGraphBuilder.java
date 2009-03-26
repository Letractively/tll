/**
 * The Logic Lab
 * @author jpk Nov 15, 2007
 */
package com.tll.model;

import java.util.Set;

import org.apache.commons.lang.math.RandomUtils;

import com.google.inject.Inject;
import com.tll.SystemError;

/**
 * TestPersistenceUnitEntityGraphBuilder
 * @author jpk
 */
public final class TestPersistenceUnitEntityGraphBuilder extends AbstractEntityGraphBuilder {

	private static final int numAccounts = 3;

	/**
	 * Constructor
	 * @param mep
	 */
	@Inject
	public TestPersistenceUnitEntityGraphBuilder(MockEntityFactory mep) {
		super(mep);
	}

	@Override
	protected void stub() {
		try {
			stubRudimentaryEntities();
			stubAccounts();
		}
		catch(final Exception e) {
			throw new SystemError("Unable to stub entity graph: " + e.getMessage(), e);
		}
	}

	private void stubRudimentaryEntities() throws Exception {
		// currency
		add(Currency.class, false);

		// addresses
		addN(Address.class, true, 10);

		// nested entities
		add(NestedEntity.class, false);
	}

	private <A extends Account> A stubAccount(Class<A> type, int num) throws Exception {
		final A a = add(type, false);

		if(num > 0) {
			a.setName(a.getName() + " " + Integer.toString(num));
		}

		a.setCurrency(getNthEntity(Currency.class, 1));

		a.setNestedEntity(getNthEntity(NestedEntity.class, 1));

		// account addresses upto 5
		final int numAddresses = RandomUtils.nextInt(5);
		if(numAddresses > 0) {
			int ai = 0;
			final Set<AccountAddress> set = addN(AccountAddress.class, true, numAddresses);
			for(final AccountAddress aa : set) {
				aa.setAccount(a);
				aa.setAddress(getNthEntity(Address.class, ++ai));
			}
		}

		return a;
	}

	private void stubAccounts() throws Exception {
		for(int i = 0; i < numAccounts; i++) {
			stubAccount(Account.class, i + 1);
		}
	}
}