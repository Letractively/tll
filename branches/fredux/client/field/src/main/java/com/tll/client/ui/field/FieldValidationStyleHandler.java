/**
 * The Logic Lab
 * @author jpk
 * Mar 4, 2009
 */
package com.tll.client.ui.field;

import com.tll.client.ui.IWidgetProvider;
import com.tll.client.ui.IWidgetRef;
import com.tll.client.validate.IError;
import com.tll.client.validate.IErrorHandler;

/**
 * FieldValidationStyleHandler - Handles IField validation styling application
 * and removal.
 * @author jpk
 */
public class FieldValidationStyleHandler implements IErrorHandler {

	@Override
	public void handleError(IWidgetRef source, IError error) {
		if(source instanceof IField) {
			source.getWidget().removeStyleName(IField.Styles.DIRTY);
			source.getWidget().addStyleName(IField.Styles.INVALID);
		}
	}

	@Override
	public void resolveError(IWidgetRef source) {
		if(source instanceof IField) {
			source.getWidget().removeStyleName(IField.Styles.INVALID);
		}
	}

	@Override
	public void toggleErrorNotification(IWidgetProvider source) {
		// no-op
	}

	@Override
	public void clear() {
		// no-op
	}

}
