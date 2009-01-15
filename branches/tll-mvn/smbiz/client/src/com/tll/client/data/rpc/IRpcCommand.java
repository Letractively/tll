/**
 * The Logic Lab
 * @author jpk Dec 5, 2007
 */
package com.tll.client.data.rpc;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.tll.client.data.Payload;

/**
 * IRpcCommand - Definition for an RPC command callback.
 * @author jpk
 * @param <P> the payload type
 */
public interface IRpcCommand<P extends Payload> extends Command, AsyncCallback<P> {

}
