package com.tll.client.ui.toolbar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.ImportedWithPrefix;
import com.google.gwt.resources.client.ImageResource;

/**
 * @author jpk
 */
public class ToolbarStyles {

	@ImportedWithPrefix("tll-toolbar")
	public interface IToolbar extends CssResource {
		
		String toolbar();
		
		String separator();
	}
	
	public interface IResources extends ClientBundle {
		
		@Source(value = "split.gif")
		ImageResource split();

		@Source("toolbar.css")
		IToolbar css();
	}
	
	private static IResources resources;
	
	static {
		resources = GWT.create(IResources.class);
		resources.css().ensureInjected();
	}
	
	public static IResources resources() {
		return resources;
	}
	
	public static IToolbar css() {
		return resources.css();
	}
}
