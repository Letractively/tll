/**
 * The Logic Lab
 * @author jpk Dec 18, 2007
 */
package com.tll.client.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.testng.annotations.BeforeClass;

import com.google.inject.Module;
import com.tll.DbTest;
import com.tll.TestUtils;
import com.tll.dao.DaoMode;
import com.tll.dao.JpaMode;
import com.tll.di.DaoModule;
import com.tll.model.schema.PropertyType;

/**
 * AbstractModelTest - Base class for testing client model stuff.
 * @author jpk
 */
public abstract class AbstractModelTest extends DbTest {

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
	public AbstractModelTest() {
		super(JpaMode.NONE, false);
	}

	@Override
	protected final void addModules(List<Module> modules) {
		super.addModules(modules);
		modules.add(new DaoModule(DaoMode.MOCK));
	}

	@BeforeClass(alwaysRun = true)
	public final void onBeforeClass() {
		beforeClass();
	}
}
