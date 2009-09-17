/**
 * The Logic Lab
 * @author jpk
 * @since May 13, 2009
 */
package com.tll.client.mvc.view.intf;

import com.tll.client.data.rpc.CrudCommand;
import com.tll.client.data.rpc.IRpcCommand;
import com.tll.client.model.ModelChangeEvent;
import com.tll.client.mvc.view.AbstractRpcAndModelAwareView;
import com.tll.client.mvc.view.ViewClass;
import com.tll.client.ui.RpcUiHandler;
import com.tll.client.ui.field.FieldPanel;
import com.tll.client.ui.field.intf.AccountMultiOptionInterfacePanel;
import com.tll.client.ui.field.intf.AccountSwitchInterfacePanel;
import com.tll.client.ui.msg.GlobalMsgPanel;
import com.tll.common.data.AuxDataRequest;
import com.tll.common.model.ModelKey;
import com.tll.common.model.SmbizEntityType;
import com.tll.common.search.AccountInterfaceDataSearch;

/**
 * AccountInterfaceView - Manages account to interface option bindings.
 * @author jpk
 */
public class AccountInterfaceView extends AbstractRpcAndModelAwareView<AccountInterfaceViewInitializer> {

	public static final Class klas = new Class();

	public static final class Class extends ViewClass {

		@Override
		public String getName() {
			return "AccountInterfaceView";
		}

		@Override
		public AccountInterfaceView newView() {
			return new AccountInterfaceView();
		}
	} // Class

	private static final AuxDataRequest auxDataRequest = new AuxDataRequest();

	static {
		auxDataRequest.requestEntityPrototype(SmbizEntityType.INTERFACE_SWITCH);
		auxDataRequest.requestEntityPrototype(SmbizEntityType.INTERFACE_SINGLE);
		auxDataRequest.requestEntityPrototype(SmbizEntityType.INTERFACE_MULTI);
		auxDataRequest.requestEntityPrototype(SmbizEntityType.INTERFACE_OPTION);
		auxDataRequest.requestEntityPrototype(SmbizEntityType.ACCOUNT_INTERFACE);
		auxDataRequest.requestEntityPrototype(SmbizEntityType.ACCOUNT_INTERFACE_OPTION);
		auxDataRequest.requestEntityPrototype(SmbizEntityType.ACCOUNT_INTERFACE_OPTION_PARAMETER);
	}

	private final GlobalMsgPanel gmp = new GlobalMsgPanel();

	private InterfaceStack intfStack;

	/**
	 * Constructor
	 */
	public AccountInterfaceView() {
		super();
		addWidget(gmp);
	}

	@Override
	protected ViewClass getViewClass() {
		return klas;
	}

	@Override
	public String getLongViewName() {
		return "Account Interface Options";
	}

	@Override
	protected String getViewStyle() {
		return "interfaces";
	}

	@Override
	protected void doInitialization(final AccountInterfaceViewInitializer initializer) {
		intfStack =
			new InterfaceStack(gmp, new RpcUiHandler(getViewContainerRef()), auxDataRequest,
					new InterfaceStack.IFieldPanelResolver() {

				@Override
				public FieldPanel<?> resolveFieldPanel(ModelKey intfKey) {
					final SmbizEntityType type = (SmbizEntityType) intfKey.getEntityType();
					switch(type) {
						case INTERFACE_SWITCH:
							return new AccountSwitchInterfacePanel();
						case INTERFACE_SINGLE:
							return new AccountMultiOptionInterfacePanel(true);
						case INTERFACE_MULTI:
							return new AccountMultiOptionInterfacePanel(false);
					}
					throw new IllegalArgumentException("Unhandled account interface type");
				}
			}, new InterfaceStack.IFieldPanelDataLoader() {

				@Override
				public IRpcCommand load(ModelKey intfKey, AuxDataRequest adr) {
					final CrudCommand c = new CrudCommand();
					c.load(new AccountInterfaceDataSearch(initializer.getAccountRef().getId(), intfKey.getId()), adr);
					return c;
				}
			}, new CrudCommand());
		addWidget(intfStack);
	}

	@Override
	protected void doRefresh() {
		intfStack.refreshData();
	}

	@Override
	protected void doDestroy() {
		intfStack.clearData();
	}

	@Override
	protected boolean shouldHandleModelChangeEvent(ModelChangeEvent event) {
		return false;
	}
}
