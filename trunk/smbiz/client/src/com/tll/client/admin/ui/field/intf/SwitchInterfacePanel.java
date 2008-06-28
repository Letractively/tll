/**
 * The Logic Lab
 * @author jkirton
 * May 21, 2008
 */
package com.tll.client.admin.ui.field.intf;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.model.Model;
import com.tll.client.ui.FlowFieldPanelComposer;

/**
 * SwitchInterfacePanel - One option exists that is either on or off.
 * @author jpk
 */
public final class SwitchInterfacePanel extends AbstractInterfacePanel {

	/**
	 * Constructor
	 */
	public SwitchInterfacePanel() {
		super();
	}

	@Override
	protected Widget draw() {
		FlowFieldPanelComposer canvas = new FlowFieldPanelComposer();

		// first row
		canvas.addField(name);
		canvas.addField(code);
		canvas.addField(description);

		canvas.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		canvas.addField(timestamps[0]);
		canvas.stopFlow();
		canvas.addField(timestamps[1]);
		canvas.resetAlignment();

		canvas.newRow();
		canvas.addWidget(createAvailabilityGrid());

		return canvas.getCanvasWidget();
	}

	@Override
	protected void applyModel(Model modelInterface) {
		/*
		RelatedManyProperty pvOptions = modelInterface.relatedMany("options");
		if(pvOptions == null || pvOptions.size() != 1) {
			throw new IllegalArgumentException();
		}
		IndexedProperty ip = pvOptions.getIndexedProperty(0);
		assert ip != null;
		pnlOption.getFields().setPropertyName(ip.getPropertyName());
		fields.addField(pnlOption.getFields());
		pnlOption.onBeforeBind(ip.getModel());
		*/
	}
}
