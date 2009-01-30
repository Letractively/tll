/**
 * The Logic Lab
 */
package com.tll.common.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import com.tll.common.bind.PropertyChangeEvent;
import com.tll.model.EntityType;
import com.tll.model.schema.PropertyType;

/**
 * RelatedManyProperty
 * @author jpk
 */
public final class RelatedManyProperty extends AbstractRelationalProperty implements Iterable<IndexedProperty> {

	/**
	 * IndexedIterator
	 * @author jpk
	 */
	public final class IndexedIterator implements Iterator<IndexedProperty> {

		private int index = -1;
		private final int size;

		/**
		 * Constructor
		 */
		public IndexedIterator() {
			super();
			this.size = list == null ? 0 : list.size();
		}

		public boolean hasNext() {
			return index < (size - 1);
		}

		public IndexedProperty next() {
			if(index >= size) {
				throw new NoSuchElementException();
			}
			return getIndexedProperty(++index);
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	/**
	 * The list of related many {@link Model}s.
	 */
	protected List<Model> list;

	/**
	 * Constructor
	 */
	public RelatedManyProperty() {
		super();
	}

	/**
	 * Constructor
	 * @param propName
	 * @param reference
	 * @param manyType The related many Model type.
	 * @param clc Collection of {@link Model}s.
	 */
	public RelatedManyProperty(final EntityType manyType, final String propName, final boolean reference,
			final Collection<Model> clc) {
		super(manyType, propName, reference);
		setValue(clc);
	}

	public PropertyType getType() {
		return PropertyType.RELATED_MANY;
	}

	public final Object getValue() {
		return getList();
	}

	@SuppressWarnings("unchecked")
	public void setValue(Object value) throws IllegalArgumentException {
		if(this.list == value) return;

		if(value == null) {
			if(list != null) {
				final Object old = list;
				list = null;
				getChangeSupport().firePropertyChange(propertyName, old, list);
			}
		}
		else if(value instanceof Collection) {
			final Collection<Model> clc = (Collection) value;
			final Object old = list;
			list = new ArrayList<Model>(clc.size());
			list.addAll(clc);
			getChangeSupport().firePropertyChange(propertyName, old, list);
		}
		else {
			throw new IllegalArgumentException("The value must be a collection of Model instances");
		}

	}

	/**
	 * Provides the encased List of {@link Model}s.
	 * <p>
	 * <em><b>IMPT:</b>Mutations made to this list do <b>NOT</b> invoke {@link PropertyChangeEvent}s.</em>
	 * @return the list of indexed models
	 */
	public List<Model> getList() {
		if(list == null) {
			list = new ArrayList<Model>();
		}
		return list;
	}

	/**
	 * Get an indexed property at the given index.
	 * @param index The index
	 * @return The {@link IndexedProperty} at the given index.
	 */
	protected IndexedProperty getIndexedProperty(int index) {
		return new IndexedProperty(relatedType, propertyName, isReference(), list, index);
	}

	public Iterator<IndexedProperty> iterator() {
		return new IndexedIterator();
	}

	@Override
	public void setProperty(String propPath, Object value) throws PropertyPathException, Exception {
		PropertyPath pp = new PropertyPath(propPath);
		if(pp.isIndexed()) {
			if(value != null && value instanceof Model == false) {
				throw new Exception("The value must be a Model instance");
			}

			final Model m = (Model) value;
			Model old = null;

			final int index = pp.index();
			final int size = size();

			if(index == size) {
				if(m != null) {
					// add
					list.add(m);
				}
			}
			else if(index < size) {
				if(m != null) {
					// replace
					old = list.set(index, m);
				}
				else {
					// remove
					old = list.remove(index);
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
		sb.append(isReference() ? "|REF|" : "");
		sb.append(" [");
		if(list != null) {
			for(Iterator<Model> itr = list.iterator(); itr.hasNext();) {
				Model m = itr.next();
				sb.append(m == null ? "-empty-" : m.toString());
				if(itr.hasNext()) {
					sb.append(", ");
				}
			}
		}
		else {
			sb.append("-empty-");
		}
		sb.append(']');

		return sb.toString();
	}
}
