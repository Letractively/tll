/**
 * The Logic Lab
 * @author jpk
 * Feb 17, 2008
 */
package com.tll.common.model;

/**
 * IndexOutOfRangeInPropPathException - Indicates a given index w/in a property
 * path is out of range.
 * @author jpk
 */
@SuppressWarnings("serial")
public final class IndexOutOfRangeInPropPathException extends PropertyPathException {

	private final String indexedProp;

	private final int index;

	/**
	 * Constructor
	 * @param propPath
	 * @param indexedProp
	 * @param index
	 */
	IndexOutOfRangeInPropPathException(final String propPath, final String indexedProp, final int index) {
		super("Property index : " + index + " for indexed prop: '" + indexedProp + "' is out of range in prop path: "
				+ propPath, propPath);
		this.indexedProp = indexedProp;
		this.index = index;
	}

	/**
	 * @return the indexedProp
	 */
	public String getIndexedProp() {
		return indexedProp;
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

}
