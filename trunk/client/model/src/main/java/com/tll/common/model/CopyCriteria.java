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

	public static final CopyCriteria COPY_ALL = new CopyCriteria(true, true, true, null);

	final boolean idAndVersion, references, markedDeleted;

	final Set<IModelProperty> whiteList;

	/**
	 * Constructor
	 * <p>
	 * <b>NOTE:</b> If <code>whiteList</code> is specified, both the
	 * <code>references</code> flag and the <code>markedDeleted</code> flag are
	 * trumped for any given model property referenced in that <code>whiteList</code>.
	 * @param idAndVersion If specified, the id and version are transferred for
	 *        all nested {@link Model} instances that are copied. This allows the
	 *        server to resolve the corres. server side entity.
	 * @param references copy reference properties?
	 * @param markedDeleted copy properties marked as deleted?
	 * @param whiteList list of properties that are copied. If <code>null</code>,
	 *        all properties are considered.
	 */
	public CopyCriteria(boolean idAndVersion, boolean references, boolean markedDeleted, Set<IModelProperty> whiteList) {
		super();
		this.idAndVersion = idAndVersion;
		this.references = references;
		this.markedDeleted = markedDeleted;
		this.whiteList = whiteList;
	}
}
