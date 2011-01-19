/**
 * The Logic Lab
 * @author jpk
 */
package com.tll.client;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

/**
 * @author jpk
 */
public interface Resources extends ClientBundle {

	@Source("smbiz.css")
	Style style();
	
	public interface Style extends CssResource {
		
	}
}
