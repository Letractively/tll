/**
 * The Logic Lab
 * @author jpk
 * Feb 24, 2008
 */
package com.tll.client.mvc.view.intf;

import java.util.LinkedHashMap;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.tll.client.data.rpc.CrudCommand;
import com.tll.client.data.rpc.IRpcCommand;
import com.tll.client.data.rpc.ModelDataCommand;
import com.tll.client.model.ModelAssembler;
import com.tll.client.model.ModelChangeEvent;
import com.tll.client.mvc.view.AbstractRpcAndModelAwareView;
import com.tll.client.mvc.view.StaticViewInitializer;
import com.tll.client.mvc.view.ViewClass;
import com.tll.client.ui.Dialog;
import com.tll.client.ui.GridRenderer;
import com.tll.client.ui.field.FieldFactory;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.AbstractBindableFieldPanel;
import com.tll.client.ui.field.AbstractBindableFlowFieldPanel;
import com.tll.client.ui.field.IFieldRenderer;
import com.tll.client.ui.field.RadioGroupField;
import com.tll.client.ui.field.intf.MultiOptionInterfacePanel;
import com.tll.client.ui.field.intf.SwitchInterfacePanel;
import com.tll.client.ui.view.ViewToolbar;
import com.tll.common.data.ModelDataPayload;
import com.tll.common.data.ModelDataRequest;
import com.tll.common.model.Model;
import com.tll.common.model.ModelKey;
import com.tll.common.model.SmbizEntityType;
import com.tll.common.search.NamedQuerySearch;
import com.tll.common.search.PrimaryKeySearch;

/**
 * InterfacesView - AbstractView for managing Interfaces and the sub-entities.
 * @author jpk
 */
@SuppressWarnings("synthetic-access")
public class InterfacesView extends AbstractRpcAndModelAwareView<StaticViewInitializer> implements ClickHandler {

	public static final Class klas = new Class();

	public static final class Class extends ViewClass {

		@Override
		public String getName() {
			return "interfacesView";
		}

		@Override
		public InterfacesView newView() {
			return new InterfacesView();
		}

	} // Class

	/**
	 * IntfSlectDlg
	 * @author jpk
	 */
	final class IntfSlectDlg extends Dialog {

		/**
		 * InterfaceTypeSelectPanel
		 * @author jpk
		 */
		final class InterfaceTypeSelectPanel extends AbstractBindableFlowFieldPanel {

			SmbizEntityType intfType;

			@Override
			protected FieldGroup generateFieldGroup() {
				final FieldGroup fg = new FieldGroup("Interface Type Selection");
				final LinkedHashMap<SmbizEntityType, String> data = new LinkedHashMap<SmbizEntityType, String>();
				data.put(SmbizEntityType.INTERFACE_SWITCH, "Switch");
				data.put(SmbizEntityType.INTERFACE_SINGLE, "Single");
				data.put(SmbizEntityType.INTERFACE_MULTI, "Multi");
				final RadioGroupField<SmbizEntityType> f =
					FieldFactory.fradiogroup("interfaceType", "interfaceType", null, "Interface Type", data, new GridRenderer(
							1, null));
				f.addValueChangeHandler(new ValueChangeHandler<SmbizEntityType>() {

					@Override
					public void onValueChange(ValueChangeEvent<SmbizEntityType> event) {
						intfType = event.getValue();
						assert intfType != null;
						final Model m = ModelAssembler.assemble(intfType);
						assert m != null;
						intfStack.addInterface(m, false);
						IntfSlectDlg.this.hide();
						intfStack.showInterface(intfStack.getNumInterfaces() - 1);
					}
				});
				fg.addField(f);
				return fg;
			}

			@Override
			protected IFieldRenderer<FlowPanel> getRenderer() {
				return new IFieldRenderer<FlowPanel>() {

					@Override
					public void render(FlowPanel widget, FieldGroup fg) {
						final HorizontalPanel hp = new HorizontalPanel();
						hp.setStyleName("addIntf");
						hp.add(fg.getFieldByName("interfaceType").getWidget());
						hp.add(new Button("Cancel", new ClickHandler() {

							@Override
							public void onClick(ClickEvent event) {
								IntfSlectDlg.this.hide();
							}
						}));
						widget.add(hp);
					}
				};
			}

		} // InterfaceTypeSelectPanel

