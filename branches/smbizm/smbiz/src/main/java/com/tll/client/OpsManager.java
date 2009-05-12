/**
 * The Logic Lab
 * @author jpk
 * @since May 5, 2009
 */
package com.tll.client;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.gwt.user.client.Window;
import com.tll.client.mvc.ViewManager;
import com.tll.client.mvc.view.EditViewInitializer;
import com.tll.client.mvc.view.IViewInitializer;
import com.tll.client.mvc.view.ShowViewRequest;
import com.tll.client.mvc.view.StaticViewInitializer;
import com.tll.client.mvc.view.account.AccountEditView;
import com.tll.client.mvc.view.account.CustomerListingViewInitializer;
import com.tll.client.mvc.view.account.IspListingView;
import com.tll.client.mvc.view.account.MerchantListingViewInitializer;
import com.tll.client.mvc.view.intf.InterfacesView;
import com.tll.client.ui.option.IOptionHandler;
import com.tll.client.ui.option.Option;
import com.tll.client.ui.option.OptionEvent;
import com.tll.common.model.SmbizEntityType;

/**
 * OpsManager - Encapsulates logic that determines the available ops based on
 * the state of the admin context and is responsible for providing associated
 * {@link IViewInitializer} instances.
 * @author jpk
 */
public final class OpsManager implements IOptionHandler {

	private static final OpsManager instance = new OpsManager();

	public static final OpsManager get() {
		return instance;
	}

	public static final Option OP_SITE_SUMMARY = new Option("Site Summary");

	// account related
	public static final Option OP_ACCOUNT_DETAIL = new Option("Account Detail");
	public static final Option OP_CREATE_ACCOUNT = new Option("Create Account");
	public static final Option OP_CREATE_ISP = new Option("Create ISP");
	public static final Option OP_CREATE_MERCHANT = new Option("Create Merchant");
	public static final Option OP_CREATE_CUSTOMER = new Option("Create Customer");
	public static final Option OP_ISPS = new Option("ISPs");
	public static final Option OP_MERCHANTS = new Option("Merchants");
	public static final Option OP_CUSTOMERS = new Option("Customers");

	// interface related
	public static final Option OP_INTERFACES = new Option("Interfaces");
	public static final Option OP_INTERFACES_ACCOUNT = new Option("Account Interfaces");

	// product related
	public static final Option OP_PRODUCTS = new Option("Products");
	public static final Option OP_CATEGORIES = new Option("Categories");

	// order related
	public static final Option OP_ORDERS = new Option("Orders");

	// merchant related
	public static final Option OP_SALES_TAX = new Option("Sales Taxes");
	public static final Option OP_SHIPPING = new Option("Shipping");
	public static final Option OP_VISITORS = new Option("Visitors");

	// asp management related
	public static final Option OP_CURRENCIES = new Option("Currencies");
	public static final Option OP_APP_PROPERTIES = new Option("App Props");

	//public static final Option OP_ = new Option("");

	private static Option[] CUAT_ASP = new Option[] {
		OP_SITE_SUMMARY,
		OP_ACCOUNT_DETAIL,
		OP_CREATE_ACCOUNT,
		OP_ISPS,
		OP_INTERFACES,
	};

	private static Option[] CUAT_ISP = new Option[] {
		OP_ACCOUNT_DETAIL,
		OP_CREATE_MERCHANT,
		OP_CREATE_CUSTOMER,
		OP_MERCHANTS,
	};

	private static Option[] CUAT_MERCHANT = new Option[] {
		OP_ACCOUNT_DETAIL,
		OP_CREATE_CUSTOMER,
		OP_CUSTOMERS,
	};

	private static Option[] CUAT_CUSOMER = new Option[] {
		OP_ACCOUNT_DETAIL,
	};

	/**
	 * Constructor
	 */
	private OpsManager() {
	}

