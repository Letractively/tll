/**
 * The Logic Lab
 * @author jpk
 * Feb 24, 2009
 */
package com.tll.client.ui.field;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.tll.client.util.Fmt;
import com.tll.client.util.GlobalFormat;
import com.tll.common.model.IModelProperty;
import com.tll.common.model.IModelRefProperty;
import com.tll.common.model.IPropertyValue;
import com.tll.common.model.IndexedProperty;
import com.tll.common.model.Model;
import com.tll.common.model.PropertyPathException;
import com.tll.common.model.RelatedManyProperty;
import com.tll.model.schema.PropertyType;

/**
 * ModelViewer - Views model properties in a hierarchical tree widget.
 * @author jpk
 */
public class ModelViewer extends Composite {

	private final Panel panel;
	private final Tree tree;
	private Model model;

	/**
	 * Constructor
	 */
	public ModelViewer() {
		super();
		tree = new Tree();
		panel = new SimplePanel();
		panel.add(tree);
		initWidget(panel);
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
		final TreeItem root;
		try {
			root = new TreeItem(getModelRefHtml((IModelRefProperty) model.getModelProperty(null)));
		}
		catch(final PropertyPathException e) {
			throw new Error(e);
		}
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
		
		root.setState(true);
	}

	@SuppressWarnings("serial")
	static final class VisitedStack extends ArrayList<IModelProperty> {

		boolean exists(final IModelProperty prop) {
			for(final IModelProperty p : this) {
				if(p == prop) return true;
			}
			return false;
		}
	}
	
	private String getPropValueHtml(IPropertyValue p) {
		String sval;
		if(p.getValue() == null) {
			sval = "-";
		}
		else if(p.getType() == PropertyType.DATE) {
			sval = Fmt.format(p.getValue(), GlobalFormat.TIMESTAMP);
		}
		else {
			sval = p.getValue().toString();
		}
		return "<span style=\"color:gray\">" + p.getPropertyName() + "</span>&nbsp;<span style=\"color:blue\">"
				+ sval + "</span>";
	}

	private String getModelRefHtml(IModelRefProperty p) {
		String sval;
		if(p.getModel() == null) {
			sval = "-";
		}
		else {
			sval = p.getModel().toString();
		}
		if(p.getType() == PropertyType.INDEXED) {
			return "<span style=\"color:green\"><b>[" + ((IndexedProperty) p).getIndex()
					+ "]</b></span>&nbsp;<span style=\"color:gray\">" + sval + "</span>";
		}
		// related one
		if(p.getPropertyName() == null) {
			// presume the root model
			return "<b>" + sval + "</b>";
		}
		return "<span style=\"color:maroon\"><b>" + p.getPropertyName() + "</b></span>&nbsp;<span style=\"color:gray\">"
		+ sval
		+ "</span>";
	}

	private String getModelCollectionHtml(RelatedManyProperty p) {
		String sval;
		if(p.getList() == null) {
			sval = "-";
		}
		else {
			sval = "(" + p.size() + ")";
		}
		return "<span style=\"color:darkgreen\"><b>" + p.getPropertyName() + "</b>&nbsp;<span style=\"color:gray\">" + sval
				+ "</span>";
	}

	/**
	 * Recursively adds model properties to the given parent tree item.
	 * @param prop the current model property
	 * @param parent the parent tree item
	 * @param visited
	 */
	private void addProp(IModelProperty prop, TreeItem parent, final VisitedStack visited) throws PropertyPathException {
		// check visited
		if(!visited.exists(prop)) {
			visited.add(prop);
		}
		else {
			return;
		}
		
		final PropertyType ptype = prop.getType();
		if(ptype.isModelRef()) {
			// related one
			final TreeItem branch = new TreeItem(getModelRefHtml((IModelRefProperty) prop));
			parent.addItem(branch);
			final Model m = ((IModelRefProperty) prop).getModel();
			if(m != null) {
				for(final IModelProperty nprop : m) {
					addProp(nprop, branch, visited);
				}
			}
		}
		else if(ptype.isRelational()) {
			// related many
			final TreeItem branch = new TreeItem(getModelCollectionHtml((RelatedManyProperty) prop));
			parent.addItem(branch);
			for(final IndexedProperty ip : (RelatedManyProperty) prop) {
				addProp(ip, branch, visited);
			}
		}
		else {
			// non-relational value property
			assert ptype.isValue() == true;
			// don't enumerate the id property (its redundant)
			if(!Model.ID_PROPERTY.equals(prop.getPropertyName())) {
				parent.addItem(getPropValueHtml((IPropertyValue) prop));
			}
		}
	}
}
