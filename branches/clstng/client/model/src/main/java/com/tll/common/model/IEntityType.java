/**
 * The Logic Lab
 * @author jpk
 * Feb 10, 2009
 */
package com.tll.common.model;

import com.tll.IMarshalable;
import com.tll.common.util.StringUtil;


/**
 * IEntityType - Generic way of identifying a particular entity type capable of
 * being resolved to a single {@link Class} that identifies the same entity
 * type.
 * @author jpk
 */
public interface IEntityType extends IMarshalable {
	
	/**
	 * Util
	 * @author jpk
	 */
	public static class Util {

		/**
		 * Converts an {@link IEntityType} to an enum of a given type (hopefully).
		 * @param enumType The enum type
		 * @param et The {@link IEntityType}
		 * @param <E> The generic enum type
		 * @return {@link Enum} instance
		 */
		public static <E extends Enum<E>> E toEnum(Class<E> enumType, IEntityType et) {
			final PropertyPath p = new PropertyPath(et.getEntityClassName());
			final String es = StringUtil.camelCaseToEnumStyle(p.last());
			return Enum.valueOf(enumType, es);
		}
	}

	/**
	 * This method serves as a way to resolve {@link IEntityType} instances to
	 * entity {@link Class} instances.
	 * @return Fully qualified class name of the referenced entity.
	 */
	String getEntityClassName();
	
	/**
	 * @return A presentation worthy name.
	 */
	String getPresentationName();
}
