package com.tll.client;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.tll.client.mock.ComplexFieldPanel;
import com.tll.client.ui.GridRenderer;
import com.tll.client.ui.edit.EditEvent;
import com.tll.client.ui.edit.EditPanel;
import com.tll.client.ui.edit.IEditHandler;
import com.tll.client.ui.field.FieldFactory;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.GridFieldComposer;
import com.tll.client.ui.field.IFieldWidget;
import com.tll.client.ui.mock.ModelViewer;
import com.tll.client.ui.msg.GlobalMsgPanel;
import com.tll.client.util.GlobalFormat;
import com.tll.common.model.IntPropertyValue;
import com.tll.common.model.Model;
import com.tll.common.model.mock.MockModelStubber;
import com.tll.common.model.mock.MockModelStubber.ModelType;

/**
 * UI Tests - GWT module for the sole purpose of verifying the DOM/Style of
 * compiled GWT code.
 */
public final class UITests extends AbstractUITest {

	@Override
	protected String getTestSubjectName() {
		return "client-field module";
	}

	@Override
	protected UITestCase[] getTestCases() {
		return new UITestCase[] {
			new FieldWidgetTest(), new FieldBindingLifecycleTest() };
	}

	/**
	 * FieldWidgetTest - tests the rendering of fields and their value change
	 * event handling for all defined field widget types.
	 * @author jpk
	 */
	static final class FieldWidgetTest extends UITestCase {
		
		enum TestEnum {
			ENUM_1,
			ENUM_2,
			ENUM_3,
			ENUM_4,
			ENUM_5,
			ENUM_6,
			ENUM_7,
			ENUM_8;
		}

		HorizontalPanel layout;
		FlowPanel pfields;
		ValueChangeDisplay vcd;
		FieldGroup group;

		@Override
		public String getName() {
			return "Field widget test";
		}

		@Override
		public String getDescription() {
			return "Renders all defined field widgets verifying their operation";
		}
		
		static class ValueChangeDisplay extends Composite {

			final VerticalPanel outer = new VerticalPanel();
			final VerticalPanel events = new VerticalPanel();
			final ScrollPanel sp = new ScrollPanel();
			final Button btnClear = new Button("Clear");