		final InterfaceTypeSelectPanel fldPnl = new InterfaceTypeSelectPanel();

		/**
		 * Constructor
		 */
		public IntfSlectDlg() {
			super();
			setText("Select the interface type to add");
			add(new InterfaceTypeSelectPanel());
		}

	} // IntfSlectDlg

	static final ModelDataRequest auxDataRequest = new ModelDataRequest();

	static {
		auxDataRequest.requestEntityPrototype(SmbizEntityType.INTERFACE_SWITCH);
		auxDataRequest.requestEntityPrototype(SmbizEntityType.INTERFACE_SINGLE);
		auxDataRequest.requestEntityPrototype(SmbizEntityType.INTERFACE_MULTI);
		auxDataRequest.requestEntityPrototype(SmbizEntityType.INTERFACE_OPTION);
		auxDataRequest.requestEntityPrototype(SmbizEntityType.INTERFACE_OPTION_PARAMETER_DEFINITION);
	}

	private final PushButton btnAddIntf = new PushButton("Add Interface");

	private IntfSlectDlg dlg;

	private InterfaceStack intfStack;

	/**
	 * Constructor
	 */
	public InterfacesView() {
		super();
		btnAddIntf.addClickHandler(this);
	}

	@Override
	public ViewClass getViewClass() {
		return klas;
	}

	@Override
	public String getLongViewName() {
		return "Interfaces";
	}

	@Override
	protected String getViewStyle() {
		return "interfaces";
	}

	@Override
	protected void doInitialization(StaticViewInitializer initializer) {
		// now we are ready to create the intf stack as we have a valid view
		// container ref
		intfStack =
			new InterfaceStack(new NamedQuerySearch(SmbizEntityType.INTERFACE, "interface.summaryList", true),
					auxDataRequest, new InterfaceStack.IFieldPanelResolver() {

				@Override
				public AbstractBindableFieldPanel<?> resolveFieldPanel(ModelKey mkey) {
					final SmbizEntityType type = (SmbizEntityType) mkey.getEntityType();
					switch(type) {
					case INTERFACE_SWITCH:
						return new SwitchInterfacePanel();
					case INTERFACE_SINGLE:
						return new MultiOptionInterfacePanel(true);
					case INTERFACE_MULTI:
						return new MultiOptionInterfacePanel(false);
					}
					throw new IllegalArgumentException("Unhandled interface type");
				}
			}, new InterfaceStack.IFieldPanelDataLoader() {

				@Override
				public IRpcCommand load(ModelKey intfKey, ModelDataRequest adr) {
					final CrudCommand c = new CrudCommand();
					c.load(new PrimaryKeySearch(intfKey), adr);
					return c;
				}
			}, true);
		addWidget(intfStack);
	}

	@Override
	protected void decorateToolbar(ViewToolbar toolbar) {
		// add add interface button
		toolbar.addViewOpButton(this.btnAddIntf, "Add Interface");
	}

	@Override
	protected boolean shouldHandleModelChangeEvent(ModelChangeEvent event) {
		final SmbizEntityType set = (SmbizEntityType) event.getModelKey().getEntityType();
		return set.isInterfaceType();
	}

	@Override
	protected void doRefresh() {
		intfStack.refreshData();
	}

	@Override
	protected void doDestroy() {
		intfStack.clearData();
	}

	private void showAddIntfDlg() {
		// show the interface type selection dialog
		if(dlg == null) dlg = new IntfSlectDlg();
		dlg.center();
	}

	@Override
	public void onClick(ClickEvent event) {
		if(event.getSource() == btnAddIntf) {
			// ensure we have the aux data cached
			final ModelDataRequest adr = ModelDataCommand.filterRequest(auxDataRequest);
			if(adr != null) {
				(new ModelDataCommand(adr) {

					@Override
					protected void handleSuccess(ModelDataPayload result) {
						super.handleSuccess(result);
						showAddIntfDlg();
					}

				}).execute();
			}
			else {
				showAddIntfDlg();
			}
		}
	}
}
