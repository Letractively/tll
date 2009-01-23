/**
 * The Logic Lab
 * @author jpk
 * Jan 22, 2009
 */
package com.tll.config;

/**
 * EnumConfigKeyProvider - Provides config keys from an {@link Enum} that
 * implements {@link IConfigKey}.
 * @author jpk
 */
public class EnumConfigKeyProvider implements IConfigKeyProvider {

	private final String[] configKeys;

	/**
	 * Constructor
	 * @param enmDef The enum definition (class) that implements
	 *        {@link IConfigKey}.
	 */
	public EnumConfigKeyProvider(Class<? extends Enum<?>> enmDef) {
		super();
		if(enmDef == null || !enmDef.isEnum() || !IConfigKey.class.isAssignableFrom(enmDef)) {
			throw new IllegalArgumentException("A enum class implementing IConfigKey must be specified.");
		}
		Enum<?>[] enumConstants = enmDef.getEnumConstants();
		configKeys = new String[enumConstants.length];
		int i = 0;
		for(Enum<?> e : enumConstants) {
			configKeys[i++] = ((IConfigKey) e).getKey();
		}
	}

	public String[] getConfigKeys() {
		return configKeys;
	}

}
