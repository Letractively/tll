/**
 * The Logic Lab
 * @author jpk Nov 15, 2007
 */
package com.tll.model.mock;

import java.util.Set;

import com.google.inject.Inject;
import com.tll.SystemError;
import com.tll.mock.model.AbstractEntityGraphBuilder;
import com.tll.mock.model.MockEntityFactory;
import com.tll.model.Account;
import com.tll.model.AccountAddress;
import com.tll.model.AccountHistory;
import com.tll.model.Address;
import com.tll.model.AddressType;
import com.tll.model.AppProperty;
import com.tll.model.Asp;
import com.tll.model.Authority;
import com.tll.model.Currency;
import com.tll.model.Customer;
import com.tll.model.CustomerAccount;
import com.tll.model.Interface;
import com.tll.model.InterfaceMulti;
import com.tll.model.InterfaceOption;
import com.tll.model.InterfaceOptionParameterDefinition;
import com.tll.model.InterfaceSingle;
import com.tll.model.InterfaceSwitch;
import com.tll.model.Isp;
import com.tll.model.Merchant;
import com.tll.model.PaymentInfo;
import com.tll.model.ProductCategory;
import com.tll.model.ProductGeneral;
import com.tll.model.ProductInventory;
import com.tll.model.SalesTax;
import com.tll.model.User;
import com.tll.model.Visitor;
import com.tll.util.EnumUtil;

/**
 * SmbizEntityGraphBuilder - Builds an {@link SmbizEntityGraphBuilder} instance.
 * @author jpk
 */
public final class SmbizEntityGraphBuilder extends AbstractEntityGraphBuilder {

	private static final int numIsps = 3;
	private static final int numMerchants = numIsps * numIsps;
	private static final int numCustomers = 2 + numIsps * 2 + numMerchants * 2;

	/**
	 * Constructor
	 * @param mep
	 */
	@Inject
	public SmbizEntityGraphBuilder(MockEntityFactory mep) {
		super(mep);
	}

	@Override
	protected void stub() {
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
		A a = add(type, false);

		if(num > 0) {
			a.setName(a.getName() + " " + Integer.toString(num));
		}

		a.setCurrency(getRandomEntity(Currency.class));

		a.setPaymentInfo(getRandomEntity(PaymentInfo.class));

		// account addresses upto 5
		int numAddresses = randomInt(6);
		if(numAddresses > 0) {
			int ai = 0;
			Set<AccountAddress> set = addN(AccountAddress.class, true, numAddresses);
			for(AccountAddress aa : set) {
				aa.setAccount(a);
				aa.setAddress(getNthEntity(Address.class, ++ai));
				aa.setType(EnumUtil.fromOrdinal(AddressType.class, randomInt(AddressType.values().length)));
			}
		}
		
		// create a random number of histories for this account upto 5
		int numHistories = randomInt(6);
		if(numHistories > 0) {
			for(int i = 0; i < numHistories; i++) {
				AccountHistory ah = add(AccountHistory.class, true);
				ah.setAccount(a);
				makeUnique(ah);
			}
		}

		// create a product set for this account upto 5
		int numProducts = randomInt(6);
		if(numProducts > 0) {
			for(int i = 0; i < numProducts; i++) {
				ProductInventory pi = add(ProductInventory.class, true);
				ProductGeneral pg = generateEntity(ProductGeneral.class, true);
				pi.setProductGeneral(pg);
				pi.setAccount(a);
				makeUnique(pi);
			}
		}

		// create product categories for these account products upto 5
		int numCategories = randomInt(6);
		if(numCategories > 0) {
			for(int i = 0; i < numCategories; i++) {
				ProductCategory pc = add(ProductCategory.class, true);
				pc.setAccount(a);
				makeUnique(pc);
			}
		}

		// TODO bind products to categories

		// create some sales taxes upto 5
		int numSalesTaxes = randomInt(6);
		if(numSalesTaxes > 0) {
			for(int i = 0; i < numSalesTaxes; i++) {
				SalesTax st = add(SalesTax.class, true);
				st.setAccount(a);
				makeUnique(st);
			}
		}

		return a;
	}

	private void stubAccounts() throws Exception {
		// asp
		Asp asp = stubAccount(Asp.class, 0);
		asp.setName(Asp.ASP_NAME);

		// isps
		Isp[] isps = new Isp[numIsps];
		for(int i = 0; i < numIsps; i++) {
			Isp isp = stubAccount(Isp.class, i + 1);
			isp.setParent(asp);
			isps[i] = isp;
		}

		// merchants
		Merchant[] merchants = new Merchant[numMerchants];
		for(int i = 0; i < numMerchants; i++) {
			Merchant m = stubAccount(Merchant.class, i + 1);
			int ispIndex = i / numIsps;
			m.setParent(isps[ispIndex]);
			merchants[i] = m;
		}

		// customers
		Customer[] customers = new Customer[numCustomers];
		for(int i = 0; i < numCustomers; i++) {
			Customer c = stubAccount(Customer.class, i + 1);

			// create customer account binder entity
			CustomerAccount ca = add(CustomerAccount.class, false);
			Account parent;
			ca.setCustomer(c);
			if(i < 2) {
				ca.setAccount(asp);
				parent = asp;
			}
			else if(i < (2 + 2 * numIsps)) {
				int ispIndex = i / (2 * numIsps); // TODO verify the math
				Isp isp = isps[ispIndex];
				parent = isp;
				ca.setAccount(isp);
				customers[i] = c;
			}
			else {
				int merchantIndex = i / (2 * numMerchants); // TODO verify the math
				Merchant merchant = merchants[merchantIndex];
				parent = merchant;
				ca.setAccount(merchant);
			}
			
			// create initial visitor record
			Visitor v = add(Visitor.class, true);
			v.setAccount(parent);
			ca.setInitialVisitorRecord(v);
		}
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

	@SuppressWarnings("unchecked")
	private void stubInterfaces() throws Exception {
		Set<Interface> intfs = (Set<Interface>) getNonNullEntitySet(Interface.class);
		intfs.addAll(addAll(InterfaceSingle.class));
		intfs.addAll(addAll(InterfaceSwitch.class));
		intfs.addAll(addAll(InterfaceMulti.class));

		Set<InterfaceOption> ios = addAll(InterfaceOption.class);

		Set<InterfaceOptionParameterDefinition> pds = addAll(InterfaceOptionParameterDefinition.class);

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
	}

	private void stubUsers() throws Exception {
		User u = add(User.class, false);
		u.addAuthority(getRandomEntity(Authority.class));
		u.setAccount(getNthEntity(Asp.class, 1));
		u.setAddress(getRandomEntity(Address.class));
	}

}
