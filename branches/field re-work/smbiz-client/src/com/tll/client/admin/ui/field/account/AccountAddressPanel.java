/**
 * The Logic Lab
 * @author jpk Dec 17, 2007
 */
package com.tll.client.admin.ui.field.account;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.App;
import com.tll.client.admin.ui.field.AddressPanel;
import com.tll.client.data.AuxDataRequest;
import com.tll.client.ui.CSS;
import com.tll.client.ui.field.NamedTimeStampEntityPanel;
import com.tll.model.impl.AddressType;

/**
 * AccountAddressPanel
 * @author jpk
 */
public final class AccountAddressPanel extends NamedTimeStampEntityPanel implements ClickListener {

	private final AddressType addressType;
	private final PushButton btnDeleteToggle;
	private final AddressPanel addressPanel;

	/**
	 * Constructor
	 * @param propName
	 * @param addressType
	 */
	public AccountAddressPanel(String propName, AddressType addressType) {
		super(propName, addressType.getName());

		this.addressType = addressType;

		// delete img btn
		btnDeleteToggle = new PushButton();
		btnDeleteToggle.addClickListener(this);
		btnDeleteToggle.addStyleName(CSS.FLOAT_RIGHT);

		addressPanel = new AddressPanel("address");
	}

	public AddressType getAddressType() {
		return addressType;
	}

	@Override
	public void neededAuxData(AuxDataRequest auxDataRequest) {
		// none
	}

	@Override
	protected void doInitFields() {
		super.doInitFields();

		addressPanel.initFields();
		fields.addField(addressPanel.getFields());

		/*
		FlowFieldPanelComposer canvas = new FlowFieldPanelComposer(panel);

		// account address name row
		canvas.addField(name);
		canvas.addWidget(btnDeleteToggle);

		// address row
		canvas.newRow();
		canvas.addWidget(addressPanel);
		*/
	}

	private void setMarkDeleted(boolean markDeleted) {
		getFields().setMarkedDeleted(markDeleted);
		if(markDeleted) {
			btnDeleteToggle.getUpFace().setImage(App.imgs().undo().createImage());
			btnDeleteToggle.setTitle("Un-delete " + addressType.getName() + " Address");
		}
		else {
			btnDeleteToggle.getUpFace().setImage(App.imgs().delete().createImage());
			btnDeleteToggle.setTitle("Delete " + addressType.getName() + " Address");
		}
	}

	public void onClick(Widget sender) {
		if(sender == btnDeleteToggle) {
			setMarkDeleted(!getFields().isMarkedDeleted());
		}
	}

}
