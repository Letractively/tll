/**
 * The Logic Lab
 * @author jpk
 * Mar 7, 2008
 */
package com.tll.dao.hibernate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.transform.ResultTransformer;

import com.tll.dao.SearchResult;
import com.tll.model.IEntity;

/**
 * SearchResultTransformer - Simple transformer that ensures:
 * <ul>
 * <li>distinct results
 * <li>wraps either a non-entity tuple or whole entity in a common container:
 * {@link SearchResult}
 * </ul>
 * @author jpk
 */
abstract class SearchResultTransformer implements ResultTransformer {

	private static final long serialVersionUID = -7867685675670170590L;

	static final class Identity {

		final Object entity;

		Identity(Object entity) {
			this.entity = entity;
		}

		@Override
		public boolean equals(Object other) {
			Identity that = (Identity) other;
			return entity == that.entity;
		}

		@Override
		public int hashCode() {
			return System.identityHashCode(entity);
		}
	}

	@SuppressWarnings("unchecked")
	public final List<SearchResult<? extends IEntity>> transformList(List list) {
		List<SearchResult<? extends IEntity>> result = new ArrayList<SearchResult<? extends IEntity>>(list.size());
		Set<Identity> distinct = new HashSet<Identity>();
		for(int i = 0; i < list.size(); i++) {
			Object entity = list.get(i);
			if(distinct.add(new Identity(entity))) {
				result.add(new SearchResult(entity));
			}
		}
		return result;
	}

}