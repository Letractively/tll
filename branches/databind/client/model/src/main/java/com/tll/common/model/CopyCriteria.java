/**
 * The Logic Lab
 * @author jpk
 * @since Sep 22, 2009
 */
package com.tll.common.model;

import java.util.Set;

import com.tll.IMarshalable;

/**
 * CopyCriteria
 * @author jpk
 */
public class CopyCriteria {

	static enum CopyMode implements IMarshalable {
		ALL,
		NO_REFERENCES,
		KEEP_REFERENCES,
		SUBSET,
		CHANGES
	}

	private static final CopyCriteria COPY_ALL = new CopyCriteria(CopyMode.ALL, null);

	private static final CopyCriteria COPY_NO_REFS = new CopyCriteria(CopyMode.NO_REFERENCES, null);

	private static final CopyCriteria COPY_KEEP_REFS = new CopyCriteria(CopyMode.KEEP_REFERENCES, null);

	private final CopyMode mode;

	private final Set<IModelProperty> whiteList;

	/**
	 * @return copy criteria that copies all props.
	 */
	public static CopyCriteria all() {
		return COPY_ALL;
	}

	/**
	 * @return copy criteria that copies all non-reference properties.
	 */
	public static CopyCriteria noReferences() {
		return COPY_NO_REFS;
	}

	/**
	 * @return copy criteria that copies all properties except model ref types in
	 *         which case the model ref is maintained.
	 */
	public static CopyCriteria keepReferences() {
		return COPY_KEEP_REFS;
	}

	/**
	 * Creates copy criteria where a whitelist is employed to create a sub-set of
	 * model properties.
	 * @param whiteList the subset of model properties to copy over
	 * @return the subset model
	 */
	public static CopyCriteria subset(Set<IModelProperty> whiteList) {
		return new CopyCriteria(CopyMode.SUBSET, whiteList);
	}

	/**
	 * Creates copy criteria in change mode where the given white list represent
	 * the properties that were altered.
	 * @param whiteList
	 * @return the "changed" model
	 */
	public static CopyCriteria changes(Set<IModelProperty> whiteList) {
		return new CopyCriteria(CopyMode.CHANGES, whiteList);
	}

	/**
	 * Constructor
	 * <p>
	 * <b>NOTE:</b> If <code>whiteList</code> is specified, both the
	 * <code>copyReferences</code> flag and the <code>markedDeleted</code> flag
	 * are trumped for any given model property referenced in that
	 * <code>whiteList</code>.
	 * @param mode copy mode
	 * @param whiteList list of properties that are copied. If <code>null</code>,
	 *        all properties are considered.
	 */
	private CopyCriteria(CopyMode mode, Set<IModelProperty> whiteList) {
		super();
		this.mode = mode;
		this.whiteList = whiteList;
	}

	/**
	 * @return the mode
	 */
	public CopyMode getMode() {
		return mode;
	}

	/**
	 * @return the whiteList
	 */
	public Set<IModelProperty> getWhitelistProps() {
		return whiteList;
	}

}