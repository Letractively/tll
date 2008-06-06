/**
 * The Logic Lab
 * @author jkirton
 * May 21, 2008
 */
package com.tll.client.admin.ui.field.intf;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.ui.FlowFieldPanelComposer;
import com.tll.client.ui.field.DateField;
import com.tll.client.ui.field.TextField;

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
	protected Widget doInit() {
		final TextField fname = createNameEntityField();
		fields.addField(fname);

		final DateField[] ftimestamps = createTimestampEntityFields();
		fields.addFields(ftimestamps);

		// pnlOption.doInit();

		FlowFieldPanelComposer canvas = new FlowFieldPanelComposer();

		// first row
		canvas.addField(fname);
		canvas.addField(code);
		canvas.addField(description);

		canvas.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		canvas.addField(ftimestamps[0]);
		canvas.stopFlow();
		canvas.addField(ftimestamps[1]);
		canvas.resetAlignment();

		canvas.newRow();
		canvas.addWidget(createAvailabilityGrid());

		// canvas.newRow();
		// canvas.addWidget(pnlOption);

		return canvas.getWidget();
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
