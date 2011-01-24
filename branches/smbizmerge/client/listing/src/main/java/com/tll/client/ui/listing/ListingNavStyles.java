package com.tll.client.ui.listing;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.ImportedWithPrefix;
import com.google.gwt.resources.client.ImageResource;
import com.tll.client.ui.toolbar.ToolbarStyles;

/**
 * Listing nav bar styles/resources.
 */
public class ListingNavStyles {
	
	@ImportedWithPrefix("tll-tvnav")
	public interface IListingNavCss extends CssResource {
		
		/**
		 * Applied to root container (toolbar).
		 */
		String tvnav();
		
		/**
		 * Style for a listing's page container.
		 */
		String page();
		
		/**
		 * Applied to summary text.
		 */
		String smry();
		
		/**
		 * Applied to goto page text box.
		 */
		String tbPage();
	}
	
	public interface IListingNavResources extends ToolbarStyles.IResources {
		
		@Source(value = "refresh.gif")
		ImageResource refresh();

		@Source(value = "page-first.gif")
		ImageResource page_first();

		@Source(value = "page-first-disabled.gif")
		ImageResource page_first_disabled();

		@Source(value = "page-last.gif")
		ImageResource page_last();

		@Source(value = "page-last-disabled.gif")
		ImageResource page_last_disabled();

		@Source(value = "page-next.gif")
		ImageResource page_next();

		@Source(value = "page-next-disabled.gif")
		ImageResource page_next_disabled();

		@Source(value = "page-prev.gif")
		ImageResource page_prev();

		@Source(value = "page-prev-disabled.gif")
		ImageResource page_prev_disabled();
		
		@Source("tvnav.css")
		IListingNavCss navBarCss();
	}
	
	private static final IListingNavResources resources;
	
	static {
		resources = GWT.create(IListingNavResources.class);
		resources.navBarCss().ensureInjected();
	}
	
	public static IListingNavResources resources() {
		return resources;
	}
	
	public static IListingNavCss css() {
		return resources.navBarCss();
	}

}