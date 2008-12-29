package com.tll.client.bind;

/**
 * IBindingAction - "Controls" a given component participating in a binding.
 * <p>
 * <em><b>IMPT NOTE: </b>This code was originally derived from the <a href="http://gwittir.googlecode.com/">gwittir</a> project.</em> @author
 * jpk
 */
public interface IBindingAction<B extends IBindable> extends IAction {

	/**
	 * Sets the bindable.
	 * @param bindable The bindable to set
	 */
	void setBindable(B bindable);

	/**
	 * Establishes the bindings. A prior call to {@link #setBindable(IBindable)}
	 * is required.
	 */
	void bind();

	/**
	 * Unbinds what is currently bound performing necessary clean up.
	 */
	void unbind();
}
