package com.tll.service.acl.afterinvocation;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.acegisecurity.AuthorizationServiceException;

import com.tll.SystemError;

/**
 * Responsible for providing the appropriate filterer for use by a
 * {@link com.tll.service.acl.afterinvocation.BasicAclEntryAfterInvocationFilteringProvider}.
 * @author jpk
 */
public class FiltererFactory {

	/**
	 * List of available {@link IMapFiltererTransformer} objects.
	 */
	private List<IMapFiltererTransformer> mapFiltererTransformers;

	/**
	 * Constructor
	 */
	public FiltererFactory() {
	}

	/**
	 * Constructor
	 * @param mapFiltererTransformers
	 */
	// @Inject
	// TODO determine if we need map transformers
	public FiltererFactory(List<IMapFiltererTransformer> mapFiltererTransformers) {
		this.mapFiltererTransformers = mapFiltererTransformers;
	}

	/**
	 * Creates the appropriate filterer based on the given object.
	 * @param obj the object for which to create an appropriate filterer
	 * @return an appropriate filterer.
	 */
	@SuppressWarnings("unchecked")
	public IFilterer create(Object obj) {
		IFilterer filterer;
		if(obj instanceof Collection) {
			Collection<Object> collection = (Collection) obj;
			filterer = new CollectionFilterer(collection);
		}
		else if(obj.getClass().isArray()) {
			Object[] array = (Object[]) obj;
			filterer = new ArrayFilterer(array);
		}
		else if(obj instanceof Map) {
			Map<Object, Object> map = (Map) obj;
			filterer = createMapFilterer(map);
		}
		else {
			throw new AuthorizationServiceException(
					"A collection, array, or serviceMap argument is required but the argument was: " + obj);
		}
		return filterer;
	}

	/**
	 * Creates a serviceMap-based filterer.
	 */
	private MapFilterer createMapFilterer(Map<Object, Object> map) {
		return new MapFilterer(map, obtainApplicableTransformer(map));
	}

	/**
	 * Returns an applicable transformer based on iterating the list of available
	 * mapFiltererTransformers.
	 * @param serviceMap the serviceMap for which an applicable transformer is
	 *        created.
	 * @return created transformer instance.
	 * @throws IllegalArgumentException if the serviceMap argument is null.
	 * @throws SystemError if no applicable transformer is found.
	 */
	private IMapFiltererTransformer obtainApplicableTransformer(Map<Object, Object> map) {
		if(map == null) {
			throw new IllegalArgumentException("The serviceMap argument must not be null.");
		}
		if(map.size() == 0) {
			return new EmptyMapFiltererTransformer();
		}
		for(IMapFiltererTransformer t : mapFiltererTransformers) {
			if(t.isApplicable(map)) {
				return t;
			}
		}
		throw new SystemError("Encountered serviceMap with no applicable serviceMap filterer transformer");
	}

	/**
	 * Transforms empty serviceMap/collection arguments.
	 * @author jpk
	 */
	public static final class EmptyMapFiltererTransformer implements IMapFiltererTransformer {

		public boolean isApplicable(Map<Object, Object> map) {
			return (map != null && map.size() == 0);
		}

		public Collection<Object> toCollection(Map<Object, Object> map) {
			return new HashSet<Object>(0);
		}

		public Map<Object, Object> toMap(Collection<Object> clc) {
			return new HashMap<Object, Object>(0);
		}
	}
}
