package com.tll.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

/**
 * @author jpk
 */
public class LoginStyles {

	public interface Css extends CssResource {

	}

	public interface Resources extends ClientBundle {

		@Source("LoginConstants.properties")
		LoginConstants constants();

		Css css();
	}

	private static Resources resources;

	static {
		resources = GWT.create(Resources.class);
		resources.css().ensureInjected();
	}

	public static Resources resources() {
		return resources;
	}

	public static Css css() {
		return resources.css();
	}

	public static LoginConstants constants() {
		return resources.constants(); 
	}
}
