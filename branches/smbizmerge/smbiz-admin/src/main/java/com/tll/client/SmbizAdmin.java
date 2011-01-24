package com.tll.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class SmbizAdmin implements EntryPoint {
  /**
   * The message displayed to the user when the server cannot be reached or
   * returns an error.
   */
  private static final String SERVER_ERROR = "An error occurred while "
      + "attempting to contact the server. Please check your network "
      + "connection and try again.";

  private final Messages messages = GWT.create(Messages.class);

  /**
   * This is the entry point method.
   */
  @Override
	public void onModuleLoad() {
  }
}
