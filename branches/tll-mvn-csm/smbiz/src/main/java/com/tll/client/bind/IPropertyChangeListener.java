/**
 * The Logic Lab
 * @author jpk
 * Dec 22, 2008
 */
package com.tll.client.bind;

import java.util.EventListener;

/**
 * IPropertyChangeListener - Replica of the java.util.PropertyChangeListener
 * interface.
 * <p><em><b>IMPT NOTE: </b>This code was originally derived from the <a href="http://gwittir.googlecode.com/">gwittir</a> project.</em>
 * @author jpk
 */
public interface IPropertyChangeListener extends EventListener {

	/**
	 * This method gets called when a bound property is changed.
	 * @param evt A PropertyChangeEvent object describing the event source and the
	 *        property that has changed.
	 */
	void propertyChange(PropertyChangeEvent evt);
}
