/**
 * The Logic Lab
 * @author jpk Nov 15, 2007
 */
package com.tll.dao.mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.math.RandomUtils;

import com.google.inject.Inject;
import com.tll.SystemError;
import com.tll.model.IEntity;
import com.tll.model.MockEntityProvider;
import com.tll.model.impl.Account;
import com.tll.model.impl.AccountAddress;
import com.tll.model.impl.Address;
import com.tll.model.impl.AddressType;
import com.tll.model.impl.AppProperty;
import com.tll.model.impl.Asp;
import com.tll.model.impl.Authority;
import com.tll.model.impl.Currency;
import com.tll.model.impl.Customer;
import com.tll.model.impl.CustomerAccount;
import com.tll.model.impl.Interface;
import com.tll.model.impl.InterfaceMulti;
import com.tll.model.impl.InterfaceOption;
import com.tll.model.impl.InterfaceOptionParameterDefinition;
import com.tll.model.impl.InterfaceSingle;
import com.tll.model.impl.InterfaceSwitch;
import com.tll.model.impl.Isp;
import com.tll.model.impl.Merchant;
import com.tll.model.impl.PaymentInfo;
import com.tll.model.impl.User;

/**
 * EntityGraph
 * @author jpk
 */
public final class EntityGraph {

	private static final Map<Class<? extends IEntity>, Set<? extends IEntity>> map =
			new HashMap<Class<? extends IEntity>, Set<? extends IEntity>>();

	private MockEntityProvider mep;

	private Asp asp;

	/**
	 * Constructor
	 * @param mep
	 */
	@Inject
	public EntityGraph(MockEntityProvider mep) {
		super();
		this.mep = mep;
		stub();
	}

	@SuppressWarnings("unchecked")
	public <E extends IEntity> Set<E> getEntitySet(Class<E> type) {
		return (Set<E>) map.get(type);
	}

	public <E extends IEntity> E getFirstEntity(Class<E> type) {
		return getEntitySet(type).iterator().next();
	}

