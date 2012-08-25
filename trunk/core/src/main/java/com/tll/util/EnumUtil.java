package com.tll.util;

/**
 * Utility methods for enum classes.
 * @author jpk
 */
public final class EnumUtil {

	/**
	 * Convenience method that returns the enum element corresponding to the given
	 * ordinal and enum type.
	 * @param <E> enum type
	 * @param ordinal The enum ordinal.
	 * @param enumType The enum class.
	 * @return {@link Enum}
	 */
	public static <E extends Enum<?>> E fromOrdinal(Class<E> enumType, int ordinal) {
		for(final E enm : enumType.getEnumConstants()) {
			if(enm.ordinal() == ordinal) return enm;
		}
		throw new IllegalArgumentException("Invalid ordinal: " + ordinal + " for enum type: " + enumType.getSimpleName());
	}

	/**
	 * 
	 */
	private EnumUtil() {
	}

}
