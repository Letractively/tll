/**
 * The Logic Lab
 * @author jpk
 * @since May 15, 2009
 */
package com.tll.server.rpc.entity;

import com.tll.common.model.IEntityType;
import com.tll.common.model.SmbizEntityType;
import com.tll.util.EnumUtil;
import com.tll.util.StringUtil;


/**
 * SmbizEntityTypeResolver
 * @author jpk
 */
public class SmbizEntityTypeResolver implements IEntityTypeResolver {

	private static final String MODEL_PKG_TKN = "com.tll.model.";

	@Override
	public Class<?> resolveEntityClass(IEntityType entityType) throws IllegalArgumentException {
		if(entityType instanceof SmbizEntityType == false) throw new IllegalArgumentException("Expeceted SmbizEntityType");
		final SmbizEntityType set = (SmbizEntityType) entityType;
		try {
			return Class.forName(MODEL_PKG_TKN + StringUtil.enumStyleToCamelCase(set.name(), true));
		}
		catch(final ClassNotFoundException e) {
			throw new IllegalArgumentException(e);
		}
	}

	@Override
	public IEntityType resolveEntityType(Class<?> clz) throws IllegalArgumentException {
		return EnumUtil.fromString(SmbizEntityType.class, StringUtil.camelCaseToEnumStyle(clz.getSimpleName()));
	}

}
