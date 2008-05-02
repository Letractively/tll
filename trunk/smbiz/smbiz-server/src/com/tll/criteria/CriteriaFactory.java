/**
 * 
 */
package com.tll.criteria;

import java.util.Map;

import com.tll.model.IEntity;
import com.tll.model.INamedEntity;
import com.tll.model.key.IBusinessKey;
import com.tll.model.key.INameKey;
import com.tll.model.key.IPrimaryKey;

/**
 * CriteriaFactory
 * @author jpk
 */
public abstract class CriteriaFactory {

	/**
	 * Builds an {@link ICriterion} for use by {@link ICriteria} objects.
	 * @param fieldName
	 * @param fieldValue
	 * @param comp
	 * @param isCaseSensitive
	 */
	public static ICriterion buildCriterion(String fieldName, Object fieldValue, Comparator comp, boolean isCaseSensitive) {
		return new Criterion(fieldName, fieldValue, comp, isCaseSensitive);
	}

	/**
	 * Builds an {@link ICriterion} from an {@link Enum}.
	 * @param fieldName
	 * @param enm the enum
	 * @return ICriterion
	 */
	public static ICriterion buildEnumCriterion(String fieldName, Enum<?> enm) {
		return new Criterion(fieldName, enm, Comparator.EQUALS, false);
	}

	/**
	 * @param key
	 */
	public static ICriterion buildPrimaryKeyCriterion(IPrimaryKey<? extends IEntity> key) {
		return buildCriterion(IEntity.PK_FIELDNAME, key.getId(), Comparator.EQUALS, false);
	}

	/**
	 * Builds a foreign key criterion.
	 * @param <F> The foreign entity type
	 * @param rltdPropName The property name that references the foreign key
	 * @param fkId The id (primary key) of the foreign table
	 */
	public static <F extends IEntity> ICriterion buildForeignKeyCriterion(String rltdPropName, IPrimaryKey<F> fkId) {
		final String fkname = rltdPropName + "." + IEntity.PK_FIELDNAME;
		if(!fkId.isSet()) {
			return buildCriterion(fkname, DBType.NULL, Comparator.IS, false);
		}
		return buildCriterion(fkname, fkId.getId(), Comparator.EQUALS, false);
	}

	/**
	 * Build a criterion entityGroup
	 * @param isConjunction
	 */
	public static ICriterionGroup buildGroup(boolean isConjunction) {
		return new CriterionGroup(isConjunction);
	}

	/**
	 * Builds ICriteria of type {@link CriteriaType#ENTITY}.
	 * @param <E>
	 * @param entityClass
	 * @return Empty entity type criteria instance
	 */
	public static <E extends IEntity> ICriteria<E> buildEntityCriteria(Class<? extends E> entityClass) {
		return new Criteria<E>(entityClass);
	}

	/**
	 * Builds ICriteria of type {@link CriteriaType#ENTITY}.
	 * @param <E>
	 * @param key the primary key to set
	 * @return Entity type criteria instance with the primary key set
	 */
	public static <E extends IEntity> ICriteria<? extends E> buildEntityCriteria(IPrimaryKey<? extends E> key) {
		final ICriteria<E> c = buildEntityCriteria(key.getType());
		c.getPrimaryGroup().addCriterion(buildPrimaryKeyCriterion(key));
		return c;
	}

	/**
	 * Builds ICriteria of type {@link CriteriaType#ENTITY}.
	 * @param <E>
	 * @param key The business key to set
	 * @param isCaseSensitive
	 * @return Entity type criteria instance with the business key set
	 */
	public static <E extends IEntity> ICriteria<? extends E> buildEntityCriteria(IBusinessKey<? extends E> key,
			boolean isCaseSensitive) {
		final ICriteria<? extends E> c = buildEntityCriteria(key.getType());
		final ICriterionGroup g = c.getPrimaryGroup();
		for(final String fname : key.getFieldNames()) {
			g.addCriterion(buildCriterion(fname, key.getFieldValue(fname), Comparator.EQUALS, isCaseSensitive));
		}
		return c;
	}

