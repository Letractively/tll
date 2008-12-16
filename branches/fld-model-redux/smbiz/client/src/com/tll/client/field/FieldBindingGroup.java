/**
 * The Logic Lab
 * @author jpk
 * Dec 14, 2008
 */
package com.tll.client.field;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.tll.client.validate.ValidationException;

/**
 * FieldBindingGroup - A collection of {@link FieldBinding}s.
 * @author jpk
 */
public class FieldBindingGroup implements IFieldBinding, Iterable<FieldBinding> {

	private final Set<FieldBinding> set = new HashSet<FieldBinding>();

	/**
	 * Constructor
	 */
	public FieldBindingGroup() {
		super();
	}

	public void add(FieldBinding binding) {
		if(binding == null) throw new IllegalArgumentException("The field binding can't be null.");
		set.add(binding);
	}

	public Iterator<FieldBinding> iterator() {
		return set.iterator();
	}

	public int size() {
		return set.size();
	}

	public void bind() {
		for(FieldBinding b : set) {
			b.bind();
		}
	}

	public void unbind() {
		for(FieldBinding b : set) {
			b.unbind();
		}
	}

	public void push() {
		for(FieldBinding b : set) {
			b.push();
		}
	}

	public void pull() throws ValidationException {
		for(FieldBinding b : set) {
			b.pull();
		}
	}

	public void clear() {
		unbind();
		set.clear();
	}
}
