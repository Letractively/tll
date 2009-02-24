package com.tll.client;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.tll.client.mock.ComplexFieldPanel;
import com.tll.client.ui.edit.EditPanel;
import com.tll.client.ui.field.ModelViewer;
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
		return new UITestCase[] { new FieldBindingLifecycleTest() };
	}

	/**
	 * FieldBindingLifecycleTest - Tests field/model binding functionality through
	 * a mocked model field panel and edit panel.
	 * @author jpk
	 */
	static final class FieldBindingLifecycleTest extends UITestCase {

		HorizontalPanel context;
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
			context = new HorizontalPanel();
			context.getElement().getStyle().setProperty("margin", "1em");
			context.getElement().getStyle().setProperty("padding", "1em");
			context.getElement().getStyle().setProperty("border", "1px solid gray");

			mv = new ModelViewer(250, 350);
			ep = new EditPanel(new ComplexFieldPanel(), false, false);
			ep.addEditListener(mv);

			context.add(ep);
			context.add(mv);

			RootPanel.get().add(context);
			
			m = MockModelStubber.create(ModelType.COMPLEX);
			ep.setModel(m);
			mv.setModel(m);
		}

		@Override
		public void unload() {
			if(ep != null) {
				// TODO do we need to cleanup bindings ???
				ep.removeFromParent();
				ep = null;
			}
			if(mv != null) {
				mv.removeFromParent();
				mv = null;
			}
			if(context != null) {
				context.removeFromParent();
				context = null;
			}
			m = null;
		}

	} // FieldBindingLifecycleTest
}
