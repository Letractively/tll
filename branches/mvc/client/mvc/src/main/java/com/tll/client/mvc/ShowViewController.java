/**
 * The Logic Lab
 * @author jpk Jan 19, 2008
 */
package com.tll.client.mvc;

import com.tll.client.mvc.view.IViewRequest;
import com.tll.client.mvc.view.ShowViewRequest;

/**
 * ShowViewController
 * @author jpk
 */
class ShowViewController extends AbstractController {

	public boolean canHandle(IViewRequest request) {
		return request instanceof ShowViewRequest;
	}

}
