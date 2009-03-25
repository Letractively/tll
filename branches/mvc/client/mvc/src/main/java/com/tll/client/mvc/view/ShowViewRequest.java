/**
 * The Logic Lab
 * @author jpk
 * @since Mar 24, 2009
 */
package com.tll.client.mvc.view;


/**
 * ShowViewRequest
 * @author jpk
 */
public abstract class ShowViewRequest extends AbstractViewRequest {

	@Override
	public final boolean addHistory() {
		return true;
	}
}
