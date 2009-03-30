package com.tll.dao.hibernate;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.hibernate.persister.entity.EntityPersister;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.tll.model.IEntity;
import com.tll.model.key.IPrimaryKeyGenerator;

/**
 * PrimaryKeyGenerator - Hibernate-specific impl of {@link IPrimaryKeyGenerator}
 * .
 * <p>
 * This class generates the identifier based on the identifier generation scheme
 * defined for a specific entity class. The strategy must be able to generate an
 * id without first inserting a row into the persistence store. This id
 * generation methodology should be used for generating ids when an entity is
 * first created.
 * <p>
 * This scheme allows us to use the primary key as the unique identifier
 * throughout the lifecycle of an entity bean. Therefore, hashCode and equals
 * can be based on the primary key identifier.
 * @author jpk
 */
public class PrimaryKeyGenerator extends HibernateJpaSupport implements IPrimaryKeyGenerator {

	private static Map<Class<? extends IEntity>, IdentifierGenerator> entityIdentifierGenerators =
			new HashMap<Class<? extends IEntity>, IdentifierGenerator>();

	/**
	 * Constructor
	 * @param emPrvdr
	 */
	@Inject
	public PrimaryKeyGenerator(Provider<EntityManager> emPrvdr) {
		super(emPrvdr);
	}

	private SessionImplementor getSessionImplementor() {
		Session session = (Session) emPrvdr.get().getDelegate();
		return (SessionImplementor) session;
	}

	/**
	 * Generates the identifier using the id strategy defined for the specified
	 * entity class.
	 * @param entityClass an entity class (must implement IEntity)
	 * @return a unique identifier
	 */
	public Integer generateIdentifier(Class<? extends IEntity> entityClass) {
		// IMPORTANT - Note the null object used in id generation. This only
		// works with specific identifier
		// generators such as sequence and the like that don't rely on a row
		// first being created in the database
		return (Integer) getIdentifierGenerator(entityClass).generate(getSessionImplementor(), null);
	}

	/**
	 * Returns a hibernate id generator strategy based on the input class. The
	 * input class must implement the IEntity interface.
	 * @param entityClass an entity class (must implement IEntity)
	 * @return the id generator for the class
	 */
	protected IdentifierGenerator getIdentifierGenerator(Class<? extends IEntity> entityClass) {
		IdentifierGenerator idGenerator = entityIdentifierGenerators.get(entityClass);
		if(idGenerator == null) {
			synchronized (entityIdentifierGenerators) {
				idGenerator = entityIdentifierGenerators.get(entityClass);
				if(idGenerator == null) {
					EntityPersister persister =
							(AbstractEntityPersister) getSessionImplementor().getFactory().getClassMetadata(entityClass);
					idGenerator = persister.getIdentifierGenerator();
					entityIdentifierGenerators.put(entityClass, idGenerator);
				}
			}
		}
		return idGenerator;
	}
}
