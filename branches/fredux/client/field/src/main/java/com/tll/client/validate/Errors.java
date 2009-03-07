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
	 * Cache errors keyed by the source.
	 */
	private Map<IWidgetRef, List<IError>> sourced;
	
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
	 * @param field the sourcing field
	 */
	public void add(IError error, IWidgetRef field) {
		if(sourced == null) {
			sourced = new HashMap<IWidgetRef, List<IError>>();
		}
		List<IError> list = sourced.get(field);
		if(list == null) {
			list = new ArrayList<IError>();
			sourced.put(field, list);
		}
		list.add(error);
		numErrors++;
	}

	/**
	 * @return List of {@link Msg}s whose ref token is the sourcing field name or
	 *         <code>null</code> if no sourced errors exist.
	 */
	public Map<IWidgetRef, List<IError>> getSourcedErrors() {
		return sourced;
	}
	
	/**
	 * @return The number of errors irrespective of the error source.
	 */
	public int size() {
		return numErrors;
	}
}
