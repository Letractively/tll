/**
 * The Logic Lab
 * @author jpk
 * Feb 24, 2008
 */
package com.tll.client.admin.ui.field.intf;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.App;
import com.tll.client.admin.ui.listing.InterfaceOptionParamListingConfig;
import com.tll.client.cache.AuxDataCache;
import com.tll.client.data.AuxDataRequest;
import com.tll.client.data.EntityOptions;
import com.tll.client.event.IEditListener;
import com.tll.client.event.IModelChangeListener;
import com.tll.client.event.type.EditEvent;
import com.tll.client.event.type.ModelChangeEvent;
import com.tll.client.event.type.EditEvent.EditOp;
import com.tll.client.field.FieldGroup;
import com.tll.client.listing.IAddRowDelegate;
import com.tll.client.listing.IDataProvider;
import com.tll.client.listing.ListingFactory;
import com.tll.client.listing.RowOpDelegate;
import com.tll.client.model.AbstractModelChangeHandler;
import com.tll.client.model.IModelChangeHandler;
import com.tll.client.model.IndexedProperty;
import com.tll.client.model.Model;
import com.tll.client.model.RefKey;
import com.tll.client.model.RelatedManyProperty;
import com.tll.client.ui.CSS;
import com.tll.client.ui.Dialog;
import com.tll.client.ui.FlowFieldPanelComposer;
import com.tll.client.ui.field.CheckboxField;
import com.tll.client.ui.field.EditPanel;
import com.tll.client.ui.field.FieldLabel;
import com.tll.client.ui.field.TextField;
import com.tll.client.ui.listing.AbstractListingWidget;
import com.tll.model.EntityType;

/**
 * AbstractInterfacePanel
 * @author jpk
 */
abstract class AbstractOptionPanel extends InterfaceRelatedPanel implements ClickListener, IEditListener {

	private static final InterfaceOptionParamListingConfig plc = new InterfaceOptionParamListingConfig();

	private CheckboxField isDefault;

	private TextField setUpCost;
	private TextField monthlyCost;
	private TextField annualCost;

	private TextField baseSetupPrice;
	private TextField baseMonthlyPrice;
	private TextField baseAnnualPrice;

	protected boolean bindDeleteBtn = true;
	protected PushButton btnDeleteToggle;

	private String optionName;

	/**
	 * list of parameters extracted from the parent property value group declared
	 * as a convenience.
	 */
	private List<Model> params;

	private final AbstractListingWidget lstngParams;

	private final List<ParameterPanel> paramFieldPanels = new ArrayList<ParameterPanel>();

	private int paramRowIndex = -1;

	private final EditPanel paramEditPanel;

	private final Dialog dlgParam;

	/**
	 * Single model instance used for adding params so we can retain field values.
	 */
	private Model paramPrototype;

	/**
	 * Constructor
	 * @param propName
	 */
	public AbstractOptionPanel(String propName) {
		super(propName, "Interface Option");

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
				ParameterPanel pep = paramFieldPanels.get(rowIndex - 1);
				paramEditPanel.setFieldPanel(pep);
				showParamEditDialog(param);
			}

