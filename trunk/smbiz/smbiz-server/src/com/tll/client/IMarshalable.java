/**
 * The Logic Lab
 * @author jpk
 * Aug 25, 2007
 */
package com.tll.client;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.tll.util.IDescriptorProvider;

/**
 * IMarshalable - Tag interface indicating RPC marshal-ability.
 * @author jpk
 */
public interface IMarshalable extends IsSerializable, IDescriptorProvider {
}
