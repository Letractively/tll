/**
 * The Logic Lab
 * @author jpk Feb 12, 2009
 */
package com.tll.common.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.tll.client.TestUtils;
import com.tll.model.schema.PropertyType;

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
	 * @param compareReferences Ignore the isReference property for related model
	 *        properties?
	 * @param copyMarkedDeletedFlag Whether or not the copy copied nested models
	 *        that are marked deleted
	 * @throws Exception When a copy discrepancy is encountered
	 */
	public static void validateCopy(final Model source, final Model copy, final boolean compareReferences,
			boolean copyMarkedDeletedFlag) throws Exception {
		compare(source, copy, compareReferences, copyMarkedDeletedFlag, true, new ArrayList<Model>());
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
				final ModelRefProperty srcMrp = (ModelRefProperty) srcMp;
				final Model m = srcMrp.getModel();
				visited.add(m);
				if(m != null) {
					validateClear(m, visited);
				}
			}
			else if(pvType == PropertyType.RELATED_MANY) {
				final RelatedManyProperty srcRmp = (RelatedManyProperty) srcMp;
				final List<Model> srcSet = srcRmp.getList();
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
	 * @param copyMarkedDeletedFlag
	 * @param requireDistinctModelProperties Fail if any encountered
	 *        {@link IModelProperty} on the source is equal by memory address on
	 *        the copy?
	 * @param visited
	 * @throws Exception
	 */
	private static void compare(Model source, Model target, final boolean compareReferences,
			final boolean copyMarkedDeletedFlag, boolean requireDistinctModelProperties, List<Model> visited)
			throws Exception {
		assert source != null && target != null;

		for(final Iterator<IModelProperty> itr = source.iterator(); itr.hasNext();) {
			final IModelProperty srcProp = itr.next();
			final String propName = srcProp.getPropertyName();
			final IModelProperty tgtProp = target.getModelProperty(propName);

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
				final ModelRefProperty src = (ModelRefProperty) srcProp;
				final ModelRefProperty tgt = (ModelRefProperty) tgtProp;
				final Model srcModel = src.getModel();
				final Model tgtModel = tgt.getModel();
				if(srcModel == null && tgtModel != null) {
					throw new Exception("Nested source model null but not on the target for prop: " + propName);
				}
				else if(tgtModel == null && srcModel != null) {
					if(copyMarkedDeletedFlag) {
						throw new Exception("Nested source model non-null but not on the target for prop: " + propName);
					}
				}
				else if(tgtModel != null && srcModel != null) {
					// both are non-null
					if(!copyMarkedDeletedFlag && srcModel.isMarkedDeleted()) {
						throw new Exception("Nested source model non-null and target non-null for model for prop: " + propName
								+ " but the source was marked as deleted");
					}
					if((compareReferences || !src.isReference())) {
						if(!visited.contains(srcModel)) {
							visited.add(srcModel);
							compare(srcModel, tgtModel, compareReferences, copyMarkedDeletedFlag, requireDistinctModelProperties,
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
				final List<Model> srcList = src.getList();
				final List<Model> tgtList = tgt.getList();

				if((srcList == null && tgtList != null) || (srcList != null && tgtList == null)) {
					throw new Exception("Related many lists differ by null for prop:" + propName);
				}
				if(srcList != null && tgtList != null) {
					if(copyMarkedDeletedFlag && srcList.size() != tgtList.size()) {
						throw new Exception("Source and copy related many list sizes differ for prop: " + propName);
					}
					if(compareReferences || !src.isReference()) {
						for(final Model srcModel : srcList) {
							// find the copied target model
							Model tgtModel = null;
							// NOTE: we can rely on this since we are doing a logical equals as Model overrides equals()/hashCode()
							final int ti = tgtList.indexOf(srcModel);
							if(ti == -1) {
								// this should only happen when we aren't copying models marked as deleted
								if(copyMarkedDeletedFlag) {
									throw new Exception("Missing related many model element for property: " + srcProp);
								}
							}
							else {
								tgtModel = tgtList.get(ti);
								assert tgtModel != null;
							}
							if(tgtModel != null && !visited.contains(srcModel)) {
								visited.add(srcModel);
								compare(srcModel, tgtModel, compareReferences, copyMarkedDeletedFlag, requireDistinctModelProperties,
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
