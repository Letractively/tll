/**
 * The Logic Lab
 * @author jpk
 * @since May 10, 2009
 */
package com.tll.client.mvc.view;

import com.google.gwt.event.shared.HandlerRegistration;
import com.tll.client.data.rpc.IHasRpcHandlers;
import com.tll.client.data.rpc.IRpcHandler;
import com.tll.client.data.rpc.RpcEvent;
import com.tll.client.ui.RpcUiHandler;

/**
 * AbstractRpcAndModelAwareView
 * @author jpk
 * @param <I> the view initializer type
 */
public abstract class AbstractRpcAndModelAwareView<I extends IViewInitializer> extends AbstractModelAwareView<I> implements IHasRpcHandlers {

	private HandlerRegistration rpcReg;

	@Override
	public final HandlerRegistration addRpcHandler(IRpcHandler handler) {
		return addHandler(handler, RpcEvent.TYPE);
	}

	@Override
	protected void loaded() {
		if(rpcReg != null) rpcReg.removeHandler();
		rpcReg = addRpcHandler(new RpcUiHandler(getViewContainerRef()));
	}

}
