package com.tll.client.ui;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.tll.client.App;

/**
 * Option - A single option in an {@link OptionsPanel}.
 * @author jpk
 */
public final class Option extends Label {

	private static final String EDIT_OPTION_PREFIX = "Edit ";
	private static final String DELETE_OPTION_PREFIX = "Delete ";

	/**
	 * Generates an edit option with the given name.
	 * <p>
	 * FORMAT: "Edit {subjectName}"
	 * @param subjectName
	 * @return New Option instance
	 */
	public static Option editOption(String subjectName) {
		return new Option(EDIT_OPTION_PREFIX + subjectName, App.imgs().pencil().createImage());
	}

	/**
	 * Generates a delete option with the given name.
	 * <p>
	 * FORMAT: "Delete {subjectName}"
	 * @param subjectName
	 * @return New Option instance
	 */
	public static Option deleteOption(String subjectName) {
		return new Option(DELETE_OPTION_PREFIX + subjectName, App.imgs().trash().createImage());
	}

	/**
	 * Does the option text indicate edit?
	 * @param optionText
	 * @return true/false
	 */
	public static boolean isEditOption(String optionText) {
		return optionText == null ? false : optionText.startsWith(EDIT_OPTION_PREFIX);
	}

	/**
	 * Does the option text indicate delete?
	 * @param optionText
	 * @return true/false
	 */
	public static boolean isDeleteOption(String optionText) {
		return optionText == null ? false : optionText.startsWith(DELETE_OPTION_PREFIX);
	}

	private static final String STYLE_OPTION = "option";

	private final String text;

	/**
	 * Constructor
	 * @param text The option text
	 * @param img The optional image the is placed before the option text.
	 */
	public Option(String text, Image img) {
		super();
		setStyleName(STYLE_OPTION);
		this.text = text;
		if(img != null) {
			getElement().appendChild((new ImageContainer(img)).getElement());
		}
		Element txt = DOM.createSpan();
		txt.setInnerText(text);
		getElement().appendChild(txt);
	}

	@Override
	public String getText() {
		return text;
	}
}