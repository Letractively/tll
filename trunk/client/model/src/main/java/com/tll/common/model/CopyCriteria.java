/**
 * The Logic Lab
 * @author jpk
 * @since Sep 22, 2009
 */
package com.tll.common.model;

import java.util.Set;

/**
 * CopyCriteria
 * @author jpk
 */
public class CopyCriteria {

	public static final CopyCriteria COPY_ALL = new CopyCriteria(true, true, null, null);

	final boolean references, markedDeleted;

	final Set<IModelProperty> whiteList, blackList;

	/**
	 * Constructor
	 * @param references copy reference properties?
	 * @param markedDeleted copy properties marked as deleted?
	 * @param whiteList list of properties that are copied. If <code>null</code>,
	 *        all properties are considered. If specified, the
	 *        <code>blackList</code> must be <code>null</code>.
	 * @param blackList list of properties that are not to be copied. If
	 *        <code>null</code>, all properties are considered. If specified, the
	 *        <code>whiteList</code> must be <code>null</code>.
	 */
	public CopyCriteria(boolean references, boolean markedDeleted, Set<IModelProperty> whiteList,
			Set<IModelProperty> blackList) {
		super();
		if(whiteList != null && blackList != null)
			throw new IllegalArgumentException("Only one black/white filter list may be specified");
		this.references = references;
		this.markedDeleted = markedDeleted;
		this.whiteList = whiteList;
		this.blackList = blackList;
	}

}
