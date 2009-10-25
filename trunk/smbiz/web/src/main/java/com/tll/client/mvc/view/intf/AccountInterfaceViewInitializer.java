/**
 * The Logic Lab
 * @author jpk
 * @since May 13, 2009
 */
package com.tll.client.mvc.view.intf;

import com.tll.client.mvc.view.AbstractDynamicViewInitializer;
import com.tll.common.model.ModelKey;


/**
 * AccountInterfaceViewInitializer
 * @author jpk
 */
public class AccountInterfaceViewInitializer extends AbstractDynamicViewInitializer {

	private final ModelKey accountRef;

	/**
	 * Constructor
	 * @param accountRef
	 */
	public AccountInterfaceViewInitializer(ModelKey accountRef) {
		super(AccountInterfaceView.klas);
		this.accountRef = accountRef;
	}

	@Override
	protected int getViewId() {
		return accountRef.hashCode();
	}
	
	public ModelKey getAccountRef() {
		return accountRef;
	}
}
