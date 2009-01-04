/**
 * The Logic Lab
 * @author jpk
 * Dec 29, 2008
 */
package com.tll.client.bind;

import com.tll.client.ui.IBoundWidget;
import com.tll.client.ui.field.FieldGroup;

/**
 * AbstractModelEditAction - Common base class for all concrete model edit
 * action classes in the client app.
 * @author jpk
 */
public abstract class AbstractModelEditAction<M extends IBindable, B extends IBoundWidget<M, FieldGroup, M>> implements IBindingAction<B> {

	/**
	 * The binding.
	 */
	protected final Binding binding = new Binding();

	/**
	 * The target boundWidget.
	 */
	protected B boundWidget;

	/**
	 * Constructor
	 */
	public AbstractModelEditAction() {
		super();
	}

	public void execute() {
		M m = boundWidget.getModel();
		if(binding.isValid()) {

		}
	}

	/**
	 * Responsible for filling the <code>binding</code> member property.
	 */
	protected abstract void populateBinding(B bindable);

	public final void setBindable(B bindable) {
		populateBinding(bindable);
		this.boundWidget = bindable;
	}

	public void bind() {
		binding.bind();
	}

	public void unbind() {
		binding.unbind();
		binding.getChildren().clear();
	}

}
