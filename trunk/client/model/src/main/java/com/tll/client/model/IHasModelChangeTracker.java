/**
 * The Logic Lab
 * @author jpk
 * @since Sep 28, 2009
 */
package com.tll.client.model;


/**
 * IHasModelChangeTracker
 * @author jpk
 */
public interface IHasModelChangeTracker {

	/**
	 * @return The model change tracker.
	 */
	public ModelChangeTracker getModelChangeTracker();

	/**
	 * Sets the model change tracker.
	 * @param modelChangeTracker
	 */
	void setModelChangeTracker(ModelChangeTracker modelChangeTracker);
}
