/**
 * The Logic Lab
 * @author jkirton
 * May 21, 2008
 */
package com.tll.client.admin.ui.field.intf;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
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
	protected void doInit() {
		super.doInit();

		// pnlOption.doInit();

		FlowFieldPanelComposer canvas = new FlowFieldPanelComposer(panel);

		// first row
		canvas.addField(name);
		canvas.addField(code);
		canvas.addField(description);

		canvas.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		canvas.addField(dateCreated);
		canvas.stopFlow();
		canvas.addField(dateModified);
		canvas.resetAlignment();

		canvas.newRow();
		canvas.addWidget(createAvailabilityGrid());

		// canvas.newRow();
		// canvas.addWidget(pnlOption);
	}

	/*
	@Override
	protected void onBeforeBind(Model modelInterface) {
		super.onBeforeBind(modelInterface);

		RelatedManyProperty pvOptions = modelInterface.relatedMany("options");
		if(pvOptions == null || pvOptions.size() != 1) {
			throw new IllegalArgumentException();
		}
		IndexedProperty ip = pvOptions.getIndexedProperty(0);
		assert ip != null;
		pnlOption.getFields().setPropertyName(ip.getPropertyName());
		fields.addField(pnlOption.getFields());
		pnlOption.onBeforeBind(ip.getModel());
	}
	*/
}
