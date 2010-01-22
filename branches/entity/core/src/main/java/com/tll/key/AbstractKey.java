/**
 * The Logic Lab
 * @author jpk
 * @since Jan 22, 2010
 */
package com.tll.key;

/**
 * AbstractKey - Base class for {@link IKey} impls. The required key type is
 * handled here.
 * @author jpk
 */
public abstract class AbstractKey implements IKey {

	private static final long serialVersionUID = 2719644606064981088L;

	private final Class<?> type;

	/**
	 * Constructor
	 * @param type The required type to which this key refers
	 */
	public AbstractKey(Class<?> type) {
		super();
		if(type == null) throw new IllegalArgumentException("A key type must be specified.");
		this.type = type;
	}

	@Override
	public Class<?> getType() {
		return type;
	}

	@Override
	public int hashCode() {
		return 31 + ((type == null) ? 0 : type.toString().hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		AbstractKey other = (AbstractKey) obj;
		if(type == null) {
			if(other.type != null) return false;
		}
		else if(!type.equals(other.type)) return false;
		return true;
	}

}
