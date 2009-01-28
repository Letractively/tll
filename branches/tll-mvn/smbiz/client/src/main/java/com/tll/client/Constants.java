package com.tll.client;

/**
 * Constants - app wide constants that are subject to being set by the build
 * process. Put "static" constants in the {@link App} class.
 * @author jpk
 */
public interface Constants extends com.google.gwt.i18n.client.Constants {

	/**
	 * @return the "app.version" app property.
	 */
	String appVersion();

	/**
	 * @return the "environment" app property.
	 */
	String environment();

	/**
	 * @return the "debug" app property.
	 */
	boolean debug();
}
