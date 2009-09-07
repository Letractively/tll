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
import com.tll.client.ui.field.intf.AccountInterfaceOptionsPanel;
import com.tll.client.ui.msg.GlobalMsgPanel;
import com.tll.common.data.AuxDataRequest;
import com.tll.common.model.ModelKey;
import com.tll.common.model.SmbizEntityType;
import com.tll.common.search.AccountInterfaceDataSearch;

/**
 * IntfOptAccView - Manages account to interface option bindings.
 * @author jpk
 */
public class IntfOptAccView extends AbstractRpcAndModelAwareView<IntfOptAccViewInitializer> {

	public static final Class klas = new Class();

	public static final class Class extends ViewClass {

		@Override
		public String getName() {
			return "IntfOptAccView";
		}

		@Override
		public IntfOptAccView newView() {
			return new IntfOptAccView();
		}
	} // Class

	private static final AuxDataRequest auxDataRequest = new AuxDataRequest();

	static {
		auxDataRequest.requestEntityPrototype(SmbizEntityType.INTERFACE_SWITCH);
		auxDataRequest.requestEntityPrototype(SmbizEntityType.INTERFACE_SINGLE);
		auxDataRequest.requestEntityPrototype(SmbizEntityType.INTERFACE_MULTI);
		auxDataRequest.requestEntityPrototype(SmbizEntityType.INTERFACE_OPTION);
		auxDataRequest.requestEntityPrototype(SmbizEntityType.INTERFACE_OPTION_PARAMETER_DEFINITION);
		auxDataRequest.requestEntityPrototype(SmbizEntityType.INTERFACE_OPTION_ACCOUNT);
	}

	private final GlobalMsgPanel gmp = new GlobalMsgPanel();

	private InterfaceStack intfStack;

	private ModelKey accountKey;

	/**
	 * Constructor
	 */
	public IntfOptAccView() {
		super();
		addWidget(gmp);
	}

	public void setAccountKey(ModelKey accountKey) {
		this.accountKey = accountKey;
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
	protected void doInitialization(IntfOptAccViewInitializer initializer) {
		intfStack =
			new InterfaceStack(gmp, new RpcUiHandler(getViewContainerRef()), auxDataRequest,
					new InterfaceStack.IFieldPanelResolver() {

				@Override
				public FieldPanel<?> resolveFieldPanel(ModelKey intfKey) {
					return new AccountInterfaceOptionsPanel();
				}
			}, new InterfaceStack.IFieldPanelDataLoader() {

				@SuppressWarnings("synthetic-access")
				@Override
				public IRpcCommand load(ModelKey intfKey, AuxDataRequest adr) {
					final CrudCommand c = new CrudCommand();
					c.load(new AccountInterfaceDataSearch(accountKey.getId(), intfKey.getId()), adr);
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