			/**
			 * Constructor
			 */
			public ValueChangeDisplay() {
				super();
				sp.add(events);
				sp.setHeight("300px");
				outer.add(btnClear);
				outer.add(sp);
				initWidget(outer);
				btnClear.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						events.clear();
					}
				});
			}

			private String getRowString(ValueChangeEvent<?> event) {
				final IFieldWidget<?> source = (IFieldWidget<?>) event.getSource();
				String val;
				if(event.getValue() instanceof Collection) {
					final Collection<?> clc = (Collection<?>) event.getValue();
					final StringBuilder sb = new StringBuilder();
					for(final Object e : clc) {
						sb.append(", ");
						sb.append(e.toString());
					}
					val = sb.length() == 0 ? "-" : sb.substring(1);
				}
				else {
					val = event.getValue() == null ? "-" : event.getValue().toString();
				}
				// FORMAT: {source}|{value}
				final StringBuilder sb = new StringBuilder();
				sb.append(source.getName());
				sb.append(" | ");
				sb.append(val);
				return sb.toString();
			}

			public void addRow(ValueChangeEvent<?> event) {
				events.add(new Label(getRowString(event), false));
			}
		}
		
		private void generateFields() {
			group = new FieldGroup("group");
			
			final Map<String, String> data = new LinkedHashMap<String, String>();
			data.put("valueA", "Key1");
			data.put("valueB", "Key2");
			data.put("valueC", "Key3");
			data.put("valueD", "Key4");
			data.put("valueE", "Key5");
			data.put("valueF", "Key6");
			data.put("valueG", "Key7");
			data.put("valueH", "Key8");
			data.put("valueI", "Key9");
			data.put("valueJ", "Key10");
			
			IFieldWidget<String> sfw;
			IFieldWidget<Boolean> bfw;
			IFieldWidget<Date> dfw;
			IFieldWidget<TestEnum> efw;
			IFieldWidget<Collection<String>> cfw;
			
			sfw = FieldFactory.ftext("ftext", "ftext", "TextField", "TextField", 8);
			group.addField(sfw);
			sfw.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					vcd.addRow(event);
				}
			});

			sfw = FieldFactory.ftextarea("ftextarea", "ftextarea", "Textarea", "Textarea", 5, 10);
			group.addField(sfw);
			sfw.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					vcd.addRow(event);
				}
			});

			sfw = FieldFactory.fpassword("fpassword", "fpassword", "Password", "Password", 8);
			group.addField(sfw);
			sfw.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					vcd.addRow(event);
				}
			});

			dfw = FieldFactory.fdate("fdate", "fdate", "DateField", "DateField", GlobalFormat.DATE);
			group.addField(dfw);
			dfw.addValueChangeHandler(new ValueChangeHandler<Date>() {

				@Override
				public void onValueChange(ValueChangeEvent<Date> event) {
					vcd.addRow(event);
				}
			});

			bfw = FieldFactory.fcheckbox("fcheckbox", "fcheckbox", "Checkbox", "Checkbox");
			group.addField(bfw);
			bfw.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					vcd.addRow(event);
				}
			});

			sfw = FieldFactory.fselect("fselect", "fselect", "Select", "Select", data);
			group.addField(sfw);
			sfw.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					vcd.addRow(event);
				}
			});

			cfw = FieldFactory.fmultiselect("fmultiselect", "fmultiselect", "Multi-select", "Multi-select", data);
			group.addField(cfw);
			cfw.addValueChangeHandler(new ValueChangeHandler<Collection<String>>() {

				@Override
				public void onValueChange(ValueChangeEvent<Collection<String>> event) {
					vcd.addRow(event);
				}
			});

			sfw =
					FieldFactory.fradiogroup("fradiogroup", "fradiogroup", "Radio Group", "Radio Group", data,
							new GridRenderer(3));
			group.addField(sfw);
			sfw.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					vcd.addRow(event);
				}
			});

			sfw = FieldFactory.fsuggest("fsuggest", "fsuggest", "Suggest", "Suggest", data);
			group.addField(sfw);
			sfw.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					vcd.addRow(event);
				}
			});
			
			efw =
					FieldFactory.fenumradio("fenumradio", "fenumradio", "Enum Radio", "Enum Radio", TestEnum.class,
							new GridRenderer(3));
			group.addField(efw);
			efw.addValueChangeHandler(new ValueChangeHandler<TestEnum>() {

				@Override
				public void onValueChange(ValueChangeEvent<TestEnum> event) {
					vcd.addRow(event);
				}
			});
		}

		@Override
		public void load() {
			layout = new HorizontalPanel();
			layout.setSpacing(7);
			layout.setBorderWidth(1);
			layout.getElement().getStyle().setProperty("margin", "1em");
			layout.getElement().getStyle().setProperty("border", "1px solid gray");

			pfields = new FlowPanel();
			vcd = new ValueChangeDisplay();
			layout.add(pfields);
			layout.add(vcd);

			RootPanel.get().add(layout);

			final GridFieldComposer composer = new GridFieldComposer();
			composer.setCanvas(pfields);
			generateFields();
			for(final IFieldWidget<?> f : group.getFieldWidgets(null)) {
				composer.addField(f);
			}
		}

		@Override
		public void unload() {
			layout.removeFromParent();
			group = null;
			vcd = null;
			pfields = null;
			layout = null;
		}

	} // FieldWidgetTest

	/**
	 * FieldBindingLifecycleTest - Tests field/model binding functionality through
	 * a mocked model field panel and edit panel.
	 * @author jpk
	 */
	static final class FieldBindingLifecycleTest extends UITestCase {

		HorizontalPanel layout;
		VerticalPanel context;
		GlobalMsgPanel gmp;
		EditPanel ep;
		ModelViewer mv;
		Model m;
		

		@Override
		public String getName() {
			return "Model/Field binding";
		}

		@Override
		public String getDescription() {
			return "Tests model/field binding with handling a nested model collection.";
		}

		@Override
		public void load() {
			layout = new HorizontalPanel();
			layout.setSpacing(7);
			layout.setBorderWidth(1);
			layout.getElement().getStyle().setProperty("margin", "1em");
			
			gmp = new GlobalMsgPanel();

			mv = new ModelViewer();
			ep = new EditPanel(gmp, new ComplexFieldPanel(), false, false);
			ep.addEditHandler(new IEditHandler() {

				@Override
				public void onEdit(EditEvent event) {
					// mimic model persist life-cycle
					assert m != null;
					final Model mcopy = m.copy(true, false);
					mcopy.set(new IntPropertyValue(Model.VERSION_PROPERTY, 1));
					ep.setModel(mcopy);
					mv.setModel(mcopy);
					m = mcopy;
				}
			});

			context = new VerticalPanel();
			context.getElement().getStyle().setProperty("margin", "5px");
			context.add(gmp);
			context.add(ep);
			
			layout.add(context);
			layout.add(mv);

			RootPanel.get().add(layout);
			
			m = MockModelStubber.create(ModelType.COMPLEX);
			ep.setModel(m);
			mv.setModel(m);
		}

		@Override
		public void unload() {
			if(layout != null) {
				layout.removeFromParent();
				layout = null;
			}
			context = null;
			gmp = null;
			ep = null;
			mv = null;
			m = null;
		}

	} // FieldBindingLifecycleTest
}
