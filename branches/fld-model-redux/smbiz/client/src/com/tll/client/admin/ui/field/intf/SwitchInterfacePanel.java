/**
 * The Logic Lab
 * @author jkirton
 * May 21, 2008
 */
package com.tll.client.admin.ui.field.intf;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Panel;
import com.tll.client.bind.IBindable;
import com.tll.client.model.Model;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.FlowPanelFieldComposer;
import com.tll.client.ui.field.IFieldRenderer;

/**
 * SwitchInterfacePanel - One option exists that is either on or off.
 * @author jpk
 */
public final class SwitchInterfacePanel<M extends IBindable> extends AbstractInterfacePanel<M> {

	class SwitchInterfaceFieldsRenderer implements IFieldRenderer {

		public void render(Panel panel, FieldGroup fg) {
			final FlowPanelFieldComposer cmpsr = new FlowPanelFieldComposer();
			cmpsr.setCanvas(canvas);

			// first row
			cmpsr.addField(fg.getField("name"));
			cmpsr.addField(fg.getField("code"));
			cmpsr.addField(fg.getField("description"));

			cmpsr.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
			cmpsr.addField(fg.getField(Model.DATE_CREATED_PROPERTY));
			cmpsr.stopFlow();
			cmpsr.addField(fg.getField(Model.DATE_MODIFIED_PROPERTY));
			cmpsr.resetAlignment();

			cmpsr.newRow();
			cmpsr.addWidget(createAvailabilityGrid(fg));
		}
	}

	private final FlowPanel canvas = new FlowPanel();

	/**
	 * Constructor
	 */
	public SwitchInterfacePanel() {
		super();
		initWidget(canvas);
		setRenderer(new SwitchInterfaceFieldsRenderer());
	}

	@Override
	protected FieldGroup generateFieldGroup() {
		return (new InterfaceFieldProvider()).getFieldGroup();
	}

	/*
	private void applyModel(IFieldGroupModelBinding bindingDef) {
		// TODO re-impl
		final Model modelInterface = bindingDef.resolveModel(EntityType.INTERFACE);
		RelatedManyProperty pvOptions = modelInterface.relatedMany("options");
		if(pvOptions == null || pvOptions.size() != 1) {
			throw new IllegalArgumentException();
		}
		IndexedProperty ip = pvOptions.getIndexedProperty(0);
		assert ip != null;
		pnlOption.getFields().setPropertyName(ip.getPropertyName());
		getFieldGroup().addField(pnlOption.getFields());
		pnlOption.onBeforeBind(ip.getModel());
	}
		*/
}
