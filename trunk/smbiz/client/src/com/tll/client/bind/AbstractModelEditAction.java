/**
 * The Logic Lab
 * @author jpk
 * Dec 29, 2008
 */
package com.tll.client.bind;

import com.tll.client.ui.field.FieldPanel;
import com.tll.client.validate.ValidationFeedbackManager;

/**
 * AbstractModelEditAction - Common base class for all concrete model edit
 * action classes in the client app.
 * @param <M> the model type
 * @param <FP> the field panel type
 * @author jpk
 */
public abstract class AbstractModelEditAction<M extends IBindable, FP extends FieldPanel<M>> implements IBindingAction<FP> {

	/**
	 * The binding.
	 */
	protected final Binding binding = new Binding();

	public void execute() {
		// TODO figure out
	}

	public final void setBindable(FP fieldPanel) {
		populateBinding(fieldPanel);
	}

	public void bind() {
		binding.bind();
	}

	public void unbind() {
		binding.unbind();
		binding.getChildren().clear();
	}

	/**
	 * Responsible for filling the <code>binding</code> member property.
	 */
	protected abstract void populateBinding(FP bindable);

	/**
	 * Adds a binding.
	 * @param fieldPanel The field panel
	 * @param property The common property path that resolves the target property
	 *        for both the model and the field panel's field group
	 */
	protected void addBinding(FP fieldPanel, String property) {
		binding.getChildren().add(
				new Binding(fieldPanel, fieldPanel.getFieldGroup().getField(property), ValidationFeedbackManager.instance(),
						property));
	}

}
