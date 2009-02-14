/**
 * The Logic Lab
 * @author jpk Dec 11, 2007
 */
package com.tll.client.ui.option;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.ui.TimedPopup;

/**
 * OptionsPopup
 * @author jpk
 */
public abstract class OptionsPopup extends TimedPopup implements MouseDownHandler, IOptionListener,
		ISourcesOptionEvents {

	protected final OptionsPanel optionsPanel = new OptionsPanel();

	//private final KeyboardListenerCollection keyboardListeners = new KeyboardListenerCollection();

	/**
	 * Constructor
	 * @param duration
	 */
	public OptionsPopup(int duration) {
		super(true, false, duration);
		optionsPanel.addOptionListener(this);
		//keyboardListeners.add(optionsPanel);
		addHandler(optionsPanel, KeyDownEvent.getType());
		setWidget(optionsPanel);
	}

	public void setOptions(Option[] options) {
		optionsPanel.setOptions(options);
	}

	@Override
	protected void onPreviewNativeEvent(NativePreviewEvent event) {
		super.onPreviewNativeEvent(event);
		if(!event.isCanceled()) {
			switch(event.getTypeInt()) {
				case Event.ONKEYDOWN: {
					final int key = event.getNativeEvent().getKeyCode();
					switch(key) {
						case KeyCodes.KEY_UP:
						case KeyCodes.KEY_DOWN:
						case KeyCodes.KEY_ENTER:
							//keyboardListeners.fireKeyboardEvent(this, event);
							fireEvent(event);
							event.cancel(); // TODO or consume?
						case KeyCodes.KEY_ESCAPE:
							hide();
							event.cancel(); // TODO or consume?
					}
				}
			}
		}
	}

	public void onMouseDown(MouseDownEvent event) {
		final Element elm = event.getNativeEvent().getTarget();
		// need to UN-offset the positioning of the focus panel (the sender)
		setPopupPosition(event.getClientX() + 13 + elm.getAbsoluteLeft(), event.getClientY() - 5 + elm.getAbsoluteTop());
	}

	public void onMouseEnter(Widget sender) {
		cancelTimer();
	}

	public void onMouseLeave(Widget sender) {
		startTimer();
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
