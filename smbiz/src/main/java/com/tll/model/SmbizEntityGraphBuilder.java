/**
 * The Logic Lab
 * @author jpk Nov 15, 2007
 */
package com.tll.model;

import java.util.Set;

import com.google.inject.Inject;
import com.tll.model.test.AbstractEntityGraphPopulator;
import com.tll.model.test.EntityBeanFactory;
import com.tll.util.EnumUtil;

/**
 * SmbizEntityGraphBuilder - Builds an {@link SmbizEntityGraphBuilder} instance.
 * @author jpk
 */
public final class SmbizEntityGraphBuilder extends AbstractEntityGraphPopulator {

	private static final int numIsps = 3;
	private static final int numMerchants = numIsps * numIsps;
	private static final int numCustomers = 2 + numIsps * 2 + numMerchants * 2;

	/**
	 * Constructor
	 * @param ebf the required entity bean factory
	 */
	@Inject
	public SmbizEntityGraphBuilder(EntityBeanFactory ebf) {
		super(ebf);
	}

	@Override
	protected void stub() {
		try {
			stubRudimentaryEntities();
			stubAccounts();
			stubInterfaces();
			stubUsers();
		}
		catch(final Exception e) {
			throw new IllegalStateException("Unable to stub entity graph: " + e.getMessage());
		}
	}

	private void stubRudimentaryEntities() throws Exception {
		add(Currency.class, false);

		// app properties
		AppProperty ap;
		ap = add(AppProperty.class, false);
		ap.setName("locale");
		ap.setValue("US");
		ap = add(AppProperty.class, false);
		ap.setName("default.iso4217");
		ap.setValue("usd");
		ap = add(AppProperty.class, false);
		ap.setName("default.country");
		ap.setValue("usa");

		// addresses
		addN(Address.class, true, 10);

		// payment infos
		add(PaymentInfo.class, false);

		// user authorities
		addAll(Authority.class);
	}

	private <A extends Account> A stubAccount(Class<A> type, int num) throws Exception {
		final A a = add(type, false);

		if(num > 0) {
			a.setName(a.getName() + " " + Integer.toString(num));
		}

		a.setCurrency(getRandomEntity(Currency.class));

		a.setPaymentInfo(getRandomEntity(PaymentInfo.class));

		// account addresses upto 5
		final int numAddresses = randomInt(2); // make 0 or 1 for quicker debugging
		if(numAddresses > 0) {
			int ai = 0;
			final Set<AccountAddress> set = addN(AccountAddress.class, true, numAddresses);
			for(final AccountAddress aa : set) {
				aa.setAddress(getNthEntity(Address.class, ++ai));
				aa.setType(EnumUtil.fromOrdinal(AddressType.class, randomInt(AddressType.values().length)));
				a.addAccountAddress(aa);
			}
		}

		// create a random number of histories for this account upto 5
		final int numHistories = randomInt(6);
		if(numHistories > 0) {
			for(int i = 0; i < numHistories; i++) {
				final AccountHistory ah = add(AccountHistory.class, true);
				ah.setAccount(a);
				makeUnique(ah);
			}
		}

		// create a product set for this account upto 5
		final int numProducts = randomInt(6);
		if(numProducts > 0) {
			for(int i = 0; i < numProducts; i++) {
				final ProductInventory pi = add(ProductInventory.class, true);
				final ProductGeneral pg = generateEntity(ProductGeneral.class, true);
				pi.setProductGeneral(pg);
				pi.setAccount(a);
				makeUnique(pi);
			}
		}

		// create product categories for these account products upto 5
		final int numCategories = randomInt(6);
		if(numCategories > 0) {
			for(int i = 0; i < numCategories; i++) {
				final ProductCategory pc = add(ProductCategory.class, true);
				pc.setAccount(a);
				makeUnique(pc);
			}
		}

		// TODO bind products to categories

		// create some sales taxes upto 5
		final int numSalesTaxes = randomInt(6);
		if(numSalesTaxes > 0) {
			for(int i = 0; i < numSalesTaxes; i++) {
				final SalesTax st = add(SalesTax.class, true);
				st.setAccount(a);
				makeUnique(st);
			}
		}

		return a;
	}

