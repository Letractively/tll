/**
 * The Logic Lab
 * @author jpk
 * @since Apr 11, 2010
 */
package com.tll.server.rpc.entity;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import com.tll.common.data.ModelPayload;
import com.tll.common.model.IEntityType;
import com.tll.common.model.Model;
import com.tll.common.msg.Msg.MsgAttr;
import com.tll.common.msg.Msg.MsgLevel;
import com.tll.model.IEntity;
import com.tll.model.ISchemaInfo;
import com.tll.model.ISchemaProperty;
import com.tll.server.marshal.MarshalOptions;
import com.tll.util.PropertyPath;


/**
 * 
 * @author jpk
 */
public class PersistHelper {

	/**
	 * Client-izes the given property path (need to account for possible nested).
	 * <p>
	 * It is assumed nested entities are only 1-level deep
	 * @param <T> the entity type
	 * @param schemaInfo
	 * @param entityClass
	 * @param path
	 * @return the clientized path
	 */
	public static final <T> String clientizePropertyPath(ISchemaInfo schemaInfo, Class<T> entityClass, String path) {
		final PropertyPath p = new PropertyPath(path);
		if(p.depth() > 2) {
			final String ppp = p.trim(1);
			final ISchemaProperty sp = schemaInfo.getSchemaProperty(entityClass, ppp);
			if(sp.getPropertyType().isNested()) {
				path = ppp + '_' + p.last();
			}
		}
		return path;
	}

	
	@SuppressWarnings("unchecked")
	public static void handleValidationException(PersistContext context, ConstraintViolationException cve, ModelPayload payload) {
		//final Class<? extends IEntity> entityClass =
			//(Class<? extends IEntity>) context.getEntityTypeResolver().resolveEntityClass(model.getEntityType());
		for(final ConstraintViolation<?> iv : cve.getConstraintViolations()) {
			// resolve index if we have a violation on under an indexed entity property
			// since the validation api doesn't provide the index rather only empty brackets ([])
			// in the ConstraintViolation's propertyPath property
			Class<? extends IEntity> entityClass = (Class<? extends IEntity>) iv.getRootBeanClass();
			payload.getStatus().addMsg(iv.getMessage(), MsgLevel.ERROR, MsgAttr.FIELD.flag,
					clientizePropertyPath(context.getSchemaInfo(), entityClass, iv.getPropertyPath().toString()));
		}
	}

	/**
	 * Convenience method for entity to model marshaling.
	 * @param context
	 * @param entityType
	 * @param entity
	 * @return new {@link Model} instance
	 * @throws RuntimeException upon marshaling related error
	 */
	public static Model marshal(PersistContext context, IEntityType entityType, IEntity entity) throws RuntimeException {
		MarshalOptions mo;
		try {
			mo = context.getMarshalOptionsResolver().resolve(entityType);
		}
		catch(final IllegalArgumentException e) {
			// default fallback
			mo = MarshalOptions.NO_REFERENCES;
		}
		final Model m = context.getMarshaler().marshalEntity(entity, mo);
		assert m != null;
		return m;
	}

	private PersistHelper() {}
}
