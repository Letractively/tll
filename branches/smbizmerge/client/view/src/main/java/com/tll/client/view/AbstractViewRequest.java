/**
 * The Logic Lab
 * @author jpk Jan 13, 2008
 */
package com.tll.client.view;

import com.google.gwt.user.client.Command;

/**
 * AbstractViewRequest - common base class for all {@link IViewRequest} types.
 * @author jpk
 */
public abstract class AbstractViewRequest extends AbstractViewKeyProvider implements IViewRequest {

	private final Command onCompleteCommand;

	/**
	 * Constructor
	 * @param onCompleteCommand
	 */
	protected AbstractViewRequest(Command onCompleteCommand) {
		super();
		this.onCompleteCommand = onCompleteCommand;
	}

	@Override
	public Command onCompleteCommand() {
		return onCompleteCommand;
	}

	/**
	 * @return <code>true</code> if history should be updated with a view token,
	 *         <code>false</code> if history is NOT to be updated.
	 *         <p>
	 *         Default returns <code>true</code>. Concrete impls may override.
	 */
	@Override
	public boolean addHistory() {
		return true;
	}

	@Override
	public final String toString() {
		String s = "";
		final ViewKey viewKey = getViewKey();
		if(viewKey != null) {
			s += viewKey.toString();
		}
		return s;
	}
}
