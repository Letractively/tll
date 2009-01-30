/**
 * The Logic Lab
 * @author jpk Nov 15, 2007
 */
package com.tll.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.inject.Inject;
import com.tll.SystemError;
import com.tll.model.key.PrimaryKey;

/**
 * EntityGraph
 * @author jpk
 */
public final class EntityGraph implements IEntityProvider {

	private static final Map<Class<? extends IEntity>, Set<? extends IEntity>> map =
			new HashMap<Class<? extends IEntity>, Set<? extends IEntity>>();

	private final MockEntityFactory mep;

	/**
	 * Constructor
	 * @param mep
	 */
	@Inject
	public EntityGraph(MockEntityFactory mep) {
		super();
		this.mep = mep;
		stub();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <E extends IEntity> Collection<E> getEntitiesByType(Class<E> type) {
		return (Collection<E>) map.get(type);
	}

	@Override
	public <E extends IEntity> E getEntity(PrimaryKey<E> key) {
		if(key == null || !key.isSet()) {
			throw new IllegalArgumentException("The key is not specified or is not set");
		}
		Collection<? extends E> clc = getEntitiesByType(key.getType());
		if(clc != null) {
			for(E e : clc) {
				if(key.getId().equals(e.getId())) {
					return e;
				}
			}
		}
		return null;
	}

	@Override
	public <E extends IEntity> E getEntityByType(Class<E> type) throws IllegalStateException {
		Collection<? extends E> clc = getEntitiesByType(type);
		if(clc != null && clc.size() != 1) {
			throw new IllegalStateException("More than one entity exists of type: " + type.getName());
		}
		return clc == null ? null : clc.iterator().next();
	}

	@SuppressWarnings("unchecked")
	private <E extends IEntity> Set<E> getEntitySet(Class<E> type) {
		return (Set<E>) map.get(type);
	}

	private <E extends IEntity> E getFirstEntity(Class<E> type) {
		return getEntitySet(type).iterator().next();
	}

	/**
	 * Attempts to retrive the Nth (1-based) entity of the given type.
	 * @param <E>
	 * @param type
	 * @param n 1-based
	 * @return the Nth entity or <code>null</code> if n is greater than the number
	 *         of entities of the given type.
	 */
	@SuppressWarnings("unchecked")
	private <E extends IEntity> E getNthEntity(Class<E> type, int n) {
		Set<E> set = (Set<E>) map.get(type);
		if(set != null && set.size() >= n) {
			int i = 0;
			for(E e : set) {
				if(++i == n) {
					return e;
				}
			}
		}
		return null;
	}

	private static final int numAccounts = 3;

	private void stub() {
		try {
			stubRudimentaryEntities();
			stubAccounts();
		}
		catch(Exception e) {
			throw new SystemError("Unable to stub entity graph: " + e.getMessage());
		}
	}

	private <E extends IEntity> E setVersion(E e) {
		e.setVersion(new Integer(1));
		return e;
	}

	private <E extends IEntity> Set<E> setVersion(Set<E> set) {
		for(IEntity e : set) {
			setVersion(e);
		}
		return set;
	}

	private void stubRudimentaryEntities() throws Exception {
		// currency
		Set<Currency> currencies = new LinkedHashSet<Currency>();
		currencies.add(setVersion(mep.getEntityCopy(Currency.class, false)));
		map.put(Currency.class, currencies);

		// addresses
		map.put(Address.class, setVersion(mep.getNEntityCopies(Address.class, 10, true)));

		// nested entities
		Set<NestedEntity> nes = new LinkedHashSet<NestedEntity>();
		nes.add(setVersion(mep.getEntityCopy(NestedEntity.class, false)));
		map.put(NestedEntity.class, nes);
	}

	@SuppressWarnings("unchecked")
	private <A extends Account> A stubAccount(Class<A> type, int num) throws Exception {
		A a = setVersion(mep.getEntityCopy(type, false));

		if(num > 0) {
			a.setName(a.getName() + " " + Integer.toString(num));
		}

		a.setCurrency(getFirstEntity(Currency.class));

		a.setNestedEntity(getFirstEntity(NestedEntity.class));

		// account addresses
		Set<AccountAddress> aas = setVersion(mep.getNEntityCopies(AccountAddress.class, 3, false));
		List<AccountAddress> aaList = new ArrayList<AccountAddress>(aas);
		Iterator<AccountAddress> itr = aas.iterator();
		
		for(int i = 0; i < 3; i++) {
			AccountAddress aa = itr.next();
			aa.setAddress(getNthEntity(Address.class, i++ + 1));
			aa.setName("Address " + i);
		}

		// create a random number of account addresses for this account
		int upper = 3;
		for(int i = 0; i < upper; i++) {
			if(i > aaList.size() - 1) {
				i = aaList.size() - 1;
			}
			if(i < 0) break;
			aaList.remove(i);
		}

		if(aaList.size() > 0) {
			aas = new LinkedHashSet<AccountAddress>(aaList);
			a.addAccountAddresses(aas);

			// update the general account address set w/ the newly created ones
			Set<AccountAddress> aaset = (Set<AccountAddress>) map.get(AccountAddress.class);
			if(aaset == null) {
				aaset = new LinkedHashSet<AccountAddress>();
				map.put(AccountAddress.class, aaset);
			}
			aaset.addAll(aas);
		}

		return a;
	}

	private void stubAccounts() throws Exception {
		Set<Account> accounts = new LinkedHashSet<Account>();
		for(int i = 0; i < numAccounts; i++) {
			accounts.add(stubAccount(Account.class, i + 1));
		}
		map.put(Account.class, accounts);
	}
}
