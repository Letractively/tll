/**
 * The Logic Lab
 * @author jpk
 * @since May 15, 2009
 */
package com.tll.server.rpc.entity.test;

import com.tll.common.model.IEntityType;
import com.tll.common.model.test.TestEntityType;
import com.tll.server.rpc.entity.IEntityTypeResolver;
import com.tll.util.EnumUtil;
import com.tll.util.StringUtil;

/**
 * TestEntityTypeResolver
 * @author jpk
 */
public class TestEntityTypeResolver implements IEntityTypeResolver {

	private static final String MODEL_PKG_TKN = "com.tll.model.";

	@Override
	public Class<?> resolveEntityClass(IEntityType entityType) throws IllegalArgumentException {
		if(entityType instanceof TestEntityType == false) throw new IllegalArgumentException("Expeceted TestEntityType");
		final TestEntityType set = (TestEntityType) entityType;
		try {
			return Class.forName(MODEL_PKG_TKN + StringUtil.enumStyleToCamelCase(set.name(), true));
		}
		catch(final ClassNotFoundException e) {
			throw new IllegalArgumentException(e);
		}
	}

	@Override
	public IEntityType resolveEntityType(Class<?> clz) throws IllegalArgumentException {
		return EnumUtil.fromString(TestEntityType.class, StringUtil.camelCaseToEnumStyle(clz.getSimpleName()));
	}

}
