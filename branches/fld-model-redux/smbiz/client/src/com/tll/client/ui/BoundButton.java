package com.tll.client.ui;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.SourcesClickEvents;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.bind.IBindable;

/**
 * BoundButton
 * @author jpk
 * @param <M> The model type
 */
public class BoundButton<M extends IBindable> extends AbstractBoundWidget<String, String, M> implements SourcesClickEvents, HasFocus {

	private final com.google.gwt.user.client.ui.Button base = new com.google.gwt.user.client.ui.Button();
	private String value;

	/**
	 * Constructor
	 */
	public BoundButton() {
		init();
	}

	/**
	 * Constructor
	 * @param label
	 */
	public BoundButton(String label) {
		init();
		setValue(label);
	}

	/**
	 * Constructor
	 * @param label
	 * @param listener
	 */
	public BoundButton(String label, ClickListener listener) {
		init();
		setValue(label);
		addClickListener(listener);
	}

	public void addClickListener(ClickListener listener) {
		base.addClickListener(listener);
	}

	public void addFocusListener(FocusListener listener) {
		base.addFocusListener(listener);
	}

	public void addKeyboardListener(KeyboardListener listener) {
		base.addKeyboardListener(listener);
	}

	public Object getProperty(String propPath) {
		return null;
	}

	public void setProperty(String propPath, Object value) {
		// no-op
	}

	public String getHTML() {
		return base.getHTML();
	}

	@Override
	public String getStyleName() {
		return base.getStyleName();
	}

	public int getTabIndex() {
		int retValue;

		retValue = base.getTabIndex();

		return retValue;
	}

	public String getText() {
		return base.getText();
	}

	@Override
	public String getTitle() {
		return base.getTitle();
	}

	public String getValue() {
		return value;
	}

	protected void init() {
		ClickListener listener = new ClickListener() {

			public void onClick(Widget sender) {
				if(getAction() != null) {
					getAction().execute();
				}
			}
		};

		base.addClickListener(listener);
		initWidget(base);
	}

	public void removeClickListener(ClickListener listener) {
		base.removeClickListener(listener);
	}

	public void removeFocusListener(FocusListener listener) {
		base.removeFocusListener(listener);
	}

	public void removeKeyboardListener(KeyboardListener listener) {
		base.removeKeyboardListener(listener);
	}

	public void setEnabled(boolean enabled) {
		base.setEnabled(enabled);
	}

	public void setFocus(boolean focused) {
		base.setFocus(focused);
	}

	public void setHTML(String html) {
		base.setHTML(html);
	}

	public void setTabIndex(int index) {
		base.setTabIndex(index);
	}

	public void setText(String text) {
		base.setText(text);
	}

	@Override
	public void setTitle(String title) {
		base.setTitle(title);
	}

	public void setValue(String value) {
		String old = value;
		setText(value);
		changeSupport.firePropertyChange(PROPERTY_VALUE, old, value);
	}

	public boolean isEnabled() {
		return base.isEnabled();
	}

	public void setAccessKey(char key) {
		base.setAccessKey(key);
	}

}
