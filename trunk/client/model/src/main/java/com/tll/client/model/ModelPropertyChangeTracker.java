/**
 * The Logic Lab
 * @author jpk
 * @since Sep 22, 2009
 */
package com.tll.client.model;

import java.util.HashSet;

import com.tll.common.bind.IPropertyChangeListener;
import com.tll.common.bind.PropertyChangeEvent;
import com.tll.common.model.CopyCriteria;
import com.tll.common.model.IModelProperty;
import com.tll.common.model.Model;

/**
 * ModelPropertyChangeTracker - Tracks model properties whose value has changed (become dirty).
 * @author jpk
 */
public class ModelPropertyChangeTracker implements IPropertyChangeListener {

	// chronological ordering
	private final HashSet<IModelProperty> changes = new HashSet<IModelProperty>();

	private Model root;

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		changes.add((IModelProperty)evt.getSource());
	}

	/**
	 * Initializes this tracker for the given root model.
	 * @param rootModel
	 */
	public void set(Model rootModel) {
		clear();
		this.root = rootModel;
	}

	/**
	 * Clears all state with a previously set root model.
	 */
	public void clear() {
		changes.clear();
		root = null;
	}

	/**
	 * @return {@link Model} instance at the same "level" as the specified root
	 *         model containing <em>only</em> those properties that have been
	 *         tracked as changed.
	 */
	public Model generateChangeModel() {
		return root.copy(new CopyCriteria(true, false,  true, changes));
	}
}
