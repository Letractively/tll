/**
 * The Logic Lab
 * @author jpk
 * Mar 4, 2009
 */
package com.tll.client.validate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tll.client.ui.IWidgetRef;
import com.tll.common.msg.Msg;


/**
 * Errors
 * @author jpk
 */
public class Errors implements IError {

	/**
	 * The cache of sourced errors.
	 */
	private Map<IWidgetRef, List<IError>> sourced;

	/**
	 * The cache of un-sourced errors.
	 */
	private List<IError> unsourced;

	/**
	 * The total number of errors with no regard to the source.
	 */
	private int numErrors;

	@Override
	public Type getType() {
		return Type.COMPOSITE;
	}

	/**
	 * Add sourced errors.
	 * @param error the errors
	 * @param source the source of the error. May be <code>null</code> in which
	 *        case this error is cached as un-sourced otherwise it is cached as
	 *        sourced.
	 */
	public void add(IError error, IWidgetRef source) {
		if(error == null) throw new IllegalArgumentException();
		if(source == null) {
			if(unsourced == null) {
				unsourced = new ArrayList<IError>();
			}
			unsourced.add(error);
		}
		else {
			if(sourced == null) {
				sourced = new HashMap<IWidgetRef, List<IError>>();
			}
			List<IError> list = sourced.get(source);
			if(list == null) {
				list = new ArrayList<IError>();
				sourced.put(source, list);
			}
			list.add(error);
		}
		numErrors++;
	}

	/**
	 * @return List of {@link Msg}s whose ref token is the sourcing field name or
	 *         <code>null</code> if no sourced errors exist.
	 */
	public Map<IWidgetRef, List<IError>> getSourcedErrors() {
		return sourced;
	}
	
	public List<IError> getUnsourcedErrors() {
		return unsourced;
	}

	/**
	 * @return The number of sourced and un-sourced errors.
	 */
	public int size() {
		return numErrors;
	}
}
