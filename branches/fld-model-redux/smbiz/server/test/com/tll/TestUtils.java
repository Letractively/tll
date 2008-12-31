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
	 * @throws Exception When a copy discrepancy is encountered
	 */
	public static void validateCopy(final Model source, final Model copy) throws Exception {
		validateCopy(source, copy, new ArrayList<Model>());
	}

	/**
	 * Called by {@link #validateCopy(Model, Model)} and enables recursion.
	 */
	private static void validateCopy(final Model source, final Model copy, final List<Model> visited) throws Exception {
		assert source != null && copy != null;

		for(final Iterator<IModelProperty> itr = source.iterator(); itr.hasNext();) {
			final IModelProperty srcPv = itr.next();
			final String propName = srcPv.getPropertyName();
			final IModelProperty cpyPv = copy.getModelProperty(propName);

			// verify like types
			TestUtils.validateEqualTypes(srcPv, cpyPv);

			// ensure the copy is not the source!
			TestUtils.validateNotEqualByMemoryAddress(srcPv, cpyPv);

			final PropertyType pvType = srcPv.getType();
			if(pvType.isValue()) {
				// require logical equals
				final Object srcValue = srcPv.getValue();
				final Object cpyValue = cpyPv.getValue();
				TestUtils.validateEquals(srcValue, cpyValue);
			}
			else if(pvType == PropertyType.RELATED_ONE) {
				// drill into if not already visited
				final ModelRefProperty srcGpv = (ModelRefProperty) srcPv;
				final ModelRefProperty cpyGpv = (ModelRefProperty) cpyPv;
				final Model srcPvg = srcGpv.getModel();
				final Model cpyPvg = cpyGpv.getModel();
				if(srcPvg == null && cpyPvg != null) {
					throw new Exception("Source prop val group found null but not the copy!");
				}
				else if(!srcGpv.isReference() && srcPvg != null && !visited.contains(srcPvg)) {
					visited.add(srcPvg);
					validateCopy(srcPvg, cpyPvg, visited);
				}
			}
			else if(pvType == PropertyType.RELATED_MANY) {
				final RelatedManyProperty srcGlpv = (RelatedManyProperty) srcPv;
				final RelatedManyProperty cpyGlpv = (RelatedManyProperty) cpyPv;
				final List<Model> srcList = srcGlpv.getList();
				final List<Model> cpyList = cpyGlpv.getList();

				if((srcList == null && cpyList != null) || (srcList != null && cpyList == null)) {
					throw new Exception("Source and copy group list property values differ by null");
				}
				if(srcList != null) {
					if(srcList.size() != cpyList.size()) {
						throw new Exception("Source and copy group list property sizes differ");
					}
					if(!srcGlpv.isReference()) {
						final Iterator<Model> citr = cpyList.iterator();
						for(final Model srcPvg : srcList) {
							final Model cpyPvg = citr.next();
							if(!visited.contains(srcPvg)) {
								visited.add(srcPvg);
								validateCopy(srcPvg, cpyPvg, visited);
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
