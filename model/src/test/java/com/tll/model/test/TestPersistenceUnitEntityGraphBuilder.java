/**
 * The Logic Lab
 * @author jpk Nov 15, 2007
 */
package com.tll.model.test;

import java.util.Set;

import org.apache.commons.lang.math.RandomUtils;

import com.google.inject.Inject;
import com.tll.model.AbstractEntityGraphPopulator;

/**
 * TestPersistenceUnitEntityGraphBuilder
 * @author jpk
 */
public final class TestPersistenceUnitEntityGraphBuilder extends AbstractEntityGraphPopulator {

	private static final int numAccounts = 3;

	/**
	 * Constructor
	 * @param ebf
	 */
	@Inject
	public TestPersistenceUnitEntityGraphBuilder(EntityBeanFactory ebf) {
		super(ebf);
	}

	@Override
	protected void stub() throws IllegalStateException {
		try {
			stubRudimentaryEntities();
			stubAccounts();
		}
		catch(final Exception e) {
			throw new IllegalStateException("Unable to stub entity graph: " + e.getMessage(), e);
		}
	}

	private void stubRudimentaryEntities() throws Exception {
		// currency
		add(Currency.class, false);

		// addresses
		addN(Address.class, true, 100);

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
