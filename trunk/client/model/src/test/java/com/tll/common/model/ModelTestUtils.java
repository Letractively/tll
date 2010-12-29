/**
 * The Logic Lab
 * @author jpk Feb 12, 2009
 */
package com.tll.common.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.tll.TestUtils;
import com.tll.common.model.CopyCriteria.CopyMode;
import com.tll.model.PropertyType;

/**
 * ModelTestUtils
 * @author jpk
 */
public class ModelTestUtils {

	/**
	 * Validates a source {@link Model} to a copied {@link Model} verifying the
	 * copied model's properties.
	 * @param source
	 * @param copy
	 * @param criteria the copy criteria that are marked deleted
	 * @throws Exception When a copy discrepancy is encountered
	 */
	public static void validateCopy(final Model source, final Model copy, CopyCriteria criteria) throws Exception {
		final boolean references = criteria.getMode() == CopyMode.ALL;
		final boolean ignoreMissingProps = criteria.getMode() == CopyMode.CHANGES;
		compare(source, copy, references, true, ignoreMissingProps, new ArrayList<Model>());
		if(source.size() != copy.size()) throw new Exception("source/copy size mismatch");
	}

	/**
	 * Validates all properties contained in the given {@link Model} are clear.
	 * @param source
	 * @param visited
	 * @throws Exception
	 */
	public static void validateClear(final Model source, final List<Model> visited) throws Exception {
		assert source != null;

		for(final Iterator<IModelProperty> itr = source.iterator(); itr.hasNext();) {
			final IModelProperty srcMp = itr.next();
			final PropertyType pvType = srcMp.getType();
			if(pvType.isValue()) {
				// require cleared property value
				final Object srcValue = srcMp.getValue();
				TestUtils.validateEmpty(srcValue);
			}
			else if(pvType == PropertyType.RELATED_ONE) {
				// drill into if not already visited
				final IModelRefProperty srcMrp = (IModelRefProperty) srcMp;
				final Model m = srcMrp.getModel();
				visited.add(m);
				if(m != null) {
					validateClear(m, visited);
				}
			}
			else if(pvType == PropertyType.RELATED_MANY) {
				final RelatedManyProperty srcRmp = (RelatedManyProperty) srcMp;
				final List<Model> srcSet = srcRmp.getModelList();
				if(srcSet != null) {
					for(final Model m : srcSet) {
						if(!visited.contains(m)) {
							visited.add(m);
							validateClear(m, visited);
						}
					}
				}
			}
		}
	}

	/**
	 * Checks for {@link Model} instance equality.
	 * @param source
	 * @param target
	 * @param compareReferences Ignore the isReference property for related model
	 *        properties?
	 * @throws Exception When a copy discrepancy is encountered
	 */
	public static void equals(final Model source, final Model target, final boolean compareReferences) throws Exception {
		compare(source, target, compareReferences, true, false, new ArrayList<Model>());
	}

	/**
	 * Compares two {@link Model} instances.
	 * <p>
	 * <em><b>IMPT: </b>This method doesn't consider those properties in the target that do <b>not</b> exist on the source.</em>
	 * @param source The source
	 * @param target The target
	 * @param compareReferences Compare nested relational properties marked as
	 *        reference?
	 * @param requireDistinctModelProperties Fail if any encountered
	 *        {@link IModelProperty} on the source is equal by memory address on
	 *        the copy?
	 * @param ignoreMissingProps
	 * @param visited
	 * @throws Exception
	 */
	private static void compare(Model source, Model target, final boolean compareReferences,
			boolean requireDistinctModelProperties, final boolean ignoreMissingProps, final List<Model> visited)
	throws Exception {
		assert source != null && target != null;

		for(final Iterator<IModelProperty> itr = source.iterator(); itr.hasNext();) {
			final IModelProperty srcProp = itr.next();
			final String propName = srcProp.getPropertyName();

			IModelProperty tgtProp = null;
			try {
				tgtProp = target.getModelProperty(propName);
			}
			catch(final PropertyPathException e) {
				if(e instanceof NullNodeInPropPathException || e instanceof UnsetPropertyException) {
					// we have a missing model prop in the copy implying it was filtered
					// via either a white or black list in the copy criteria
					if(ignoreMissingProps) continue;
				}
				throw e;
			}

			// verify like types
			TestUtils.validateEqualTypes(srcProp, tgtProp);

			if(requireDistinctModelProperties) {
				// ensure distinct memory address
				TestUtils.validateNotEqualByMemoryAddress(srcProp, tgtProp);
			}

			final PropertyType pvType = srcProp.getType();
			if(pvType.isValue()) {
				// require logical equals
				final Object srcValue = srcProp.getValue();
				final Object tgtValue = tgtProp.getValue();
				TestUtils.validateEquals(srcValue, tgtValue);
			}
			else if(pvType == PropertyType.RELATED_ONE) {
				// drill into if not already visited
				final IModelRefProperty src = (IModelRefProperty) srcProp;
				final IModelRefProperty tgt = (IModelRefProperty) tgtProp;
				final Model srcModel = src.getModel();
				final Model tgtModel = tgt.getModel();
				if(srcModel == null && tgtModel != null) {
					throw new Exception("Nested source model null but not on the target for prop: " + propName);
				}
				else if(tgtModel == null && srcModel != null) {
					// no-op
				}
				else if(tgtModel != null && srcModel != null) {
					// both are non-null
					if((compareReferences || !src.isReference())) {
						if(!visited.contains(srcModel)) {
							visited.add(srcModel);
							compare(srcModel, tgtModel, compareReferences, requireDistinctModelProperties, ignoreMissingProps,
									visited);
						}
					}
				}
				else {
					// both are null
				}
			}
			else if(pvType == PropertyType.RELATED_MANY) {
				final RelatedManyProperty src = (RelatedManyProperty) srcProp;
				final RelatedManyProperty tgt = (RelatedManyProperty) tgtProp;
				final List<Model> srcList = src.getModelList();
				final List<Model> tgtList = tgt.getModelList();

				if((srcList == null && tgtList != null) || (srcList != null && tgtList == null)) {
					throw new Exception("Related many lists differ by null for prop:" + propName);
				}
				if(srcList != null && tgtList != null) {
					if(compareReferences || !src.isReference()) {
						for(final Model srcModel : srcList) {
							// find the copied target model
							Model tgtModel = null;
							int ti = -1, index = 0;
							for(final Model tm : tgtList) {
								if(tm.getKey().equals(srcModel.getKey())) {
									ti = index;
									break;
								}
								index++;
							}
							if(ti == -1) {
								// this should only happen when we are copying in change mode
								if(!ignoreMissingProps) {
									throw new Exception("Missing related many model element for property: " + srcProp);
								}
							}
							else {
								tgtModel = tgtList.get(ti);
								assert tgtModel != null;
							}
							if(tgtModel != null && !visited.contains(srcModel)) {
								visited.add(srcModel);
								compare(srcModel, tgtModel, compareReferences, requireDistinctModelProperties, ignoreMissingProps,
										visited);
							}
						}
					}
				}
				else {
					// both are null
				}
			}
		}
	}
}
