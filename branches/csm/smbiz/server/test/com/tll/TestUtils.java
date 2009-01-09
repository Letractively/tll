/**
 * The Logic Lab
 * @author jpk
 * Dec 31, 2008
 */
package com.tll;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;

import com.tll.client.model.IModelProperty;
import com.tll.client.model.Model;
import com.tll.client.model.ModelRefProperty;
import com.tll.client.model.RelatedManyProperty;
import com.tll.model.schema.PropertyType;

/**
 * TestUtils
 * @author jpk
 */
public final class TestUtils {

	public static final String LENGTH_64_STRING = "1234567890123456789012345678901234567890123456789012345678901234";

	public static final String LENGTH_65_STRING = LENGTH_64_STRING + "5";

	public static final String LENGTH_128_STRING = LENGTH_64_STRING + LENGTH_64_STRING;

	public static final String LENGTH_129_STRING = LENGTH_64_STRING + LENGTH_65_STRING;

	public static final String LENGTH_256_STRING = LENGTH_128_STRING + LENGTH_128_STRING;

	public static final String LENGTH_257_STRING = LENGTH_128_STRING + LENGTH_129_STRING;

	/**
	 * Validate the given object is empty. Handles {@link Collection}s and arrays.
	 * @param obj
	 * @throws Exception When the given object is found not empty
	 */
	public static void validateEmpty(Object obj) throws Exception {
		if(obj == null) return;
		if(obj instanceof Collection) {
			if(((Collection<?>) obj).size() > 0) {
				throw new Exception("Non-empty collection");
			}
		}
		else if(obj.getClass().isArray()) {
			final int length = Array.getLength(obj);
			if(length > 0) {
				throw new Exception("Non-empty array");
			}
		}
	}

	/**
	 * Validate the 2 objects given are equal by class type
	 * @param src
	 * @param tgt
	 * @throws Exception When not of like type
	 */
	public static void validateEqualTypes(Object src, Object tgt) throws Exception {
		if((src == null && tgt != null) || (src != null && tgt == null)) {
			throw new Exception("Types differ: one is null the other is not");
		}
		if(src != null && !tgt.getClass().equals(src.getClass()))
			throw new Exception("Types differ: src type: " + src.getClass().toString() + ", cpyValue: "
					+ tgt.getClass().toString());
	}

	/**
	 * Validate the 2 objects given are equal. When the given objects are
	 * <code>null</code>, this validation passes.
	 * @param src
	 * @param tgt
	 * @throws Exception When the given objects are found unequal
	 */
	public static void validateEquals(Object src, Object tgt) throws Exception {
		if(!ObjectUtils.equals(src, tgt))
			throw new Exception("Objects do not equal: src: " + (src == null ? "null" : src.toString()) + ", tgt: "
					+ (tgt == null ? "null" : tgt.toString()));
	}

	/**
	 * Validate the 2 objects given are NOTE equal by memory address. This
	 * validation passes when the given objects are <code>null</code>.
	 * @param src
	 * @param tgt
	 * @throws Exception When the given objects are found unequal
	 */
	public static void validateNotEqualByMemoryAddress(Object src, Object tgt) throws Exception {
		if(src != null && src == tgt)
			throw new Exception("Objects are equal by memory address: src: " + src.toString() + ", tgt: " + tgt.toString());
	}

	/**
	 * Validate the 2 objects given are locically equal AND NOT by memory address.
	 * @param src
	 * @param tgt
	 * @throws Exception When the given objects are found unequal
	 */
	public static void validateEqualsAndNotAddressEquals(Object src, Object tgt) throws Exception {
		if(src != null && src == tgt) {
			throw new Exception("Objects are equal by memory address: src: " + src.toString() + ", tgt: " + tgt.toString());
		}
		validateEquals(src, tgt);
	}

	/**
	 * Validates a source {@link Model} to a copied {@link Model} verifying the
	 * copied model's properties.
	 * @param source
	 * @param copy
	 * @param compareReferences Ignore the isReference property for related model
	 *        properties?
	 * @throws Exception When a copy discrepancy is encountered
	 */
	public static void validateCopy(final Model source, final Model copy, final boolean compareReferences)
			throws Exception {
		compare(source, copy, compareReferences, true, new ArrayList<Model>());
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
		compare(source, target, compareReferences, false, new ArrayList<Model>());
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
	 * @param visited
	 * @throws Exception
	 */
	private static void compare(Model source, Model target, final boolean compareReferences,
			boolean requireDistinctModelProperties, List<Model> visited) throws Exception {
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
					throw new Exception("Nested source model non-null but not on the target for prop: " + propName);
				}
				else if((compareReferences || !src.isReference()) && srcModel != null && !visited.contains(srcModel)) {
					visited.add(srcModel);
					compare(srcModel, tgtModel, compareReferences, requireDistinctModelProperties, visited);
				}
			}
			else if(pvType == PropertyType.RELATED_MANY) {
				final RelatedManyProperty src = (RelatedManyProperty) srcProp;
				final RelatedManyProperty tgt = (RelatedManyProperty) tgtProp;
				final Collection<Model> srcSet = src.getList();
				final Collection<Model> tgtSet = tgt.getList();

				if((srcSet == null && tgtSet != null) || (srcSet != null && tgtSet == null)) {
					throw new Exception("Related many lists differ by null for prop:" + propName);
				}
				if(srcSet != null) {
					if(srcSet.size() != tgtSet.size()) {
						throw new Exception("Source and copy related many list sizes differ for prop: " + propName);
					}
					if(compareReferences || !src.isReference()) {
						final Iterator<Model> citr = tgtSet.iterator();
						for(final Model srcPvg : srcSet) {
							final Model cpyPvg = citr.next();
							if(!visited.contains(srcPvg)) {
								visited.add(srcPvg);
								compare(srcPvg, cpyPvg, compareReferences, requireDistinctModelProperties, visited);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Constructor
	 */
	private TestUtils() {
		super();
	}

}