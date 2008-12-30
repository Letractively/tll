/**
 * The Logic Lab
 * @author jkirton
 * May 21, 2008
 */
package com.tll.client.admin.ui.field.intf;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.tll.client.ui.field.FlowFieldPanelComposer;

/**
 * SwitchInterfacePanel - One option exists that is either on or off.
 * @author jpk
 */
public final class SwitchInterfacePanel<M> extends AbstractInterfacePanel<M> {

	private final FlowPanel canvas = new FlowPanel();

	/**
	 * Constructor
	 */
	public SwitchInterfacePanel() {
		super();
		initWidget(canvas);
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

	@Override
	protected void draw() {
		final FlowFieldPanelComposer cmpsr = new FlowFieldPanelComposer();
		cmpsr.setCanvas(canvas);

		// first row
		cmpsr.addField(name);
		cmpsr.addField(code);
		cmpsr.addField(description);

		cmpsr.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		cmpsr.addField(timestamps[0]);
		cmpsr.stopFlow();
		cmpsr.addField(timestamps[1]);
		cmpsr.resetAlignment();

		cmpsr.newRow();
		cmpsr.addWidget(createAvailabilityGrid());
	}
}