	/**
	 * Builds ICriteria of type {@link CriteriaType#ENTITY}.
	 * @param <N>
	 * @param key the name key to set
	 * @param isCaseSensitive
	 * @return Entity type criteria instance with the name key set
	 */
	public static <N extends INamedEntity> ICriteria<? extends N> buildEntityCriteria(INameKey<? extends N> key,
			boolean isCaseSensitive) {
		final ICriteria<N> c = buildEntityCriteria(key.getType());
		c.getPrimaryGroup().addCriterion(
				buildCriterion(key.getFieldName(), key.getName(), Comparator.EQUALS, isCaseSensitive));
		return c;
	}

	/**
	 * Builds ICriteria of type {@link CriteriaType#ENTITY}.
	 * @param <E>
	 * @param entityClass
	 * @param fieldName
	 * @param fieldValue
	 * @return Entity type criteria instance with field name/value pair set
	 */
	public static <E extends IEntity> ICriteria<? extends E> buildEntityCriteria(Class<? extends E> entityClass,
			String fieldName, Object fieldValue) {
		return buildEntityCriteria(entityClass, fieldName, fieldValue, Comparator.EQUALS);
	}

	/**
	 * Builds ICriteria of type {@link CriteriaType#ENTITY}.
	 * @param <E>
	 * @param entityClass
	 * @param fieldName
	 * @param fieldValue
	 * @param comp
	 * @return Entity type criteria instance with field name/value pair set with
	 *         the given comparator
	 */
	public static <E extends IEntity> ICriteria<? extends E> buildEntityCriteria(Class<? extends E> entityClass,
			String fieldName, Object fieldValue, Comparator comp) {
		return buildEntityCriteria(entityClass, fieldName, fieldValue, comp, true);
	}

	/**
	 * Builds ICriteria of type {@link CriteriaType#ENTITY}.
	 * @param <E>
	 * @param entityClass
	 * @param fieldName
	 * @param fieldValue
	 * @param comp
	 * @param isCaseSensitive
	 * @return Entity type criteria instance with field name/value pair set with
	 *         the given comparator and case-sensitivity flag set
	 */
	public static <E extends IEntity> ICriteria<? extends E> buildEntityCriteria(Class<? extends E> entityClass,
			String fieldName, Object fieldValue, Comparator comp, boolean isCaseSensitive) {
		final ICriteria<E> criteria = buildEntityCriteria(entityClass);
		criteria.getPrimaryGroup().addCriterion(buildCriterion(fieldName, fieldValue, comp, isCaseSensitive));
		return criteria;
	}

	/**
	 * Builds foreign key criteria
	 * @param <E> the primary entity type
	 * @param <F> the foreign entity type
	 * @param entityClass the primary entity class
	 * @param rltdPropName The property name of the entity owning the foreign key
	 * @param fkId the id of the foreign table record
	 * @return {@link ICriteria}
	 */
	public static <E extends IEntity, F extends IEntity> ICriteria<? extends E> buildForeignKeyCriteria(
			Class<E> entityClass, String rltdPropName, IPrimaryKey<F> fkId) {
		final ICriteria<? extends E> criteria = buildEntityCriteria(entityClass);
		criteria.getPrimaryGroup().addCriterion(buildForeignKeyCriterion(rltdPropName, fkId));
		return criteria;
	}

	/**
	 * Builds criteria that points to a defined named query.
	 * @param namedQuery The named query definition
	 * @param queryParams The possible query parameters
	 * @return New {@link ICriteria} instance wired to invoke the given named
	 *         query.
	 */
	public static ICriteria<? extends IEntity> buildQueryCriteria(SelectNamedQuery namedQuery,
			Map<String, String> queryParams) {
		return new Criteria<IEntity>(namedQuery, queryParams);
	}
}
