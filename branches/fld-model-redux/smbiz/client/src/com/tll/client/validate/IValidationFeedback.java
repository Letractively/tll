/*
 * IValidationFeedback.java
 *
 * Created on July 16, 2007, 12:58 PM
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package com.tll.client.validate;

/**
 * IValidationFeedback - Serves to propagate validation feedback throughout a
 * particular artifact.
 * <p><em><b>IMPT NOTE: </b>This code was originally derived from the <a href="http://gwittir.googlecode.com/">gwittir</a> project.</em>
 * @author jpk
 */
public interface IValidationFeedback {

	/**
	 * Resolve the validation error.
	 * @param source
	 */
	void resolve(Object source);

	/**
	 * Handles the validation exception.
	 * @param source
	 * @param exception
	 */
	void handleException(Object source, ValidationException exception);

}
