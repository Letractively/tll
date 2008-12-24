/**
 * The Logic Lab
 * @author jpk Dec 11, 2007
 */
package com.tll.client.ui;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.KeyboardListenerCollection;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.Widget;

/**
 * OptionsPopup
 * @author jpk
 */
public abstract class OptionsPopup extends TimedPopup implements MouseListener, IOptionListener, ISourcesOptionEvents {

	protected final OptionsPanel optionsPanel = new OptionsPanel();

	private final KeyboardListenerCollection keyboardListeners = new KeyboardListenerCollection();

	/**
	 * Constructor
	 * @param duration
	 */
	public OptionsPopup(int duration) {
		super(true, false, duration);
		optionsPanel.addOptionListener(this);
		keyboardListeners.add(optionsPanel);
		setWidget(optionsPanel);
	}

	public void setOptions(Option[] options) {
		optionsPanel.setOptions(options);
	}

	@Override
	public boolean onEventPreview(Event event) {
		boolean rval = super.onEventPreview(event);
		if(rval) {
			switch(event.getTypeInt()) {
				case Event.ONKEYDOWN: {
					final int key = event.getKeyCode();
					switch(key) {
						case KeyboardListener.KEY_UP:
						case KeyboardListener.KEY_DOWN:
						case KeyboardListener.KEY_ENTER:
							keyboardListeners.fireKeyboardEvent(this, event);
							return false;
						case KeyboardListener.KEY_ESCAPE:
							hide();
							return false;
					}
				}
			}
		}
		return rval;
	}

	public void onMouseDown(Widget sender, int x, int y) {
		final Element elm = sender.getElement();
		// need to UN-offset the positioning of the focus panel (the sender)
		setPopupPosition(x + 13 + elm.getAbsoluteLeft(), y - 5 + elm.getAbsoluteTop());
	}

	public void onMouseEnter(Widget sender) {
		cancelTimer();
	}

	public void onMouseLeave(Widget sender) {
		startTimer();
	}

	public void onMouseMove(Widget sender, int x, int y) {
	}

	public void onMouseUp(Widget sender, int x, int y) {
	}

	public void onCurrentOptionChanged(OptionEvent event) {
		assert (event.getSource() == optionsPanel);
		cancelTimer();
	}

	public void onOptionSelected(OptionEvent event) {
		assert (event.getSource() == optionsPanel);
		hide();
	}

	public void addOptionListener(IOptionListener listener) {
		optionsPanel.addOptionListener(listener);
	}

	public void removeOptionListener(IOptionListener listener) {
		optionsPanel.removeOptionListener(listener);
	}

}
