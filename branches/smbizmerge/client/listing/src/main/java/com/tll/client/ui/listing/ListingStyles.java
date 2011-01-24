package com.tll.client.ui.listing;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.Import;
import com.google.gwt.resources.client.CssResource.ImportedWithPrefix;
import com.google.gwt.resources.client.ImageResource;

/**
 * Listing nav bar styles/resources.
 */
public class ListingStyles {
	
	@ImportedWithPrefix("tll-tableview")
	public interface IListingCss extends CssResource {
	
		String glass();
		
		String tableView();
		
		String portal();
		
		String caption();
		
		String nodata();
		
		String table();
		
		String head();
		
		String sort();
		
		String even();
		
		String odd();
		
		String crnt();
		
		String actv();
		
		String added();
		
		String updated();
		
		String deleted();
		
		String countCol();
	}
	
	public interface IListingResources extends ClientBundle {
		
		@Source("sort_asc.gif")
		ImageResource sortAsc();
		
		@Source("sort_desc.gif")
		ImageResource sortDesc();

		@Source("tableview.css")
		@Import(ListingNavStyles.IListingNavCss.class)
		IListingCss css();
	}
	
	private static final IListingResources resources;
	
	static {
		resources = GWT.create(IListingResources.class);
		resources.css().ensureInjected();
	}
	
	public static IListingResources resources() {
		return resources;
	}
	
	public static IListingCss css() {
		return resources.css();
	}
}