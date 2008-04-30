/**
 * 
 */
package com.tll.client.validate;

import java.util.List;

import com.tll.client.msg.Msg;

/**
 * IValidationFeedback - Definition for providing validation feedback to
 * ascribed listeners.
 * @author jpk
 */
public interface IValidationFeedback {

	/**
	 * @return list of {@link Msg}s representing validation feedback.
	 */
	List<Msg> getValidationMessages();
}
