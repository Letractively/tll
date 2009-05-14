/**
 * The Logic Lab
 * @author jpk
 * @since May 13, 2009
 */
package com.tll.client.mvc.view.intf;

import com.tll.client.mvc.view.AbstractDynamicViewInitializer;
import com.tll.common.model.ModelKey;


/**
 * IntfOptAccViewInitializer
 * @author jpk
 */
public class IntfOptAccViewInitializer extends AbstractDynamicViewInitializer {

	private final ModelKey accountRef;

	/**
	 * Constructor
	 * @param accountRef
	 */
	public IntfOptAccViewInitializer(ModelKey accountRef) {
		super(IntfOptAccView.klas);
		this.accountRef = accountRef;
	}

	@Override
	protected int getViewId() {
		return accountRef.hashCode();
	}

}
