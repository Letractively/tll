/**
 * The Logic Lab
 * @author jkirton
 * May 21, 2008
 */
package com.tll.client.ui.field.intf;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.FlowPanelFieldComposer;
import com.tll.client.ui.field.IFieldRenderer;
import com.tll.common.bind.IBindable;
import com.tll.common.model.Model;

/**
 * SwitchInterfacePanel - One option exists that is either on or off.
 * @author jpk
 * @param <M>
 */
public final class SwitchInterfacePanel<M extends IBindable> extends AbstractInterfacePanel<FlowPanel, M> {

	class SwitchInterfaceFieldsRenderer implements IFieldRenderer<FlowPanel> {

		@SuppressWarnings("synthetic-access")
		public void render(FlowPanel panel, FieldGroup fg) {
			final FlowPanelFieldComposer cmpsr = new FlowPanelFieldComposer();
			cmpsr.setCanvas(canvas);

			// first row
			cmpsr.addField(fg.getFieldWidgetByName("intfName"));
			cmpsr.addField(fg.getFieldWidgetByName("intfCode"));
			cmpsr.addField(fg.getFieldWidgetByName("intfDescription"));

			cmpsr.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
			cmpsr.addField(fg.getFieldWidgetByName(Model.DATE_CREATED_PROPERTY));
			cmpsr.stopFlow();
			cmpsr.addField(fg.getFieldWidgetByName(Model.DATE_MODIFIED_PROPERTY));
			cmpsr.resetAlignment();

			cmpsr.newRow();
			cmpsr.addWidget(createAvailabilityGrid(fg));
		}
	}

	private final FlowPanel canvas = new FlowPanel();

	/**
	 * Constructor
	 */
	public SwitchInterfacePanel() {
		super();
		initWidget(canvas);
	}

	@Override
	protected FieldGroup generateFieldGroup() {
		final FieldGroup fg = (new InterfaceFieldProvider()).getFieldGroup();

		// the switch option
		fg.addField("options[0]", (new OptionFieldProvider()).getFieldGroup());

		return fg;
	}

	@Override
	public IFieldRenderer<FlowPanel> getRenderer() {
		return new SwitchInterfaceFieldsRenderer();
	}
}
