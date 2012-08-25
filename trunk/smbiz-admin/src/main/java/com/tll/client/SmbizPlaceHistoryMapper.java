/**
 * The Logic Lab
 * @author jpk
 */
package com.tll.client;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;
import com.tll.client.place.HomeViewPlace;

/**
 * @author jpk
 */
@WithTokenizers(HomeViewPlace.Tokenizer.class)
public interface SmbizPlaceHistoryMapper extends PlaceHistoryMapper {

}
