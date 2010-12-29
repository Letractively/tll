/**
 * The Logic Lab
 * @author jpk
 * @since May 15, 2009
 */
package com.tll.server.rpc.entity.test;

import com.tll.model.IEntityTypeResolver;
import com.tll.util.StringUtil;

/**
 * TestEntityTypeResolver
 * @author jpk
 */
public class TestEntityTypeResolver implements IEntityTypeResolver {

	private static final String MODEL_PKG_TKN = "com.tll.model.test.";

	@Override
	public Class<?> resolveEntityClass(String entityType) throws IllegalArgumentException {
		try {
			return Class.forName(MODEL_PKG_TKN + StringUtil.enumStyleToCamelCase(entityType, true));
		}
		catch(final ClassNotFoundException e) {
			throw new IllegalArgumentException(e);
		}
	}

	@Override
	public String resolveEntityType(Class<?> clz) throws IllegalArgumentException {
		return StringUtil.camelCaseToEnumStyle(clz.getSimpleName());
	}

}
