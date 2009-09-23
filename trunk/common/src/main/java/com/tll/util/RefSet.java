/**
 * The Logic Lab
 * @author jpk
 * @since Sep 23, 2009
 */
package com.tll.util;

import java.util.HashSet;


/**
 * RefSet
 * @author jpk
 * @param <E> the element type
 */
public class RefSet<E> extends HashSet<E> {
	private static final long serialVersionUID = 3963114477962268237L;

	public boolean exists(E arg) {
		for(final E e : this) {
			if(e == arg) return true;
		}
		return false;
	}
}