	private void stubAccounts() throws Exception {
		// asp
		final Asp asp = stubAccount(Asp.class, 0);
		asp.setName(Asp.ASP_NAME);

		// isps
		final Isp[] isps = new Isp[numIsps];
		for(int i = 0; i < numIsps; i++) {
			final Isp isp = stubAccount(Isp.class, i + 1);
			isp.setParent(asp);
			isps[i] = isp;
		}

		// merchants
		final Merchant[] merchants = new Merchant[numMerchants];
		for(int i = 0; i < numMerchants; i++) {
			final Merchant m = stubAccount(Merchant.class, i + 1);
			final int ispIndex = i / numIsps;
			m.setParent(isps[ispIndex]);
			merchants[i] = m;
		}

		// customers
		final Customer[] customers = new Customer[numCustomers];
		for(int i = 0; i < numCustomers; i++) {
			final Customer c = stubAccount(Customer.class, i + 1);

			// create customer account binder entity
			final CustomerAccount ca = add(CustomerAccount.class, false);
			Account parent;
			ca.setCustomer(c);
			if(i < 2) {
				ca.setAccount(asp);
				parent = asp;
			}
			else if(i < (2 + 2 * numIsps)) {
				final int ispIndex = i / (2 * numIsps); // TODO verify the math
				final Isp isp = isps[ispIndex];
				parent = isp;
				ca.setAccount(isp);
				customers[i] = c;
			}
			else {
				final int merchantIndex = i / (2 * numMerchants); // TODO verify the math
				final Merchant merchant = merchants[merchantIndex];
				parent = merchant;
				ca.setAccount(merchant);
			}

			// create initial visitor record
			final Visitor v = add(Visitor.class, true);
			v.setAccount(parent);
			ca.setInitialVisitorRecord(v);
		}
	}

	private static InterfaceOption findInterfaceOption(String ioCode, Set<InterfaceOption> options) {
		for(final InterfaceOption io : options) {
			if(ioCode.equals(io.getCode())) {
				return io;
			}
		}
		return null;
	}

	private static InterfaceOptionParameterDefinition findParameterDefinition(String pdCode,
			Set<InterfaceOptionParameterDefinition> params) {
		for(final InterfaceOptionParameterDefinition pd : params) {
			if(pdCode.equals(pd.getCode())) {
				return pd;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private void stubInterfaces() throws Exception {
		final Set<Interface> intfs = (Set<Interface>) getNonNullEntitySet(Interface.class);
		intfs.addAll(addAll(InterfaceSingle.class));
		intfs.addAll(addAll(InterfaceSwitch.class));
		intfs.addAll(addAll(InterfaceMulti.class));

		final Set<InterfaceOption> ios = addAll(InterfaceOption.class);

		final Set<InterfaceOptionParameterDefinition> pds = addAll(InterfaceOptionParameterDefinition.class);

		for(final Interface intf : intfs) {
			if(Interface.CODE_CROSS_SELL.equals(intf.getCode())) {
				final InterfaceOption io = findInterfaceOption("crosssell-switch", ios);
				if(io != null) {
					for(int i = 1; i <= 3; i++) {
						final InterfaceOptionParameterDefinition pd = findParameterDefinition("crosssellP" + i, pds);
						if(pd != null) {
							io.addParameter(pd);
						}
					}
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
				final InterfaceOption io = findInterfaceOption("native_salestax", ios);
				if(io != null) {
					intf.addOption(io);
				}
			}
			else if(Interface.CODE_SHIP_METHOD.equals(intf.getCode())) {
				final InterfaceOption io = findInterfaceOption("native_shipmethod", ios);
				if(io != null) {
					intf.addOption(io);
				}
			}
		}
	}

	private void stubUsers() throws Exception {
		final User u = add(User.class, false);
		u.addAuthority(getRandomEntity(Authority.class));
		u.setAccount(getNthEntity(Asp.class, 1));
		u.setAddress(getRandomEntity(Address.class));
	}

}