			@Override
			protected void doDeleteRow(int rowIndex, RefKey rowRef) {
				// FieldGroup paramGrp = (FieldGroup)
				// fields.getField(PropertyPath.indexed("parameters", rowIndex - 1));
				FieldGroup paramGrp = paramFieldPanels.get(rowIndex - 1).getFields();
				if(paramGrp.isPending()) {
					params.remove(rowIndex - 1);
					paramFieldPanels.remove(rowIndex - 1);
					lstngParams.refresh();
				}
				else {
					paramGrp.setMarkedDeleted(true);
					lstngParams.markRowDeleted(rowIndex);
				}
			}

		}, new IAddRowDelegate() {

			public void handleAddRow() {
				ParameterPanel newParamPanel = new ParameterPanel(FieldGroup.getPendingPropertyName());
				paramFieldPanels.add(newParamPanel);
				paramEditPanel.setFieldPanel(newParamPanel);
				if(paramPrototype == null) {
					IModelChangeHandler handler = new AbstractModelChangeHandler() {

						@Override
						protected Widget getSourcingWidget() {
							return AbstractOptionPanel.this;
						}

						@Override
						protected AuxDataRequest getNeededAuxData() {
							return null;
						}

						@Override
						protected EntityOptions getEntityOptions() {
							return null;
						}

					};
					handler.addModelChangeListener(new IModelChangeListener() {

						public void onModelChangeEvent(ModelChangeEvent event) {
							if(!event.isError()) {
								paramPrototype =
										AuxDataCache.instance().getEntityPrototype(EntityType.INTERFACE_OPTION_PARAMETER_DEFINITION);
								assert paramPrototype != null;
								showParamEditDialog(paramPrototype.copy());
							}
						}

					});
					handler.handleModelPrototypeFetch(EntityType.INTERFACE_OPTION_PARAMETER_DEFINITION);
				}
				else {
					showParamEditDialog(paramPrototype.copy());
				}
			}

		});

		paramEditPanel = new EditPanel(true, false);
		paramEditPanel.addEditListener(this);

		dlgParam = new Dialog(lstngParams, false);
		dlgParam.setWidget(paramEditPanel);
	}

	public void setBindDeleteBtn(boolean bindDeleteBtn) {
		this.bindDeleteBtn = bindDeleteBtn;
	}

	@Override
	protected final void doInitFields() {
		super.doInitFields();

		isDefault = fbool("isDefault", "Default?");
		// isDefault.setAlignBottom(true);

		setUpCost = ftext("setUpCost", "Setup Cost", 10);
		monthlyCost = ftext("monthlyCost", "Monthly Cost", 10);
		annualCost = ftext("annualCost", "Annual Cost", 10);

		baseSetupPrice = ftext("baseSetupPrice", "Setup Price", 10);
		baseMonthlyPrice = ftext("baseMonthlyPrice", "Monthly Price", 10);
		baseAnnualPrice = ftext("baseAnnualPrice", "Annual Price", 10);

		fields.addField(isDefault);
		fields.addField(setUpCost);
		fields.addField(monthlyCost);
		fields.addField(annualCost);
		fields.addField(baseSetupPrice);
		fields.addField(baseMonthlyPrice);
		fields.addField(baseAnnualPrice);

		FlowFieldPanelComposer canvas = new FlowFieldPanelComposer(panel);

		// first row
		if(bindNameField) canvas.addField(name);
		if(bindCodeAndDescFields) {
			canvas.addField(code);
			canvas.addField(description);
		}
		canvas.addField(isDefault);

		if(bindDeleteBtn) {
			btnDeleteToggle = new PushButton();
			btnDeleteToggle.addClickListener(this);
			btnDeleteToggle.addStyleName(CSS.FLOAT_RIGHT);
			canvas.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
			canvas.addWidget(btnDeleteToggle);
			canvas.resetAlignment();
		}
		else {
			btnDeleteToggle = null;
		}

		// second row
		canvas.newRow();
		canvas.addWidget(createPricingGrid());

		// third row
		canvas.newRow();
		canvas.addWidget(lstngParams);
	}

	protected Grid createPricingGrid() {
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
		return g;
	}

	@Override
	protected void onBeforeBind(Model modelOption) {
		super.onBeforeBind(modelOption);

		optionName = modelOption.getName();

		// clear existing params
		for(ParameterPanel pp : paramFieldPanels) {
			fields.removeField(pp.getFields());
		}
		paramFieldPanels.clear();

		// add params
		RelatedManyProperty pvParams = modelOption.relatedMany("parameters");
		if(pvParams != null && pvParams.size() > 0) {
			for(IndexedProperty param : pvParams) {
				ParameterPanel pp = new ParameterPanel(param.getPropertyName());
				paramFieldPanels.add(pp);
				fields.addField(pp.getFields());
			}
		}

		// re-display params listing
		params = modelOption.relatedMany("parameters").getList();
		lstngParams.display();
	}

	@Override
	protected void onBeforeUpdateModel(Model model) {
		super.onBeforeUpdateModel(model);

		RelatedManyProperty pvParams = model.relatedMany("parameters");

		for(ParameterPanel pp : paramFieldPanels) {
			if(pp.getFields().isPending()) {
				Model proto = paramPrototype.copy();
				String actualPropName = pvParams.add(proto);
				pp.getFields().setPropertyName(actualPropName);
			}
		}
	}

	private void setMarkDeleted(boolean markDeleted) {
		assert optionName != null;
		getFields().setMarkedDeleted(markDeleted);
		if(btnDeleteToggle != null) {
			if(markDeleted) {
				btnDeleteToggle.getUpFace().setImage(App.imgs().undo().createImage());
				btnDeleteToggle.setTitle("Un-delete " + optionName + " Option");
			}
			else {
				btnDeleteToggle.getUpFace().setImage(App.imgs().delete().createImage());
				btnDeleteToggle.setTitle("Delete " + optionName + " Option");
			}
		}
	}

	private void showParamEditDialog(Model param) {
		paramEditPanel.setModel(param);
		paramEditPanel.refresh();
		dlgParam.setText((param.isNew() ? "Add" : "Edit") + " Parameter");
		dlgParam.setPopupPositionAndShow(new PopupPanel.PositionCallback() {

			public void setPosition(int offsetWidth, int offsetHeight) {
				dlgParam.setPopupPosition(lstngParams.getAbsoluteLeft() + 5, lstngParams.getAbsoluteTop() + 5);
			}

		});
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
			else {
				params.add(event.getModel());
				fields.addField(paramFieldPanels.get(paramFieldPanels.size() - 1).getFields());
				lstngParams.refresh();
			}
		}
		else if(event.getOp() == EditOp.CANCEL) {
			if(event.getModel().isNew()) {
				paramFieldPanels.remove(paramFieldPanels.size() - 1);
			}
		}
		dlgParam.hide();
	}
}
