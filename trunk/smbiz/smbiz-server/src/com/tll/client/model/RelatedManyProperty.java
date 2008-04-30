/**
 * The Logic Lab
 */
package com.tll.client.model;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import com.tll.model.schema.PropertyType;

/**
 * RelatedManyProperty
 * @author jpk
 */
public class RelatedManyProperty extends AbstractRelationalProperty implements Iterable<IndexedProperty> {

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
	 * @param list List of {@link Model}s.
	 */
	public RelatedManyProperty(final String propName, final boolean reference, final List<Model> list) {
		super(propName, reference);
		this.list = list;
	}

	public String descriptor() {
		return "Related many collection property";
	}

	public PropertyType getType() {
		return PropertyType.RELATED_MANY;
	}

	public final Object getValue() {
		return list;
	}

	/**
	 * Provides the encased List of {@link Model}s.
	 */
	public List<Model> getList() {
		return list;
	}

	void setList(final List<Model> list) {
		this.list = list;
	}

	/**
	 * IndexedIterator
	 * @author jpk
	 */
	public class IndexedIterator implements Iterator<IndexedProperty> {

		private int index = -1;
		private final int size;

		/**
		 * Constructor
		 */
		public IndexedIterator() {
			super();
			this.size = list == null ? 0 : list.size();
		}

		/**
		 * @return the index
		 */
		public int getIndex() {
			return index;
		}

		/**
		 * @return the size
		 */
		public int getSize() {
			return size;
		}

		public boolean hasNext() {
			return index < (size - 1);
		}

		public IndexedProperty next() {
			if(index >= size) {
				throw new NoSuchElementException();
			}
			final Model model = list.get(++index);
			return new IndexedProperty(propertyName, isReference(), model, index);
		}

		public void remove() {
			throw new UnsupportedOperationException("Indexed property value iterators don't support removal");
		}
	}

	public Iterator<IndexedProperty> iterator() {
		return new IndexedIterator();
	}

	public int size() {
		return list == null ? 0 : list.size();
	}

	@Override
	public String toString() {
		if(list == null) return "<null>";
		final StringBuffer sb = new StringBuffer();
		sb.append(getPropertyName());
		sb.append("|ref:");
		sb.append(isReference());
		sb.append('|');
		sb.append(":[");
		if(list != null) {
			for(final Iterator<Model> itr = list.iterator(); itr.hasNext();) {
				final Model model = itr.next();
				sb.append("hash:");
				sb.append(model.hashCode());
				if(itr.hasNext()) {
					sb.append(", ");
				}
			}
		}
		else {
			sb.append("null");
		}
		sb.append(']');

		return sb.toString();
	}
}
