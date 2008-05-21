/**
 * The Logic Lab
 * @author jkirton
 * May 21, 2008
 */
package com.tll.client.admin.ui.field;

import com.tll.client.model.IndexedProperty;
import com.tll.client.model.Model;
import com.tll.client.model.RelatedManyProperty;

/**
 * InterfaceSinglePanel
 * @author jpk
 */
public final class InterfaceSinglePanel extends InterfaceMultiOptionPanel {

	/**
	 * The single un-deletable switch option
	 */
	private final InterfaceOptionPanel pnlOption = new InterfaceOptionPanel(null);

	/**
	 * Constructor
	 * @param propName
	 */
	public InterfaceSinglePanel(String propName) {
		super(propName);
	}

	@Override
	protected void configure() {
		super.configure();
		pnlOption.configure();
		add(pnlOption);
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
