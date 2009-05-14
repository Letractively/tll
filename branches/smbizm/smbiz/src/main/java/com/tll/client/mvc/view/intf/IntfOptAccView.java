/**
 * The Logic Lab
 * @author jpk
 * @since May 13, 2009
 */
package com.tll.client.mvc.view.intf;

import com.tll.client.model.ModelChangeEvent;
import com.tll.client.mvc.view.AbstractRpcAndModelAwareView;
import com.tll.client.mvc.view.ViewClass;
import com.tll.client.ui.field.FieldPanel;
import com.tll.client.ui.msg.GlobalMsgPanel;
import com.tll.common.data.AuxDataRequest;
import com.tll.common.model.IEntityType;
import com.tll.common.model.SmbizEntityType;

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

	private final InterfaceStack intfStack;

	/**
	 * Constructor
	 */
	public IntfOptAccView() {
		super();
		intfStack = new InterfaceStack(gmp, this, auxDataRequest, new InterfaceStack.IFieldPanelResolver() {

			@Override
			public FieldPanel<?> resolveFieldPanel(IEntityType type) {
				return null;
			}
		});
		addWidget(gmp);
		addWidget(intfStack);
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
	protected boolean shouldHandleModelChangeEvent(ModelChangeEvent event) {
		return false;
	}

	@Override
	protected void doInitialization(IntfOptAccViewInitializer initializer) {
	}

	@Override
	protected void doDestroy() {
	}
}
