/**
 * The Logic Lab
 * @author jpk
 * Dec 14, 2008
 */
package com.tll.client.field;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.tll.client.event.IFieldBindingListener;
import com.tll.client.event.ISourcesFieldBindingEvents;
import com.tll.client.validate.ValidationException;

/**
 * FieldBindingGroup - A collection of {@link FieldBinding}s.
 * @author jpk
 */
public class FieldBindingGroup implements IFieldBinding, Iterable<FieldBinding>, ISourcesFieldBindingEvents {

	private final Set<FieldBinding> set = new HashSet<FieldBinding>();

	private FieldBindingListenerCollection bindingListeners;

	/**
	 * Constructor
	 */
	public FieldBindingGroup() {
		super();
	}

	public void addFieldBindingListener(IFieldBindingListener listener) {
		if(bindingListeners == null) {
			bindingListeners = new FieldBindingListenerCollection();
		}
		bindingListeners.add(listener);
	}

	public void removeFieldBindingListener(IFieldBindingListener listener) {
		if(bindingListeners != null) {
			bindingListeners.remove(listener);
		}
	}

	public void add(FieldBinding binding) {
		set.add(binding);
	}

	public Iterator<FieldBinding> iterator() {
		return set.iterator();
	}

	public int size() {
		return set.size();
	}

	public void bind() {
		if(bindingListeners != null) bindingListeners.fireBeforeBindEvent();
		for(FieldBinding b : set) {
			b.bind();
		}
		if(bindingListeners != null) bindingListeners.fireAfterBindEvent();
	}

	public void unbind() {
		if(bindingListeners != null) bindingListeners.fireBeforeUnbindEvent();
		for(FieldBinding b : set) {
			b.unbind();
		}
		if(bindingListeners != null) bindingListeners.fireAfterUnbindEvent();
	}

	public Object validate(Object value) {
		for(FieldBinding b : set) {
			try {
				b.validate(b.getField().getValue());
			}
			catch(ValidationException e) {
				// no-op
			}
		}
		return null;
	}
}
