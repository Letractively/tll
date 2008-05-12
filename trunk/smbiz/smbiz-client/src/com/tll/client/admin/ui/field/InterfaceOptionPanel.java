/**
 * The Logic Lab
 * @author jpk
 * Feb 24, 2008
 */
package com.tll.client.admin.ui.field;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.App;
import com.tll.client.admin.ui.listing.InterfaceOptionParamListingConfig;
import com.tll.client.event.IEditListener;
import com.tll.client.event.type.EditEvent;
import com.tll.client.event.type.EditEvent.EditOp;
import com.tll.client.field.FieldGroup;
import com.tll.client.field.IField;
import com.tll.client.field.IField.LabelMode;
import com.tll.client.listing.IAddRowDelegate;
import com.tll.client.listing.IDataProvider;
import com.tll.client.listing.ListingFactory;
import com.tll.client.model.IndexedProperty;
import com.tll.client.model.Model;
import com.tll.client.model.PropertyPath;
import com.tll.client.model.RefKey;
import com.tll.client.ui.CSS;
import com.tll.client.ui.Dialog;
import com.tll.client.ui.field.CheckboxField;
import com.tll.client.ui.field.EditPanel;
import com.tll.client.ui.field.FieldLabel;
import com.tll.client.ui.field.FieldPanel;
import com.tll.client.ui.field.TextField;
import com.tll.client.ui.listing.AbstractListingWidget;
import com.tll.client.ui.listing.RowOpDelegate;

/**
 * InterfacePanel
 * @author jpk
 */
final class InterfaceOptionPanel extends InterfaceRelatedPanel implements ClickListener, IEditListener {

	private static final InterfaceOptionParamListingConfig plc = new InterfaceOptionParamListingConfig();

	private CheckboxField isDefault;

	private TextField setUpCost;
	private TextField monthlyCost;
	private TextField annualCost;

	private TextField baseSetupPrice;
	private TextField baseMonthlyPrice;
	private TextField baseAnnualPrice;

	private final PushButton btnDeleteToggle;

	private String optionName;

	/**
	 * list of parameters extracted from the parent property value group declared
	 * as a convenience.
	 */
	private List<Model> params;

	private final AbstractListingWidget lstngParams;

	private final List<InterfaceOptionParameterPanel> paramFieldPanels = new ArrayList<InterfaceOptionParameterPanel>();

	private int paramRowIndex = -1;

	private final EditPanel paramEditPanel;

	private final Dialog dlgParam;

	/**
	 * Constructor
	 * @param propName
	 */
	public InterfaceOptionPanel(String propName) {
		super(propName, "Interface Option");

		btnDeleteToggle = new PushButton();
		btnDeleteToggle.addClickListener(this);
		btnDeleteToggle.addStyleName(CSS.FLOAT_RIGHT);

		lstngParams = ListingFactory.dataListing(plc, new IDataProvider() {

			public List<Model> getData() {
				return params;
			}

		}, new RowOpDelegate() {

			@Override
			protected String getListingElementName() {
				return plc.getListingElementName();
			}

			@Override
			protected void doEditRow(int rowIndex, RefKey rowRef) {
				paramRowIndex = rowIndex - 1;
				Model param = params.get(paramRowIndex);
				String paramName = param.getName();
				assert paramName != null;
				InterfaceOptionParameterPanel pep = paramFieldPanels.get(rowIndex - 1);
				paramEditPanel.setFieldPanel(pep);
				paramEditPanel.setModel(param);
				paramEditPanel.refresh();
				dlgParam.setText("Edit " + paramName);
				dlgParam.setPopupPositionAndShow(new PopupPanel.PositionCallback() {

					public void setPosition(int offsetWidth, int offsetHeight) {
						dlgParam.setPopupPosition(lstngParams.getAbsoluteLeft() + 5, lstngParams.getAbsoluteTop() + 5);
					}

				});
			}

			@Override
			protected void doDeleteRow(int rowIndex, RefKey rowRef) {
				FieldGroup paramGrp = (FieldGroup) fields.getField(PropertyPath.indexed("parameters", rowIndex - 1));
				paramGrp.setMarkedDeleted(true);
				lstngParams.deleteRow(rowIndex);
			}

		}, new IAddRowDelegate() {

			public void handleAddRow() {
				// TODO impl
				Window.alert("You clicked the add button");
			}

		});

		paramEditPanel = new EditPanel(true);
		paramEditPanel.addEditListener(this);

		dlgParam = new Dialog(lstngParams, false);
		dlgParam.setWidget(paramEditPanel);
	}

