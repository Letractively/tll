/**
 * The Logic Lab
 * @author jpk
 * Feb 24, 2009
 */
package com.tll.client.ui.field;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.tll.client.ui.edit.EditEvent;
import com.tll.client.ui.edit.IEditListener;
import com.tll.common.model.IModelProperty;
import com.tll.common.model.IModelRefProperty;
import com.tll.common.model.Model;
import com.tll.common.model.PropertyPathException;
import com.tll.common.model.RelatedManyProperty;
import com.tll.model.schema.PropertyType;

/**
 * ModelViewer - Views model properties in a hierarchical tree widget.
 * @author jpk
 */
public class ModelViewer extends Composite implements IEditListener {

	private final ScrollPanel scrollPanel;
	private final Tree tree;
	private Model model;

	/**
	 * Constructor
	 * @param pixelWidth
	 * @param pixelHeight
	 */
	public ModelViewer(int pixelWidth, int pixelHeight) {
		super();
		tree = new Tree();
		scrollPanel = new ScrollPanel(tree);
		scrollPanel.setPixelSize(pixelWidth, pixelHeight);
		initWidget(scrollPanel);
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(Model model) {
		if(model == null) {
			tree.clear();
		}
		else if(this.model != model) {
			fillTree(model);
		}
		this.model = model;
	}

	/**
	 * Fills the tree with the given model properties.
	 * @param model
	 */
	private void fillTree(Model model) {
		assert model != null;

		// clear out tree
		tree.clear();

		// add model ref root tree item
		final TreeItem root = new TreeItem(model.toString());
		tree.addItem(root);

		// add model props..
		try {
			for(final IModelProperty prop : model) {
				addProp(prop, root, new VisitedStack());
			}
		}
		catch(final PropertyPathException e) {
			throw new IllegalStateException();
		}
	}

	@SuppressWarnings("serial")
	static final class VisitedStack extends ArrayList<Model> {

		boolean exists(final Model model) {
			for(final Model m : this) {
				if(m == model) return true;
			}
			return false;
		}
	}

	/**
	 * Recursively adds model properties to the given parent tree item.
	 * @param prop the current model property
	 * @param parent the parent tree item
	 * @param visited
	 */
	private void addProp(IModelProperty prop, TreeItem parent, final VisitedStack visited) throws PropertyPathException {
		final PropertyType ptype = prop.getType();
		if(ptype.isModelRef()) {
			// related one
			final Model m = ((IModelRefProperty) prop).getModel();
			if(m == null || !visited.exists(m)) {
				final TreeItem branch = new TreeItem(prop.toString());
				parent.addItem(branch);
				if(m != null) {
					visited.add(m);
					for(final IModelProperty nprop : m) {
						addProp(nprop, branch, visited);
					}
				}
			}
		}
		else if(ptype.isRelational()) {
			// related many
			final TreeItem branch = new TreeItem(prop.toString());
			parent.addItem(branch);
			final List<Model> list = ((RelatedManyProperty) prop).getList();
			if(list != null) {
				for(final Model m : list) {
					if(!visited.exists(m)) {
						visited.add(m);
						addProp(m.getModelProperty(null), branch, visited);
					}
				}
			}
		}
		else {
			// non-relational value property
			assert ptype.isValue() == true;
			parent.addItem(prop.toString());
		}
	}

	@Override
	public void onEditEvent(EditEvent event) {
		// re-build tree
		fillTree(model);
	}
}
