package com.tll.client.model;

import java.util.ArrayList;

/**
 * BindingStack
 * @author jpk
 */
@SuppressWarnings("serial")
final class BindingStack<B extends PropBinding> extends ArrayList<B> {

	/**
	 * Locates an {@link Binding} given a model ref.
	 * @param model The sought model in this list of bindings
	 * @return The containing binding or <code>null</code> if not present.
	 */
	PropBinding find(final Model model) {
		for(final PropBinding b : this) {
			if(b.model == model) return b;
		}
		return null;
	}
}