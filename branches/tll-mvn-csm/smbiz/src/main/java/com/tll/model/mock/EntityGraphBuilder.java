/**
 * The Logic Lab
 * @author jpk Nov 15, 2007
 */
package com.tll.model.mock;

import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.lang.math.RandomUtils;

import com.tll.SystemError;
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
import com.tll.model.mock.AbstractEntityGraphBuilder;
import com.tll.model.mock.MockEntityFactory;
import com.tll.util.EnumUtil;

/**
 * EntityGraphBuilder - Builds an {@link EntityGraphBuilder} instance.
 * @author jpk
 */
public final class EntityGraphBuilder extends AbstractEntityGraphBuilder {

	private static final int numIsps = 3;
	private static final int numMerchants = numIsps * numIsps;
	private static final int numCustomers = 2 + numIsps * 2 + numMerchants * 2;

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

			stubInterfaces();

			stubUsers();
		}
		catch(Exception e) {
			throw new SystemError("Unable to stub entity graph: " + e.getMessage());
		}
	}

	private void stubRudimentaryEntities() throws Exception {
		generateAndAdd(Currency.class, 1, false);

		// app properties
		AppProperty ap;
		ap = generateAndAdd(AppProperty.class, 1, false);
		ap.setName("locale");
		ap.setValue("US");
		ap = generateAndAdd(AppProperty.class, 1, false);
		ap.setName("default.iso4217");
		ap.setValue("usd");
		ap = generateAndAdd(AppProperty.class, 1, false);
		ap.setName("default.country");
		ap.setValue("usa");

		// addresses
		generateAndAddN(Address.class, 1, 10);

		// payment infos
		generateAndAdd(PaymentInfo.class, 1, false);

		// user authorities
		generateAndAddAll(Authority.class, 1);
	}

	private <A extends Account> A stubAccount(Class<A> type, int num) throws Exception {
		A a = generateAndAdd(type, 1, false);

		if(num > 0) {
			a.setName(a.getName() + " " + Integer.toString(num));
		}

		a.setCurrency(getNthEntity(Currency.class, 1));

		a.setPaymentInfo(getNthEntity(PaymentInfo.class, 1));

		// account addresses upto 5
		int numAddresses = RandomUtils.nextInt(5);
		if(numAddresses > 0) {
			Set<AccountAddress> set = generateAndAddN(AccountAddress.class, 1, numAddresses);
			for(AccountAddress aa : set) {
				aa.setAccount(a);
				aa.setAddress(getRandomExisting(Address.class));
				aa.setType(EnumUtil.fromOrdinal(AddressType.class, RandomUtils.nextInt(AddressType.values().length)));
			}
		}
		
		// create a random number of histories for this account upto 5
		int numHistories = RandomUtils.nextInt(5);
		if(numHistories > 0) {
			for(int i = 0; i < numHistories; i++) {
				AccountHistory ah = generateAndAdd(AccountHistory.class, 1, true);
				ah.setAccount(a);
			}
		}

		// create a product set for this account
		int numProducts = RandomUtils.nextInt(5);
		if(numProducts > 0) {
			for(int i = 0; i < numProducts; i++) {
				ProductInventory pi = generateAndAdd(ProductInventory.class, 1, true);
				ProductGeneral pg = generateEntity(ProductGeneral.class, 1, true);
				pi.setProductGeneral(pg);
				pi.setAccount(a);
			}
		}

		// create product categories for these account products
		int numCategories = RandomUtils.nextInt(5);
		if(numCategories > 0) {
			for(int i = 0; i < numCategories; i++) {
				ProductCategory pc = generateAndAdd(ProductCategory.class, 1, true);
				pc.setAccount(a);
			}
		}

		// TODO bind products to categories

		// create some sales taxes
		int numSalesTaxes = RandomUtils.nextInt(5);
		if(numSalesTaxes > 0) {
			for(int i = 0; i < numSalesTaxes; i++) {
				SalesTax st = generateAndAdd(SalesTax.class, 1, true);
				st.setAccount(a);
			}
		}

		return a;
	}

	private void stubAccounts() throws Exception {
		// asp
		Asp asp = stubAccount(Asp.class, 0);
		asp.setName(Asp.ASP_NAME);

		// isps
		Set<Isp> isps = new LinkedHashSet<Isp>();
		for(int i = 0; i < numIsps; i++) {
			Isp isp = stubAccount(Isp.class, i + 1);
			isp.setParent(asp);
		}
		Isp[] arrIsp = isps.toArray(new Isp[isps.size()]);

		// merchants
		for(int i = 0; i < numMerchants; i++) {
			Merchant m = stubAccount(Merchant.class, i + 1);
			int ispIndex = i / numIsps;
			m.setParent(arrIsp[ispIndex]);
		}
		Set<Merchant> merchants = getNonNullEntitySet(Merchant.class);
		assert merchants != null;
		Merchant[] arrMerchant = merchants.toArray(new Merchant[merchants.size()]);

		// customers
		Set<Customer> customers = getNonNullEntitySet(Customer.class);
		for(int i = 0; i < numCustomers; i++) {
			Customer c = stubAccount(Customer.class, i + 1);

			// customer account
			CustomerAccount ca = generateAndAdd(CustomerAccount.class, 1, false);
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

			getNonNullEntitySet(CustomerAccount.class).add(ca);
		}

		// merge the accounts under the common base type in the map (required for
		// proper dao operation)
		Set<Account> accounts = getNonNullEntitySet(Account.class);
		accounts.add(asp);
		accounts.addAll(isps);
		accounts.addAll(merchants);
		accounts.addAll(customers);
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
		Set<Interface> intfs = getNonNullEntitySet(Interface.class);
		intfs.addAll(generateAndAddAll(InterfaceSingle.class, 1));
		intfs.addAll(generateAndAddAll(InterfaceSwitch.class, 1));
		intfs.addAll(generateAndAddAll(InterfaceMulti.class, 1));

		Set<InterfaceOption> ios = generateAndAddAll(InterfaceOption.class, 1);

		Set<InterfaceOptionParameterDefinition> pds = generateAndAddAll(InterfaceOptionParameterDefinition.class, 1);

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
		User u = generateAndAdd(User.class, 1, true);
		u.addAuthority(getRandomExisting(Authority.class));
		u.setAccount(getNthEntity(Asp.class, 1));
		u.setAddress(getRandomExisting(Address.class));
	}

}
