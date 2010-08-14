/**
 * The Logic Lab
 * @author jpk
 * Feb 24, 2009
 */
package com.tll.client.ui.test;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.IField;
import com.tll.client.ui.field.IFieldWidget;

/**
 * Views FieldGroup properties in a tree widget.
 * @author jpk
 */
public class FieldGroupViewer extends Composite {

	private static String getFieldGroupHtml(FieldGroup fg) {
		return "<span style=\"color:maroon\"><b>" + fg.getName() + "</b></span>";
	}

	private static String getFieldWidgetHtml(IFieldWidget<?> fw) {
		String propName = fw.getPropertyName();
		String text = fw.getText();
		return "<span style=\"color:gray\">" + propName + "</span>&nbsp;<span style=\"color:blue\">" + text + "</span>";
	}

	private final Panel panel;
	private final Tree tree;
	private FieldGroup fg;

	/**
	 * Constructor
	 */
	public FieldGroupViewer() {
		super();
		tree = new Tree();
		panel = new SimplePanel();
		panel.add(tree);
		initWidget(panel);
	}

	/**
	 * @param fg the field group to set
	 */
	public void setFieldGroup(FieldGroup fg) {
		if(fg == null) {
			tree.clear();
		}
		else if(this.fg != fg) {
			fillTree(fg);
		}
		this.fg = fg;
	}

	/**
	 * Fills the tree with the given model properties.
	 * @param afg a field group
	 */
	private void fillTree(FieldGroup afg) {
		assert afg != null;

		// clear out tree
		tree.clear();

		// add field group root tree item
		final TreeItem root = new TreeItem(getFieldGroupHtml(afg));
		tree.addItem(root);

		// add child fields
		for(final IField f : afg) {
			addField(f, root, new VisitedStack());
		}

		root.setState(true);
	}

	@SuppressWarnings("serial")
	static final class VisitedStack extends ArrayList<IField> {

		boolean exists(final IField field) {
			for(final IField f : this) {
				if(f == field) return true;
			}
			return false;
		}
	}

	/**
	 * Recursively adds model properties to the given parent tree item.
	 * @param field the current model property
	 * @param parent the parent tree item
	 * @param visited
	 */
	private void addField(IField field, TreeItem parent, final VisitedStack visited) {
		// check visited
		if(!visited.exists(field)) {
			visited.add(field);
		}
		else {
			return;
		}

		if(field instanceof FieldGroup) {
			final TreeItem branch = new TreeItem(getFieldGroupHtml((FieldGroup) field));
			parent.addItem(branch);
			for(IField nf : (FieldGroup)field) {
				addField(nf, branch, visited);
			}
		}
		else {
			final TreeItem branch = new TreeItem(getFieldWidgetHtml((IFieldWidget<?>) field));
			parent.addItem(branch);
		}
	}
}
