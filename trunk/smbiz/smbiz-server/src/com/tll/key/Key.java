/**
 * The Logic Lab
 * @author jpk
 * Nov 14, 2007
 */
package com.tll.key;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.tll.SystemError;
import com.tll.util.CommonUtil;

/**
 * Key
 * @author jpk
 */
public abstract class Key<T> implements IKey<T> {

	private Object[] values;

	/**
	 * Constructor
	 */
	public Key() {
		super();
		clear();
	}

	public void clear() {
		values = new Object[getFields().length];
	}

	public boolean isSet() {
		return (!ArrayUtils.isEmpty(getValues()));
	}

	/**
	 * @return String array of the field names. NOTE: The ordinality of this array
	 *         is significant.
	 */
	protected abstract String[] getFields();

	/**
	 * @return an array of field values whose elements corres. to the
	 *         {@link #getFields()} method.
	 */
	protected Object[] getValues() {
		return values;
	}

	/**
	 * Returns the field value at the specified index.
	 * @param i the index
	 * @return the field value
	 */
	protected Object getValue(int i) {
		return values[i];
	}

	/**
	 * Sets the field value at the specific index. The index should correspond to
	 * the index of the field name in getFields()
	 * @param i the index
	 * @param value the field value
	 */
	protected void setValue(int i, Object value) {
		values[i] = value;
	}

	/**
	 * Gets the field index for a given field name
	 * @param fieldName
	 * @return the corres. 0-based field index or -1 if not found
	 */
	private int getFieldIndex(String fieldName) {
		if(fieldName != null && fieldName.length() > 0) {
			int i = 0;
			for(final String f : getFields()) {
				if(f.equals(fieldName)) {
					return i;
				}
				i++;
			}
		}
		return -1;
	}

	public final Object getFieldValue(String fieldName) {
		final int i = getFieldIndex(fieldName);
		if(i < 0) {
			throw new IllegalArgumentException("Empty or invalid key field name: " + fieldName);
		}
		return getValue(i);
	}

	public final void setFieldValue(String fieldName, Object fieldValue) {
		final int i = getFieldIndex(fieldName);
		if(i < 0) {
			throw new IllegalArgumentException("Unable to set field value: Empty or invalid key field name: " + fieldName);
		}
		this.setValue(i, fieldValue);
	}

	private boolean fieldsEqual(Key<T> key) {
		final String[] theseFields = getFields();
		final String[] thoseFields = key.getFields();
		if(theseFields.length != thoseFields.length) {
			return false;
		}
		for(int i = 0; i < theseFields.length; i++) {
			final String tf = theseFields[i];
			final String of = thoseFields[i];
			if((tf != null && !tf.equals(of)) || (tf == null && of != null) || (tf != null && of == null)) {
				return false;
			}
		}
		return true;
	}

	private boolean valuesEqual(Key<T> key) {
		for(int i = 0; i < getValues().length; i++) {
			if(getValue(i) == null || !getValue(i).equals(key.getValue(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Is the given class compatible with this key type? Used in
	 * {@link #compareTo(IKey)} for instance.
	 * @param type The type to compare to this key's type.
	 * @return <code>true</code> if compatible.
	 */
	private boolean typeCompatible(Class<T> type) {
		final Class<T> thisType = getType();
		return (type == null || thisType == null) ? false : thisType.isAssignableFrom(type)
				|| type.isAssignableFrom(thisType);
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(!(obj instanceof Key)) {
			return false;
		}
		final Key key = (Key) obj;
		return typeCompatible(key.getType()) && fieldsEqual(key) && valuesEqual(key);
	}

	@Override
	public int hashCode() {
		int result = getClass().hashCode();
		for(int i = 0; i < getValues().length; i++) {
			if(getValue(i) != null) {
				result = 29 * result + getValue(i).hashCode();
			}
		}
		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected Object clone() throws CloneNotSupportedException {
		final Key<T> key = (Key) super.clone();

		final Object[] fvalues = getValues();
		if(fvalues != null) {
			key.values = new Object[fvalues.length];
		}

		for(int i = 0; i < getValues().length; i++) {
			final Object value = getValue(i);
			key.setValue(i, CommonUtil.clone(value));
		}

		return key;
	}

	@SuppressWarnings("unchecked")
	public final IKey<T> copy() {
		try {
			return (IKey<T>) clone();
		}
		catch(final CloneNotSupportedException e) {
			throw new SystemError("Unable to copy an IKey!");
		}
	}

	@SuppressWarnings("unchecked")
	public final int compareTo(IKey<T> o) {
		if(o == null || (o instanceof Key == false) || !typeCompatible(o.getType())) {
			throw new IllegalArgumentException("Null or uncomparable key: " + o.descriptor());
		}
		final Key<T> key = (Key<T>) o;
		if(!fieldsEqual(key)) {
			throw new IllegalArgumentException("Uncomparable key - fields don't match: " + o.descriptor());
		}
		return new CompareToBuilder().append(getValues(), key.getValues()).toComparison();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(getType()).append(getFields()).append(getValues()).toString();
	}

}
