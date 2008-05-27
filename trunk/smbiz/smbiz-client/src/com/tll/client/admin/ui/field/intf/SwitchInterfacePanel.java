/**
 * The Logic Lab
 * @author jkirton
 * May 21, 2008
 */
package com.tll.client.admin.ui.field.intf;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.tll.client.model.IndexedProperty;
import com.tll.client.model.Model;
import com.tll.client.model.RelatedManyProperty;
import com.tll.client.ui.FlowFieldPanelComposer;

/**
 * SwitchInterfacePanel - One option exists that is either on or off.
 * @author jpk
 */
public final class SwitchInterfacePanel extends AbstractInterfacePanel {

	/**
	 * The single un-deletable switch option
	 */
	private final SwitchOptionPanel pnlOption = new SwitchOptionPanel(null);

	/**
	 * Constructor
	 * @param propName
	 */
	public SwitchInterfacePanel(String propName) {
		super(propName);
	}

	@Override
	protected void configure() {
		super.configure();

		pnlOption.configure();

		FlowFieldPanelComposer canvas = new FlowFieldPanelComposer(panel);

		// first row
		canvas.addField(name);
		canvas.addField(code);
		canvas.addField(description);

		canvas.addWidget(createAvailabilityGrid());

		canvas.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		canvas.addField(dateCreated);
		canvas.stopFlow();
		canvas.addField(dateModified);
		canvas.resetAlignment();

		canvas.newRow();
		canvas.addWidget(pnlOption);
	}

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

}
