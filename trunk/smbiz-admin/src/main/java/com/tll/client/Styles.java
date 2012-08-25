package com.tll.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

/**
 * @author jpk
 */
public class Styles {

  /**
   * Common styles.
   */
  public interface Common extends CssResource {

  }

  /**
   * Shared resources.
   */
  public interface Resources extends ClientBundle {

    //@Source("../Messages.properties")
    //TextResource messages();

    //ImageResource approvedIcon();
  }

  private static Resources resources;

  static {
    resources = GWT.create(Resources.class);
    //resources.common().ensureInjected();
  }

//  public static Common common() {
//    return resources.common();
//  }

  public static Resources resources() {
    return resources;
  }
}
