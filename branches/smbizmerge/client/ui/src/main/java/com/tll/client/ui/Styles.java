package com.tll.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.ImportedWithPrefix;
import com.google.gwt.resources.client.CssResource.NotStrict;

/**
 * @author jpk
 */
public class Styles {

	/*
	public interface Reset extends CssResource {
		
	}
	
	public interface Base extends CssResource {
		
	}
	
	public interface Hnav extends CssResource {
		
		String hnav();
		
	}
	*/
	
	@ImportedWithPrefix("tll")
	public interface TllWidget extends CssResource {

		String imageContainer();
		
		String simpleHyperlink();
		
		String glassPanel();
	}

	public interface Resources extends ClientBundle {
	
		/*
		@Source("base.css")
		@NotStrict
		Reset baseCss();
		
		@Source("hnav.css")
		@NotStrict
		Reset hnavCss();
		*/
		
		@Source("widget-tll.css")
		@NotStrict
		TllWidget tllWidgetCss();

// NOTE: since this image is animated, it doesn't work in GWT's ImageBundle
//		@Source("throbber.gif")
//		ImageResource throbber();
	}
	
	private static Resources resources;

  static {
    resources = GWT.create(Resources.class);
    resources.tllWidgetCss().ensureInjected();
  }

  public static TllWidget tllWidgetStyle() {
    return resources.tllWidgetCss();
  }

  public static Resources resources() {
    return resources;
  }
}
