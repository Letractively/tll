/**
 * The Logic Lab
 * @author jpk
 * @since May 18, 2009
 */
package com.tll.common.search;

import com.tll.common.model.ModelKey;


/**
 * PrimaryKeySearch
 * @author jpk
 */
public class PrimaryKeySearch extends SearchBase implements IPrimaryKeySearch {

	private ModelKey key;

	/**
	 * Constructor
	 */
	public PrimaryKeySearch() {
		super();
	}

	/**
	 * Constructor
	 * @param key
	 */
	public PrimaryKeySearch(ModelKey key) {
		super();
		this.key = key;
	}

	@Override
	public ModelKey getKey() {
		return key;
	}

	@Override
	public void clear() {
		if(key != null) key.clear();
	}

	@Override
	public boolean isSet() {
		return key == null ? false : key.isSet();
	}
}
