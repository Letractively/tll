/**
 * The Logic Lab
 * @author jpk
 * Dec 27, 2008
 */
package com.tll.common.util;

import java.io.Serializable;
import java.util.Comparator;

/**
 * SimpleComparator
 * @author jpk
 */
@SuppressWarnings("serial")
public class SimpleComparator implements Comparator<Object>, Serializable {

	public static final SimpleComparator INSTANCE = new SimpleComparator();

	/**
	 * Constructor
	 */
	private SimpleComparator() {
		super();
	}

	@SuppressWarnings("unchecked")
	public int compare(Object o1, Object o2) {
		if(o1 instanceof Comparable && o2 instanceof Comparable) {
			return ((Comparable) o1).compareTo(o2);
		}
		if(ObjectUtil.equals(o1, o2)) {
			return 0;
		}
		else if((o1 != null) && (o2 != null)) {
			return o1.toString().compareTo(o2.toString());
		}
		else if((o1 != null) && (o2 == null)) {
			return +1;
		}
		else {
			return -1;
		}
	}
}
