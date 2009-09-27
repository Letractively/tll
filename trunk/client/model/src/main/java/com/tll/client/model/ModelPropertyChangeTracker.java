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
import com.tll.common.model.PropertyPathException;

/**
 * ModelPropertyChangeTracker - Tracks model properties whose value has changed
 * (become dirty).
 * @author jpk
 */
public class ModelPropertyChangeTracker implements IPropertyChangeListener {

	/**
	 *  The root model subject to editing.
	 */
	private Model root;

	/**
	 * The set of model properties under that have been altered.
	 */
	private final HashSet<IModelProperty> changes = new HashSet<IModelProperty>();

	/**
	 * Flag to turn on/off this tracker to jive with the field binding life-cycle.
	 */
	private boolean handleChanges = true;

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(!handleChanges) return;
		final Object src = evt.getSource();
		if(src instanceof Model) {
			try {
				final IModelProperty mp = ((Model) src).getModelProperty(evt.getPropertyName());
				changes.add(mp);
			}
			catch(final PropertyPathException e) {
				throw new IllegalStateException("Unable to resolve model property: " + evt.getPropertyName()
						+ " due to error: " + e.getMessage(), e);
			}
		}
		else if(src instanceof IModelProperty) {
			changes.add((IModelProperty) src);
		}
		else {
			throw new IllegalArgumentException("Unhandled event source type: " + src);
		}
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
	 * @return <code>true</code> if this instance is tracking changes.
	 */
	public boolean isHandleChanges() {
		return handleChanges;
	}

	/**
	 * Manually turns or or off property change tracking.
	 * @param handleChanges the handleChanges to set
	 */
	public void setHandleChanges(boolean handleChanges) {
		this.handleChanges = handleChanges;
	}

	/**
	 * Clears all state with a previously set root model.
	 */
	public void clear() {
		changes.clear();
		//root = null;
	}

	/**
	 * @return {@link Model} instance at the same "level" as the specified root
	 *         model containing <em>only</em> those properties that have been
	 *         tracked as changed.
	 */
	public Model generateChangeModel() {
		return root.copy(CopyCriteria.changes(changes));
	}
}
