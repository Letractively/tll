/**
 * The Logic Lab
 * @author jpk
 * Mar 5, 2009
 */
package com.tll.common.bind;

/**
 * IModel - Definition for a bindable model implementation.
 * @author jpk
 */
public interface IModel extends IBindable {

	/**
	 * @return <code>true</code> if this model is marked as deleted
	 */
	boolean isMarkedDeleted();

	/**
	 * Mark or un-mark as deleted.
	 * @param delete mark deleted if <code>true<code>
	 */
	void setMarkedDeleted(boolean delete);
}
