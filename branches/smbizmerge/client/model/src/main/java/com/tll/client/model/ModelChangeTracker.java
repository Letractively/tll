/**
 * The Logic Lab
 * @author jpk
 * @since Sep 22, 2009
 */
package com.tll.client.model;

import java.util.HashSet;

import com.tll.common.model.CopyCriteria;
import com.tll.common.model.IModelProperty;
import com.tll.common.model.IPropertyChangeListener;
import com.tll.common.model.IPropertyValue;
import com.tll.common.model.Model;
import com.tll.common.model.PropertyChangeEvent;
import com.tll.common.model.PropertyPathException;
import com.tll.model.PropertyMetadata;

/**
 * ModelChangeTracker - Tracks model properties whose value has changed (become
 * dirty).
 * @author jpk
 */
public final class ModelChangeTracker implements IPropertyChangeListener {

	/**
	 * The set of model properties marked as changed.
	 */
	private HashSet<IModelProperty> propRefChanges;

	/**
	 * Set of property paths marked as changed.
	 */
	private HashSet<String> pathChanges;

	/**
	 * Flag to turn on/off this tracker to jive with the field binding life-cycle.
	 */
	private boolean handleChanges = true;

	/**
	 * Manually add a change by model property ref
	 * @param mp the model property ref
	 */
	public void addChange(IModelProperty mp) {
		if(mp instanceof IPropertyValue) {
			final PropertyMetadata pm = ((IPropertyValue)mp).getMetadata();
			if(pm != null && pm.isManaged()) return;	// don't track managed props
		}
		if(propRefChanges == null) {
			propRefChanges = new HashSet<IModelProperty>();
		}
		propRefChanges.add(mp);
	}

	/**
	 * Manuall add a change by a root relative property path
	 * @param path relative to the root model subject to editing
	 */
	public void addChange(String path) {
		if(pathChanges == null) {
			pathChanges = new HashSet<String>();
		}
		pathChanges.add(path);
	}

	/**
	 * Removes a previously added model prop ref change.
	 * @param mp the prop ref to remove
	 */
	public void removeChange(IModelProperty mp) {
		if(propRefChanges != null) {
			propRefChanges.remove(mp);
		}
	}

	/**
	 * Removes a previously added path change.
	 * @param path the path to remove
	 */
	public void removeChange(String path) {
		if(pathChanges != null) {
			pathChanges.remove(path);
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(!handleChanges) return;
		final Object src = evt.getSource();
		if(src instanceof Model) {
			try {
				final IModelProperty mp = ((Model) src).getModelProperty(evt.getPropertyName());
				addChange(mp);
			}
			catch(final PropertyPathException e) {
				throw new IllegalStateException("Unable to resolve model property: " + evt.getPropertyName()
						+ " due to error: " + e.getMessage(), e);
			}
		}
		else if(src instanceof IModelProperty) {
			addChange((IModelProperty) src);
		}
		else {
			throw new IllegalArgumentException("Unhandled event source type: " + src);
		}
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
	 * @return The total number of recorded changes.
	 */
	public int getNumChanges() {
		int c = 0;
		if(propRefChanges != null) c+= propRefChanges.size();
		if(pathChanges != null) c+= pathChanges.size();
		return c;
	}

	/**
	 * Removes all changes.
	 */
	public void clear() {
		if(propRefChanges != null) propRefChanges.clear();
		if(pathChanges != null) pathChanges.clear();
	}

	/**
	 * Generates a new {@link Model} instance containing a sub-set of the given
	 * model containing only those properties that were altered.
	 * @param root The root model
	 * @return {@link Model} instance containing only those properties that were
	 *         previously added to this tracker or <code>null</code> if no changes
	 *         were added.
	 */
	public Model generateChangeModel(Model root) {
		if(propRefChanges == null && pathChanges == null) return null;
		final HashSet<IModelProperty> allChanges = new HashSet<IModelProperty>();
		if(propRefChanges != null) {
			allChanges.addAll(propRefChanges);
		}
		if(pathChanges != null) {
			for(final String path : pathChanges) {
				IModelProperty mp;
				try {
					mp = root.getModelProperty(path);
				}
				catch(final PropertyPathException e) {
					throw new IllegalStateException("Unable to resolve path: " + path, e);
				}
				allChanges.add(mp);
			}
		}
		return root.copy(CopyCriteria.changes(allChanges));
	}
}
