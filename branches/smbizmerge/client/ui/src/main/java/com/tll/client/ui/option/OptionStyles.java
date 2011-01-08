package com.tll.client.ui.option;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;

/**
 * @author jpk
 */
public class OptionStyles {

	public interface Options extends CssResource {
		
		String option();
		
		String active();
	}
	
	public interface Resources extends ClientBundle {
		
		@Source("options.css")
		@NotStrict
		Options css();
	}
	
	private static Resources resources;
	
	static {
		resources = GWT.create(Resources.class);
		resources.css().ensureInjected();
	}
	
	public static Resources getResources() {
		return resources;
	}
	
	public static Options getOptions() {
		return resources.css();
	}
}
