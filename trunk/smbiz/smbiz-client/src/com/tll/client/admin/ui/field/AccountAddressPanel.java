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
import com.tll.client.field.IField;
import com.tll.client.ui.CSS;
import com.tll.client.ui.field.FieldGroupPanel;
import com.tll.client.ui.field.FieldPanel;
import com.tll.client.ui.field.TextField;
import com.tll.model.impl.AddressType;

/**
 * AccountAddressPanel
 * @author jpk
 */
public class AccountAddressPanel extends FieldGroupPanel implements ClickListener {

	static final String ID_ACCOUNT_ADDRESS_DELETE_PREFIX = "dlte_aa_";

	protected final AddressType addressType;
	protected int propValIndex = -1;
	protected TextField name;
	// protected final Image imgDeleteToggle = new Image();
	protected final PushButton btnDeleteToggle;
	protected final AddressPanel addressPanel;

	/**
	 * Constructor
	 * @param propName
	 * @param addressType
	 * @param propValIndex
	 */
	public AccountAddressPanel(String propName, AddressType addressType, int propValIndex) {
		super(propName, addressType.getName());

		this.addressType = addressType;
		this.propValIndex = propValIndex;

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

	public int getPropValIndex() {
		return propValIndex;
	}

	public void setPropValIndex(int propValIndex) {
		this.propValIndex = propValIndex;
	}

	@Override
	protected void configure() {
		name = ftext("name", "Name", IField.LBL_ABOVE, 20);

		fields.addField(name);
		addressPanel.configure();
		fields.addField(addressPanel.getFields());

		setMarkDeleted(false);

		FieldPanel frow;

		// account address name row
		frow = new FieldPanel(IField.CSS_FIELD_ROW);
		frow.add(name);
		frow.add(btnDeleteToggle);
		add(frow);

		// address row
		frow = new FieldPanel(IField.CSS_FIELD_ROW);
		frow.add(addressPanel);
		add(frow);
	}

	private void setMarkDeleted(boolean markDeleted) {
		getFields().setMarkedDeleted(markDeleted);
		if(markDeleted) {
			// App.imgs().undo().applyTo(imgDeleteToggle);
			btnDeleteToggle.getUpFace().setImage(App.imgs().undo().createImage());
			btnDeleteToggle.setTitle("Un-delete " + addressType.getName() + " Address");
		}
		else {
			// App.imgs().delete().applyTo(imgDeleteToggle);
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
