/**
 * The Logic Lab
 * @author jpk
 * Dec 22, 2007
 */
package com.tll.model.validate;

import java.util.HashMap;
import java.util.Map;

import com.tll.model.IEntity;


/**
 * EntityValidatorFactory
 * @author jpk
 */
public final class EntityValidatorFactory {

	private final static Map<Class<? extends IEntity>, IEntityValidator<? extends IEntity>> map = 
		new HashMap<Class<? extends IEntity>, IEntityValidator<? extends IEntity>>();
	
	@SuppressWarnings("unchecked")
	public static <E extends IEntity> IEntityValidator<E> instance(Class<E> entityType) {
		IEntityValidator<E> validator = (IEntityValidator<E>) map.get(entityType);
		if(validator == null) {
			validator = new ClassValidatorDelegate<E>(entityType);
			map.put(entityType, validator);
		}
		return validator;
	}
}
