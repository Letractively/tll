package com.tll.client.ui.toolbar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

/**
 * @author jpk
 */
public class ToolbarStyles {

	public interface Toolbar extends CssResource {
		
		String toolbar();
		
		String separator();
	}
	
	public interface Resources extends ClientBundle {
		
		@Source("toolbar.css")
		Toolbar css();

		@Source(value = "split.gif")
		ImageResource split();
	}
	
	private static Resources resources;
	
	static {
		resources = GWT.create(Resources.class);
		resources.css().ensureInjected();
	}
	
	public static Resources resources() {
		return resources;
	}
	
	public static Toolbar css() {
		return resources.css();
	}
}
