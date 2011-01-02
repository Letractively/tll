/**
 * The Logic Lab
 * @author jpk
 * @since Mar 26, 2009
 */
package com.tll.client.view;



/**
 * IViewKeyProvider
 * @author jpk
 */
public interface IViewKeyProvider extends Comparable<IViewKeyProvider> {

	/**
	 * @return The {@link ViewKey}.
	 */
	ViewKey getViewKey();
}
