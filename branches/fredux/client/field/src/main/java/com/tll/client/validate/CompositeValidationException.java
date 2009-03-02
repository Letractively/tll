/**
 * The Logic Lab
 * @author jpk
 * Mar 2, 2009
 */
package com.tll.client.validate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tll.client.ui.field.IField;
import com.tll.common.msg.Msg;


/**
 * CompositeValidationException - Use when there are both sourced and un-sourced validation errors.
 * @author jpk
 */
@SuppressWarnings("serial")
public class CompositeValidationException extends ValidationException {

	/**
	 * Cache of sourced errors.
	 */
	private Map<IField, List<Msg>> sourced;

	/**
	 * Constructor
	 */
	public CompositeValidationException() {
		super((List<Msg>)null);
	}
	
	/**
	 * Add sourced errors.
	 * @param msgs the errors
	 * @param field the sourcing field
	 */
	public void add(List<Msg> msgs, IField field) {
		if(sourced == null) {
			sourced = new HashMap<IField, List<Msg>>();
		}
		List<Msg> list = sourced.get(field);
		if(list == null) {
			list = new ArrayList<Msg>();
			sourced.put(null, list);
		}
		list.addAll(msgs);
	}
	
	@Override
	public Type getType() {
		return Type.SOURCED;
	}

	@Override
	public List<Msg> getErrors() {
		if(sourced == null || sourced.size() < 1) return null;
		final List<Msg> rlist = new ArrayList<Msg>();
		for(final IField f : sourced.keySet()) {
			for(final Msg m : sourced.get(f)) {
				m.setRefToken(f.getName());
				rlist.add(m);
			}
		}
		return rlist;
	}

	/**
	 * @return List of {@link Msg}s whose ref token is the sourcing field name or
	 *         <code>null</code> if no sourced errors exist.
	 */
	public Map<IField, List<Msg>> getSourcedErrors() {
		return sourced;
	}
}
