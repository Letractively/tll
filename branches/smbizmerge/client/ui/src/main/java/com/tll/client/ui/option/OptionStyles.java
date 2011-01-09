package com.tll.client.ui.option;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.ImportedWithPrefix;

/**
 * @author jpk
 */
public class OptionStyles {

	@ImportedWithPrefix("tll-option")
	public interface IOptionsCss extends CssResource {
		
		String options();
		
		String option();
		
		String active();
	}
	
	public interface IResources extends ClientBundle {
		
		@Source("options.css")
		//@NotStrict
		IOptionsCss css();
	}
	
	private static IResources resources;
	
	static {
		resources = GWT.create(IResources.class);
		resources.css().ensureInjected();
	}
	
	public static IResources getResources() {
		return resources;
	}
	
	public static IOptionsCss getOptions() {
		return resources.css();
	}
}
