/**
 * The Logic Lab
 * @author jpk
 * Dec 29, 2008
 */
package com.tll.client.bind;

/**
 * AbstractModelEditAction - Common base class for all concrete model edit
 * action classes in the client app.
 * @author jpk
 */
public abstract class AbstractModelEditAction<B extends IBindable> implements IBindingAction<B> {

	protected final Binding binding = new Binding();

	/**
	 * Constructor
	 */
	public AbstractModelEditAction() {
		super();
	}

	public void execute() {
		// base impl no-op
	}

	/*
	public void setBindable(P fieldProvider) {
		// iterate the field group and bind to all fields
		IField f = fieldProvider.getField();
		if(f == null) throw new IllegalArgumentException("No field provided.");
		if(f instanceof FieldGroup) {

		}
		else {

		}
	}
	*/

	public void bind() {
		binding.bind();
	}

	public void unbind() {
		binding.unbind();
		binding.getChildren().clear();
	}

}
