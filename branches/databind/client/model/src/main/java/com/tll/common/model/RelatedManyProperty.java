/**
 * The Logic Lab
 */
package com.tll.common.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.tll.common.bind.PropertyChangeEvent;
import com.tll.schema.PropertyType;
import com.tll.util.PropertyPath;

/**
 * RelatedManyProperty
 * @author jpk
 */
public final class RelatedManyProperty extends AbstractRelationalProperty implements Iterable<IndexedProperty> {

	/**
	 * The list of indexed props.
	 */
	private final ArrayList<IndexedProperty> list = new ArrayList<IndexedProperty>();

	/**
	 * The value collection whereby each indexed model corresponds to the wrapped
	 * model in the {@link #list}.
	 */
	private ArrayList<Model> mlist;

	/**
	 * Constructor
	 */
	public RelatedManyProperty() {
		super();
	}

	/**
	 * Constructor
	 * @param manyType The required related many Model type.
	 * @param propName
	 * @param reference
	 * @param clc Collection of {@link Model}s.
	 */
	public RelatedManyProperty(final IEntityType manyType, final String propName, final boolean reference,
			final Collection<Model> clc) {
		super(manyType, propName, reference);
		setValue(clc);
	}

	public PropertyType getType() {
		return PropertyType.RELATED_MANY;
	}

	public Object getValue() {
		return getModelList();
	}

	@SuppressWarnings("unchecked")
	public void setValue(Object value) throws IllegalArgumentException {
		if(this.mlist == value) return;

		if(value == null) {
			if(mlist != null) {
				final Object old = mlist;
				list.clear();
				mlist = null;
				getChangeSupport().firePropertyChange(propertyName, old, mlist);
			}
		}
		else if(value instanceof Collection) {
			final Collection<Model> clc = (Collection) value;
			final Object old = mlist;
			list.clear();
			if(mlist == null) {
				mlist = new ArrayList<Model>(clc.size());
			}
			else {
				mlist.clear();
			}
			int i = 0;
			for(final Model im : clc) {
				final IndexedProperty ip = new IndexedProperty(relatedType, im, propertyName, isReference(), i++);
				list.add(ip);
				mlist.add(im);
			}
			// IMPT: refer to the value agrument as the new value to avoid spurious
			// re-firings of property change events
			getChangeSupport().firePropertyChange(propertyName, old, value);
		}
		else {
			throw new IllegalArgumentException("The value must be a collection of Model instances");
		}

	}

	/**
	 * Inserts a {@link Model} instance at the given index.
	 * <p>
	 * <b>NOTE: </b>This method does <em>not</em> raise property change events.
	 * @param model the model to insert
	 * @param index the index at which to insert
	 * @throws IllegalArgumentException When the model is <code>null</code> or its
	 *         entity type does not match this properties reference type.
	 * @throws IndexOutOfBoundsException when the index is out of bounds
	 */
	public void insert(Model model, int index) throws IllegalArgumentException, IndexOutOfBoundsException {
		if(model == null) throw new IllegalArgumentException("Null index models not allowed.");
		if(!this.relatedType.equals(model.getEntityType())) throw new IllegalArgumentException("Entity type mismatch.");
		list.add(new IndexedProperty(relatedType, model, propertyName, isReference(), index));
		mlist.add(model);
	}

	/**
	 * Removes a {@link Model} instance at the given index.
	 * <p>
	 * <b>NOTE: </b>This method does <em>not</em> raise property change events.
	 * @param index the index at which to remove the model
	 * @return the removed model
	 * @throws IndexOutOfBoundsException when the index is out of bounds
	 */
	public Model remove(int index) {
		Model m = mlist.remove(index);
		list.remove(index);
		return m;
	}

	/**
	 * Removes a {@link Model} instance given a key.
	 * <p>
	 * <b>NOTE: </b>This method does <em>not</em> raise property change events.
	 * @param key
	 * @return <code>true</code> if the model was found and removed.
	 */
	public boolean remove(ModelKey key) {
		Model m = Model.findInCollection(mlist, key);
		if(m == null) return false;
		for(IndexedProperty ip : list) {
			if(ip.model == m) {
				list.remove(ip);
				mlist.remove(m);
				return true;
			}
		}
		return false;
	}

	/**
	 * Provides the encased List of {@link Model}s.
	 * <p>
	 * <em><b>IMPT:</b>Mutations made to this list do <b>NOT</b> invoke {@link PropertyChangeEvent}s.</em>
	 * @return the list of indexed models
	 */
	public List<Model> getModelList() {
		// don't give a ref to "our" list, rather copy the elements into a new list
		return mlist == null ? new ArrayList<Model>(0) : new ArrayList<Model>(mlist);
	}

	/**
	 * Get an indexed property at the given index.
	 * @param index The index
	 * @throws IndexOutOfBoundsException
	 * @return The {@link IndexedProperty} at the given index.
	 */
	public IndexedProperty getIndexedProperty(int index) throws IndexOutOfBoundsException {
		return list.get(index);
	}

	public Iterator<IndexedProperty> iterator() {
		return list.iterator();
	}

	@Override
	public void setProperty(String propPath, Object value) throws PropertyPathException, IllegalArgumentException {
		final PropertyPath pp = new PropertyPath(propPath);
		if(pp.isIndexed()) {
			if(value != null && value instanceof Model == false) {
				throw new IllegalArgumentException("The value must be a Model instance");
			}

			final Model m = (Model) value;
			Model old = null;

			final int index;
			try {
				index = pp.index();
			}
			catch(final IllegalArgumentException e) {
				throw new MalformedPropPathException(e.getMessage());
			}
			final int size = size();

			if(index == size) {
				// we're appending a new index
				if(m != null) {
					// add
					if(mlist == null) {
						mlist = new ArrayList<Model>();
					}
					mlist.add(m);
					list.add(new IndexedProperty(relatedType, m, propertyName, isReference(), index));
				}
			}
			else if(index < size) {
				if(m != null) {
					// replace
					old = mlist.set(index, m);
					list.get(index).setModel(m, false);
				}
				else {
					// remove
					old = mlist.remove(index);
					list.remove(index);
				}
			}
			else {
				throw new IndexOutOfRangeInPropPathException(propPath, pp.last(), pp.index());
			}
			if(old != value) {
				getChangeSupport().fireIndexedPropertyChange(propertyName, index, old, value);
			}
		}
		else {
			setValue(value);
		}

		assert ((mlist == null && list.size() == 0) || (mlist.size() == list.size()));
	}

	/**
	 * @return The number of indexed models in this related many list.
	 */
	public int size() {
		return list == null ? 0 : list.size();
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(propertyName);
		sb.append(isReference() ? "[REF] " : " ");
		sb.append(" [");
		if(list != null) {
			for(final Iterator<IndexedProperty> itr = list.iterator(); itr.hasNext();) {
				final IndexedProperty ip = itr.next();
				sb.append(ip);
				if(itr.hasNext()) {
					sb.append(", ");
				}
			}
		}
		sb.append(']');

		return sb.toString();
	}
}
