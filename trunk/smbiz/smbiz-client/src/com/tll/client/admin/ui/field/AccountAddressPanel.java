/**
 * The Logic Lab
 * @author jpk Dec 17, 2007
 */
package com.tll.client.admin.ui.field;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.App;
import com.tll.client.data.AuxDataRequest;
import com.tll.client.ui.CSS;
import com.tll.client.ui.FlowFieldCanvas;
import com.tll.client.ui.field.NamedTimeStampEntityPanel;
import com.tll.model.impl.AddressType;

/**
 * AccountAddressPanel
 * @author jpk
 */
public class AccountAddressPanel extends NamedTimeStampEntityPanel implements ClickListener {

	protected final AddressType addressType;
	protected final PushButton btnDeleteToggle;
	protected final AddressPanel addressPanel;

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
	protected void neededAuxData(AuxDataRequest auxDataRequest) {
		// none
	}

	@Override
	protected void configure() {
		super.configure();

		addressPanel.configure();
		fields.addField(addressPanel.getFields());

		// TODO determine why we need this as we shouldn't!!!
		setMarkDeleted(false);

		FlowFieldCanvas canvas = new FlowFieldCanvas(panel);

		// account address name row
		canvas.addField(name);
		canvas.addWidget(btnDeleteToggle);

		// address row
		canvas.newRow();
		canvas.addWidget(addressPanel);
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
