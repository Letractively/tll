/**
 * The Logic Lab
 * @author jpk Jan 19, 2008
 */
package com.tll.client.mvc;

import com.tll.client.mvc.view.ShowViewRequest;
import com.tll.client.mvc.view.ViewRequestEvent;

/**
 * ShowViewController
 * @author jpk
 */
class ShowViewController extends AbstractController {

	public boolean canHandle(ViewRequestEvent request) {
		return request instanceof ShowViewRequest;
	}

}
