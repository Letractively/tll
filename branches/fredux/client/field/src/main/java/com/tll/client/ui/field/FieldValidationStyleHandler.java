/**
 * The Logic Lab
 * @author jpk
 * Mar 4, 2009
 */
package com.tll.client.ui.field;

import com.tll.client.ui.IWidgetRef;
import com.tll.client.validate.IError;
import com.tll.client.validate.IValidationFeedback;

/**
 * FieldValidationStyleHandler - Handles IField validation styling application
 * and removal.
 * @author jpk
 */
public class FieldValidationStyleHandler implements IValidationFeedback {

	@Override
	public void handleError(IWidgetRef source, IError error) {
		source.getWidget().removeStyleName(IField.Styles.DIRTY);
		source.getWidget().addStyleName(IField.Styles.INVALID);
	}

	@Override
	public void resolveError(IWidgetRef source) {
		source.getWidget().removeStyleName(IField.Styles.INVALID);
	}
}
