/**
 * The Logic Lab
 * @author jpk Nov 15, 2007
 */
package com.tll.model.mock;

import java.util.Set;

import org.apache.commons.lang.math.RandomUtils;

import com.tll.SystemError;
import com.tll.model.Account;
import com.tll.model.AccountAddress;
import com.tll.model.Address;
import com.tll.model.Currency;
import com.tll.model.NestedEntity;
import com.tll.model.mock.AbstractEntityGraphBuilder;
import com.tll.model.mock.MockEntityFactory;

/**
 * EntityGraph
 * @author jpk
 */
public final class EntityGraphBuilder extends AbstractEntityGraphBuilder {

	private static final int numAccounts = 3;

	/**
	 * Constructor
	 * @param mep
	 */
	public EntityGraphBuilder(MockEntityFactory mep) {
		super(mep);
	}

	@Override
	protected void stub() {
		try {
			stubRudimentaryEntities();
			stubAccounts();
		}
		catch(Exception e) {
			throw new SystemError("Unable to stub entity graph: " + e.getMessage());
		}
	}

	private void stubRudimentaryEntities() throws Exception {
		// currency
		generateAndAdd(Currency.class, 1, false);

		// addresses
		generateAndAddN(Address.class, 1, 10);

		// nested entities
		generateAndAdd(NestedEntity.class, 1, false);
	}

	private <A extends Account> A stubAccount(Class<A> type, int num) throws Exception {
		A a = generateAndAdd(type, 1, false);

		if(num > 0) {
			a.setName(a.getName() + " " + Integer.toString(num));
		}

		a.setCurrency(getNthEntity(Currency.class, 1));

		a.setNestedEntity(getNthEntity(NestedEntity.class, 1));

		// account addresses upto 5
		int numAddresses = RandomUtils.nextInt(5);
		if(numAddresses > 0) {
			Set<AccountAddress> set = generateAndAddN(AccountAddress.class, 1, numAddresses);
			for(AccountAddress aa : set) {
				aa.setAccount(a);
				aa.setAddress(getRandomExisting(Address.class));
			}
		}

		return a;
	}

	private void stubAccounts() throws Exception {
		Set<Account> accounts = getNonNullEntitySet(Account.class);
		for(int i = 0; i < numAccounts; i++) {
			accounts.add(stubAccount(Account.class, i + 1));
		}
	}
}
