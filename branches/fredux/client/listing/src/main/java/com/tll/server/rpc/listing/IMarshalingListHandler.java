/**
 * The Logic Lab
 * @author jpk
 * Feb 25, 2008
 */
package com.tll.server.rpc.listing;

import com.tll.common.model.Model;
import com.tll.dao.SearchResult;
import com.tll.listhandler.IDecoratedListHandler;
import com.tll.model.IEntity;

/**
 * IMarshalingListHandler - List handler responsible for transforming requested
 * rows from a {@link SearchResult} to a marshalable construct.
 * @param <E> The entity type
 * @author jpk
 */
public interface IMarshalingListHandler<E extends IEntity> extends IDecoratedListHandler<SearchResult<E>, Model> {

}
