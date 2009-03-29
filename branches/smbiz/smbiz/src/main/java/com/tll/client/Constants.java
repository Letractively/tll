package com.tll.client;

/**
 * Constants
 * @author jpk
 */
public interface Constants extends com.google.gwt.i18n.client.Constants {

	/**
	 * @return the application version.
	 */
	@DefaultStringValue("0.0.0")
  @Key("appVersion")
  String appVersion();
}