	/**
	 * Creates a fresh {@link IViewInitializer} based on the given params.
	 * @param ac the admin context
	 * @param optionText the desired view option text
	 * @return Newly created {@link IViewInitializer}.
	 */
	private IViewInitializer resolveViewInitializer(AdminContext ac, String optionText) {
		final SmbizEntityType crntUserAccountType = SmbizEntityType.convert(ac.getUserAccount().getEntityType());

		if(OP_ACCOUNT_DETAIL.getText().equals(optionText)) {
			switch(crntUserAccountType) {
				case ASP:
				case ISP:
				case MERCHANT:
				case CUSTOMER:
					return new EditViewInitializer(AccountEditView.klas, ac.getUserAccount());
			}
		}

		if(OP_ISPS.getText().equals(optionText)) {
			return new StaticViewInitializer(IspListingView.klas);
		}
		if(OP_MERCHANTS.getText().equals(optionText)) {
			assert crntUserAccountType == SmbizEntityType.ISP;
			return new MerchantListingViewInitializer(ac.getUserAccount().getKey());
		}
		if(OP_CUSTOMERS.getText().equals(optionText)) {
			assert crntUserAccountType == SmbizEntityType.MERCHANT;
			return new CustomerListingViewInitializer(ac.getUserAccount().getKey(), null);
		}

		if(OP_INTERFACES.getText().equals(optionText)) {
			assert crntUserAccountType == SmbizEntityType.ASP;
			return new StaticViewInitializer(InterfacesView.klas);
		}
		if(OP_INTERFACES_ACCOUNT.getText().equals(optionText)) {
			return null;
		}

		if(OP_PRODUCTS.getText().equals(optionText)) {

		}
		if(OP_CATEGORIES.getText().equals(optionText)) {

		}

		if(OP_ORDERS.getText().equals(optionText)) {

		}

		if(OP_SALES_TAX.getText().equals(optionText)) {

		}
		if(OP_SHIPPING.getText().equals(optionText)) {

		}

		if(OP_SITE_SUMMARY.getText().equals(optionText)) {

		}
		if(OP_CURRENCIES.getText().equals(optionText)) {

		}
		if(OP_APP_PROPERTIES.getText().equals(optionText)) {

		}

		// default - signifying not implemented
		return null;
	}

	/**
	 * @param ac the active admin context
	 * @return The available ops
	 */
	public static Option[] get(AdminContext ac) {
		final SmbizEntityType crntUserAccountType = SmbizEntityType.convert(ac.getUserAccount().getEntityType());
		final SmbizEntityType crntAccountType = SmbizEntityType.convert(ac.getAccount().getEntityType());

		final ArrayList<Option> options = new ArrayList<Option>();

		// add current user account related
		switch(crntUserAccountType) {
			case ASP:
				options.addAll(Arrays.asList(CUAT_ASP));
				// add current account related
				switch(crntAccountType) {
					case ASP:
					case ISP:
					case MERCHANT:
					case CUSTOMER:
				}
				break;
			case ISP:
				options.addAll(Arrays.asList(CUAT_ISP));
				// add current account related
				switch(crntAccountType) {
					case ASP:
					case ISP:
					case MERCHANT:
					case CUSTOMER:
				}
				break;
			case MERCHANT:
				options.addAll(Arrays.asList(CUAT_MERCHANT));
				// add current account related
				switch(crntAccountType) {
					case ASP:
					case ISP:
					case MERCHANT:
					case CUSTOMER:
				}
				break;
			case CUSTOMER:
				options.addAll(Arrays.asList(CUAT_CUSOMER));
				// add current account related
				switch(crntAccountType) {
					case ASP:
					case ISP:
					case MERCHANT:
					case CUSTOMER:
				}
				break;
			default:
				throw new IllegalStateException("Unknown account type");
		}

		return options.toArray(new Option[options.size()]);
	}

	@Override
	public void onOptionEvent(OptionEvent event) {
		final String optionText = event.getOptionText();
		if(event.getOptionEventType() == OptionEvent.EventType.SELECTED) {
			final IViewInitializer vi = resolveViewInitializer(SmbizAdmin.getAdminContextCmd().getAdminContext(), optionText);
			if(vi == null) {
				Window.alert("The view: '" + optionText + "' is currently not implemented.");
				return;
			}
			ViewManager.get().dispatch(new ShowViewRequest(vi));
		}
	}
}
