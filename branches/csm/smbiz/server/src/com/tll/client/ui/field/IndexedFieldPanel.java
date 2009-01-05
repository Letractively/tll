/**
 * The Logic Lab
 * @author jpk
 * Jan 3, 2009
 */
package com.tll.client.ui.field;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.tll.client.bind.IBindable;
import com.tll.client.model.MalformedPropPathException;
import com.tll.client.model.NullNodeInPropPathException;
import com.tll.client.model.PropertyPath;
import com.tll.client.model.PropertyPathException;
import com.tll.client.model.UnsetPropertyException;

/**
 * IndexedFieldPanel - Caters to the display of field collections that are
 * indexable by adapting the {@link FieldPanel} with index related routines. All
 * indexing operations sync with the {@link FieldPanel}'s member ("underlying")
 * field group.
 * @author jpk
 */
public abstract class IndexedFieldPanel<M extends IBindable> extends FieldPanel<M> {

	/**
	 * Resolves to the indexable property relative to a root field group that is
	 * parent to the field group of this field panel. {@link #list}.
	 * <p>
	 * E.g.: "account.addresses"
	 */
	private final String parentPropertyPath;

	/**
	 * The list that enables field groups to be resolved by an index.
	 */
	private final List<FieldGroup> list = new ArrayList<FieldGroup>();

	/**
	 * Reference to the last set value to determine whether or not to fire
	 * property change events.
	 */
	private Object lastValue;

	/**
	 * Generates a new field group for fields displayed at any given index.
	 */
	private final IFieldGroupProvider indexedFieldGroupProvider;

	/**
	 * Constructor
	 * @param parentPropertyPath
	 */
	public IndexedFieldPanel(String parentPropertyPath, IFieldGroupProvider indexedFieldGroupProvider) {
		super();
		this.parentPropertyPath = parentPropertyPath == null ? "" : parentPropertyPath;
		this.indexedFieldGroupProvider = indexedFieldGroupProvider;
	}

	/**
	 * @return The number of indexed field groups.
	 */
	protected final int size() {
		return list.size();
	}

	/**
	 * Updates an <em>existing</em> indexed field group with data provided by the
	 * given model syncing with the underlying field group.
	 * @param index The index at which the field group is replaced
	 * @param model The model from which data is retrieved to update the field
	 *        group
	 * @throws IndexOutOfBoundsException
	 * @throws IllegalStateException When the field group being replaced can't be
	 *         removed from the underlying field group for whatever reason.
	 */
	protected final void update(int index, M model) throws IndexOutOfBoundsException, IllegalStateException {
		assert model != null;

		FieldGroup fg = list.get(index);
		updateIndexedFieldGroup(fg, model);

		// update underlying list
		if(fg != null) {
			if(!getFieldGroup().removeField(fg)) {
				// revert state
				list.set(index, fg);
				throw new IllegalStateException("Unable to remove old field group from the underlying list");
			}
		}
	}

	/**
	 * Adds an indexed field group syncing with the underlying field group given a
	 * model instance which is first converted to a field group.
	 * @param model The model to be converted to a field group
	 * @throws IllegalArgumentException When the field to add already exists in
	 *         the underlying field group or has the same name as an existing
	 *         field in the underlying group.
	 */
	protected final void add(M model) throws IllegalArgumentException {
		assert model != null;

		FieldGroup fg = indexedFieldGroupProvider.getFieldGroup();
		updateIndexedFieldGroup(fg, model);

		// update underlying list first
		getFieldGroup().addField(fg);
		list.add(fg);
	}

	/**
	 * Removes an indexed field group at the given index syncing with the
	 * underlying field group.
	 * @param index The index at which the field group is removed
	 * @return The removed field group
	 * @throws IndexOutOfBoundsException
	 */
	protected final FieldGroup remove(int index) throws IndexOutOfBoundsException {
		FieldGroup old = list.remove(index);
		// update underlying list
		if(!getFieldGroup().removeField(old)) {
			throw new IllegalStateException("Indexed field group doesn't exist in underlying field group");
		}
		return old;
	}

	/**
	 * Updates an indexed field group with the values held in the given model.
	 * @param fg The indexed field group to be updated
	 * @param model The model
	 */
	private void updateIndexedFieldGroup(FieldGroup fg, M model) {
		final PropertyPath pp = new PropertyPath();
		for(IField<?> f : fg) {
			pp.parse(f.getPropertyName());
			if(pp.toString().startsWith(parentPropertyPath)) {
				String mpn = pp.last();
				try {
					f.setProperty(f.getPropertyName(), model.getProperty(mpn));
				}
				catch(UnsetPropertyException e) {
					// ok
				}
				catch(NullNodeInPropPathException e) {
					// ok
				}
				catch(PropertyPathException e) {
					// bad proeperty path/name
					throw new IllegalStateException(e);
				}
				catch(Exception e) {
					// bad value
					throw new IllegalStateException(e);
				}
			}
		}
	}

	@Override
	public Object getProperty(String propPath) throws PropertyPathException {
		return super.getProperty(propPath);
	}

	@SuppressWarnings("unchecked")
	@Override
	public final void setProperty(String propPath, Object value) throws PropertyPathException, Exception {
		PropertyPath pp = new PropertyPath(propPath);

		// ensure the parent property paths match
		String parentPath = pp.getParentPropertyPath();
		if(!parentPropertyPath.equals(parentPath == null ? "" : parentPath)) {
			throw new MalformedPropPathException("The parent property path does not match");
		}

		// TODO verify this!
		if(value == lastValue) return;

		if(pp.isIndexed()) {
			// we're expecting a single bindable

			FieldGroup old = null;

			final int index = pp.index();
			final int size = size();

			try {
				if(index == size) {
					if(value != null) {
						// add
						add((M) value);
					}
				}
				else if(index < size) {
					if(value != null) {
						// replace
						update(index, (M) value);
					}
					else {
						// remove
						old = remove(index);
					}
				}
				else {
					throw new IndexOutOfBoundsException("Index " + index + " is out of bounds.");
				}
				this.lastValue = value;
				if(changeSupport != null && old != value) {
					changeSupport.fireIndexedPropertyChange(propPath, index, old, value);
				}
			}
			catch(Throwable e) {
				throw new Exception(e);
			}
		}
		else {
			// we're expecting a collection of bindables
			if(value != null && value instanceof Collection == false) {
				throw new Exception("The given value is not a collection");
			}

			// clear the list first
			for(FieldGroup fg : list) {
				getFieldGroup().removeField(fg);
			}
			list.clear();

			if(value != null) {
				// translate the collection, add to list and sync
				Collection<M> mc = (Collection<M>) value;
				for(M m : mc) {
					FieldGroup fg = indexedFieldGroupProvider.getFieldGroup();
					updateIndexedFieldGroup(fg, m);
					try {
						getFieldGroup().addField(fg);
						list.add(fg);
					}
					catch(Throwable e) {
						// we're hosed
						// TODO put in roll-back code! so we maintain data integrity between
						// the list and the underlying field group
						throw new IllegalStateException(e);
					}
				}
			}
			this.lastValue = value;
			if(changeSupport != null) changeSupport.firePropertyChange(propPath, list, value);
		}
	}

	@Override
	protected FieldGroup generateFieldGroup() {
		throw new UnsupportedOperationException();
	}

}