	@Override
	protected void configure() {
		super.configure();

		isDefault = fbool("isDefault", "Default?");
		isDefault.setAlignBottom(true);

		setUpCost = ftext("setUpCost", "Setup Cost", LabelMode.NONE, 10);
		monthlyCost = ftext("monthlyCost", "Monthly Cost", LabelMode.NONE, 10);
		annualCost = ftext("annualCost", "Annual Cost", LabelMode.NONE, 10);

		baseSetupPrice = ftext("baseSetupPrice", "Setup Price", LabelMode.NONE, 10);
		baseMonthlyPrice = ftext("baseMonthlyPrice", "Monthly Price", LabelMode.NONE, 10);
		baseAnnualPrice = ftext("baseAnnualPrice", "Annual Price", LabelMode.NONE, 10);

		fields.addField(isDefault);
		fields.addField(setUpCost);
		fields.addField(monthlyCost);
		fields.addField(annualCost);
		fields.addField(baseSetupPrice);
		fields.addField(baseMonthlyPrice);
		fields.addField(baseAnnualPrice);

		FieldPanel frow;

		// first row
		frow = new FieldPanel(IField.CSS_FIELD_ROW);
		add(frow);
		frow.add(name);
		frow.add(code);
		frow.add(description);
		frow.add(isDefault);

		frow.add(btnDeleteToggle);

		// second row
		frow = new FieldPanel(IField.CSS_FIELD_ROW);
		add(frow);

		Grid g = new Grid(3, 4);
		g.setWidget(0, 1, new FieldLabel("Set Up", setUpCost.getDomId(), false));
		g.setWidget(0, 2, new FieldLabel("Monthly", monthlyCost.getDomId(), false));
		g.setWidget(0, 3, new FieldLabel("Annual", annualCost.getDomId(), false));
		g.setWidget(1, 0, new FieldLabel("Cost", setUpCost.getDomId(), true));
		g.setWidget(1, 1, setUpCost);
		g.setWidget(1, 2, monthlyCost);
		g.setWidget(1, 3, annualCost);
		g.setWidget(2, 0, new FieldLabel("Price", baseSetupPrice.getDomId(), true));
		g.setWidget(2, 1, baseSetupPrice);
		g.setWidget(2, 2, baseMonthlyPrice);
		g.setWidget(2, 3, baseAnnualPrice);
		frow.add(g);

		frow.add(lstngParams);
	}

	@Override
	protected void onBeforeBind(Model modelOption) {
		super.onBeforeBind(modelOption);
		optionName = modelOption.getName();
		setMarkDeleted(false);

		// clear existing params
		for(InterfaceOptionParameterPanel pp : paramFieldPanels) {
			fields.removeField(pp.getFields());
		}
		paramFieldPanels.clear();

		// add params
		Iterator<IndexedProperty> itr = modelOption.relatedMany("parameters").iterator();
		if(itr != null) {
			while(itr.hasNext()) {
				IndexedProperty param = itr.next();
				InterfaceOptionParameterPanel pp = new InterfaceOptionParameterPanel(param.getPropertyName());
				// pp.configure();
				paramFieldPanels.add(pp);
				fields.addField(pp.getFields());
			}
		}

		params = modelOption.relatedMany("parameters").getList();
		lstngParams.display();
	}

	private void setMarkDeleted(boolean markDeleted) {
		assert optionName != null;
		getFields().setMarkedDeleted(markDeleted);
		if(markDeleted) {
			btnDeleteToggle.getUpFace().setImage(App.imgs().undo().createImage());
			btnDeleteToggle.setTitle("Un-delete " + optionName + " Option");
		}
		else {
			btnDeleteToggle.getUpFace().setImage(App.imgs().delete().createImage());
			btnDeleteToggle.setTitle("Delete " + optionName + " Option");
		}
	}

	public void onClick(Widget sender) {
		if(sender == btnDeleteToggle) {
			setMarkDeleted(!getFields().isMarkedDeleted());
		}
	}

	public void onEditEvent(EditEvent event) {
		if(event.getOp() == EditOp.SAVE) {
			if(!event.getModel().isNew()) {
				lstngParams.updateRow(paramRowIndex + 1, event.getModel());
			}
		}
		dlgParam.hide();
	}

}
