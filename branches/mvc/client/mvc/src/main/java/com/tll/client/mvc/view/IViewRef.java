/**
 * The Logic Lab
 * @author jpk
 * Mar 19, 2008
 */
package com.tll.client.mvc.view;

/**
 * IViewRef - A view key provider with embellishing short and long view names.
 * @author jpk
 */
public interface IViewRef extends IViewKeyProvider {

	/**
	 * @return The short view name.
	 */
	String getShortViewName();

	/**
	 * @return The long view name.
	 */
	String getLongViewName();
}