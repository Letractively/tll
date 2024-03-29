package com.tll.client.ui.option;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.tll.client.ui.ImageContainer;

/**
 * Option - A single option in an {@link OptionsPanel}.
 * @author jpk
 */
public class Option extends Label {

	private final String text;

	/**
	 * Constructor
	 * @param text The option text
	 */
	public Option(String text) {
		this(text, null);
	}

	/**
	 * Constructor
	 * @param text The option text
	 * @param img Optional image placed before the option text.
	 */
	public Option(String text, Image img) {
		super();
		setStyleName(OptionStyles.getOptions().option());
		this.text = text;
		if(img != null) {
			getElement().appendChild((new ImageContainer(img)).getElement());
		}
		final Element txt = DOM.createSpan();
		txt.setInnerText(text);
		getElement().appendChild(txt);
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public String toString() {
		return getText();
	}

}