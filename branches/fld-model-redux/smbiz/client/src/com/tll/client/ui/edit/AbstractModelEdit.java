/**
 * The Logic Lab
 * @author jpk
 * Dec 28, 2008
 */
package com.tll.client.ui.edit;

import com.tll.client.bind.IBindable;
import com.tll.client.bind.IBindingAction;
import com.tll.client.model.Model;
import com.tll.client.model.PropertyPathException;
import com.tll.client.ui.AbstractBoundWidget;

/**
 * AbstractModelEdit
 * @author jpk
 */
public abstract class AbstractModelEdit<A extends IBindingAction<IBindable>> extends AbstractBoundWidget<Model, Model, A, Model> {

	/**
	 * The model subject to editing.
	 */
	private Model model;

	/**
	 * Constructor
	 * @param model The model subject to edit
	 */
	public AbstractModelEdit(Model model) {
		super();
		setModel(model);
	}

	public final Model getValue() {
		return getModel();
	}

	public final void setValue(Model value) {
		setModel(value);
	}

	public Object getProperty(String propPath) throws PropertyPathException {
		return null;
	}

	public void setProperty(String propPath, Object value) throws PropertyPathException {
	}

}