	/**
	 * Attempts to retrive the Nth (1-based) entity of the given type.
	 * @param <E>
	 * @param type
	 * @param n 1-based
	 * @return the Nth entity or <code>null</code> if n is greater than the
	 *         number of entities of the given type.
	 */
	@SuppressWarnings("unchecked")
	public <E extends IEntity> E getNthEntity(Class<E> type, int n) {
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

	private static final int numIsps = 3;
	private static final int numMerchants = numIsps * numIsps;
	private static final int numCustomers = 2 + numIsps * 2 + numMerchants * 2;

	private void stub() {
		try {
			stubRudimentaryEntities();

			stubAccounts();

			stubInterfaces();

			stubUsers();
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

		// app properties
		Set<AppProperty> aps = new LinkedHashSet<AppProperty>();
		AppProperty ap = setVersion(mep.getEntityCopy(AppProperty.class, false));
		ap.setName("locale");
		ap.setValue("US");
		aps.add(ap);
		ap = setVersion(mep.getEntityCopy(AppProperty.class, false));
		ap.setName("default.iso4217");
		ap.setValue("usd");
		aps.add(ap);
		ap = setVersion(mep.getEntityCopy(AppProperty.class, false));
		ap.setName("default.country");
		ap.setValue("usa");
		aps.add(ap);
		map.put(AppProperty.class, aps);

		// addresses
		map.put(Address.class, setVersion(mep.getNEntityCopies(Address.class, 10, true)));

		// payment infos
		Set<PaymentInfo> pis = new LinkedHashSet<PaymentInfo>();
		pis.add(setVersion(mep.getEntityCopy(PaymentInfo.class, false)));
		map.put(PaymentInfo.class, pis);

		// user authorities
		map.put(Authority.class, setVersion(mep.getAllEntityCopies(Authority.class)));
	}

	@SuppressWarnings("unchecked")
	private <A extends Account> A stubAccount(Class<A> type) throws Exception {
		A a = setVersion(mep.getEntityCopy(type, false));

		a.setCurrency(getFirstEntity(Currency.class));

		a.setPaymentInfo(getFirstEntity(PaymentInfo.class));

		// account addresses
		AddressType ats[] = AddressType.values();
		Set<AccountAddress> aas = setVersion(mep.getNEntityCopies(AccountAddress.class, ats.length, false));
		List<AccountAddress> aaList = new ArrayList<AccountAddress>(aas);
		Iterator<AccountAddress> itr = aas.iterator();
		int i = 0;

		for(AddressType at : ats) {
			AccountAddress aa = itr.next();
			aa.setAddress(getNthEntity(Address.class, i++ + 1));
			aa.setType(at);
			aa.setName(at.getName() + " address");
		}

		// create a random number of account addresses for this account (for non-Asp
		// accounts)
		int maxAdrsIndex = Asp.class.equals(type) ? ats.length : RandomUtils.nextInt(ats.length);
		i = 0;
		for(i = maxAdrsIndex + 1; i < ats.length; i++) {
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

	@SuppressWarnings("unchecked")
	private void stubAccounts() throws Exception {
		// asp
		asp = stubAccount(Asp.class);
		asp.setName(Asp.ASP_NAME);
		Set<Asp> asps = new LinkedHashSet<Asp>();
		asps.add(asp);
		map.put(Asp.class, asps);

		// isps
		Set<Isp> isps = new LinkedHashSet<Isp>();
		for(int i = 0; i < numIsps; i++) {
			Isp isp = stubAccount(Isp.class);
			isp.setParent(asp);
			isps.add(isp);
		}
		map.put(Isp.class, isps);
		Isp[] arrIsp = isps.toArray(new Isp[isps.size()]);

		// merchants
		Set<Merchant> merchants = new LinkedHashSet<Merchant>();
		for(int i = 0; i < numMerchants; i++) {
			Merchant m = stubAccount(Merchant.class);
			int ispIndex = i / numIsps;
			m.setParent(arrIsp[ispIndex]);
			merchants.add(m);
		}
		map.put(Merchant.class, merchants);
		Merchant[] arrMerchant = merchants.toArray(new Merchant[merchants.size()]);

		// customers
		Set<Customer> customers = new LinkedHashSet<Customer>();
		for(int i = 0; i < numCustomers; i++) {
			Customer c = stubAccount(Customer.class);

			// customer account
			CustomerAccount ca = setVersion(mep.getEntityCopy(CustomerAccount.class, false));
			ca.setCustomer(c);

			if(i < 2) {
				ca.setAccount(asp);
			}
			else if(i < 2 + 2 * numIsps) {
				int ispIndex = i / (2 * numIsps); // TODO verify the math
				ca.setAccount(arrIsp[ispIndex]);
			}
			else {
				int merchantIndex = i / (2 * numMerchants); // TODO verify the math
				ca.setAccount(arrMerchant[merchantIndex]);
			}

			customers.add(c);
			Set<CustomerAccount> caset = (Set<CustomerAccount>) map.get(CustomerAccount.class);
			if(caset == null) {
				caset = new LinkedHashSet<CustomerAccount>();
				map.put(CustomerAccount.class, caset);
			}
			caset.add(ca);
		}
		map.put(Customer.class, customers);

		Set<Account> accounts = new LinkedHashSet<Account>();
		accounts.add(asp);
		accounts.addAll(isps);
		accounts.addAll(merchants);
		accounts.addAll(customers);
		map.put(Account.class, accounts);

	}

	private static InterfaceOption findInterfaceOption(String ioCode, Set<InterfaceOption> options) {
		for(InterfaceOption io : options) {
			if(ioCode.equals(io.getCode())) {
				return io;
			}
		}
		return null;
	}

	private static InterfaceOptionParameterDefinition findParameterDefinition(String pdCode,
			Set<InterfaceOptionParameterDefinition> params) {
		for(InterfaceOptionParameterDefinition pd : params) {
			if(pdCode.equals(pd.getCode())) {
				return pd;
			}
		}
		return null;
	}

	private void stubInterfaces() throws Exception {
		Set<Interface> intfs = new LinkedHashSet<Interface>();
		intfs.addAll(setVersion(mep.getAllEntityCopies(InterfaceSingle.class)));
		intfs.addAll(setVersion(mep.getAllEntityCopies(InterfaceSwitch.class)));
		intfs.addAll(setVersion(mep.getAllEntityCopies(InterfaceMulti.class)));
		Set<InterfaceOption> ios = setVersion(mep.getAllEntityCopies(InterfaceOption.class));
		Set<InterfaceOptionParameterDefinition> pds =
				setVersion(mep.getAllEntityCopies(InterfaceOptionParameterDefinition.class));

		for(Interface intf : intfs) {
			if(Interface.CODE_CROSS_SELL.equals(intf.getCode())) {
				InterfaceOption io = findInterfaceOption("crosssell-switch", ios);
				if(io != null) {
					intf.addOption(io);
				}
			}
			else if(Interface.CODE_PAYMENT_METHOD.equals(intf.getCode())) {
				InterfaceOption io = findInterfaceOption("visa", ios);
				if(io != null) {
					intf.addOption(io);
				}
				io = findInterfaceOption("mc", ios);
				if(io != null) {
					intf.addOption(io);
				}
			}
			else if(Interface.CODE_PAYMENT_PROCESSOR.equals(intf.getCode())) {
				InterfaceOption io = findInterfaceOption("native_payproc", ios);
				if(io != null) {
					intf.addOption(io);
				}
				io = findInterfaceOption("verisign_payproc", ios);
				if(io != null) {
					InterfaceOptionParameterDefinition pd = findParameterDefinition("verisignP1", pds);
					if(pd != null) {
						io.addParameter(pd);
					}
					pd = findParameterDefinition("verisignP2", pds);
					if(pd != null) {
						io.addParameter(pd);
					}
					intf.addOption(io);
				}
			}
			else if(Interface.CODE_SALES_TAX.equals(intf.getCode())) {
				InterfaceOption io = findInterfaceOption("native_salestax", ios);
				if(io != null) {
					intf.addOption(io);
				}
			}
			else if(Interface.CODE_SHIP_METHOD.equals(intf.getCode())) {
				InterfaceOption io = findInterfaceOption("native_shipmethod", ios);
				if(io != null) {
					intf.addOption(io);
				}
			}
		}
		map.put(Interface.class, intfs);
	}

	private void stubUsers() throws Exception {
		User u = setVersion(mep.getEntityCopy(User.class, false));
		u.addAuthority((Authority) map.get(Authority.class).iterator().next());
		u.setAccount(asp);
		u.setAddress((Address) map.get(Address.class).iterator().next());
		Set<User> users = new LinkedHashSet<User>();
		users.add(u);
		map.put(User.class, users);
	}

}
