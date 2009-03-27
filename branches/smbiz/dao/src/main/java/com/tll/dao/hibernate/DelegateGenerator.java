package com.tll.dao.hibernate;

import java.io.Serializable;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerationException;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.IdentifierGeneratorFactory;
import org.hibernate.type.Type;

import com.tll.model.IEntity;

/**
 * ID generation scheme that delegates the id generation strategy based on
 * whether the entity has an id or not.
 * 
 * @author jpk
 */
public class DelegateGenerator implements IdentifierGenerator, Configurable {

  /**
   * The generator parameter specifies the underlying id generator to use when
   * an identifier hasn't already been assigned.
   */
  public static final String DELEGATE = "delegate";

  private IdentifierGenerator assignedGenerator;

  private IdentifierGenerator delegateGenerator;

  /**
   * @param sessionImplementor
   * @param object
   * @return an id
   * @throws HibernateException
   */
  public Serializable generate(SessionImplementor sessionImplementor, Object object) throws HibernateException {

    if (object == null) {
      // Use the underlying id generation strategy if no object is specified
      // Note that this should *only* occur when using DAO generation
      return delegateGenerator.generate(sessionImplementor, object);
    }

    if (object instanceof IEntity) {
      if (((IEntity) object).isGenerated()) {
        return assignedGenerator.generate(sessionImplementor, object);
      }
      return delegateGenerator.generate(sessionImplementor, object);
    }
    throw new IdentifierGenerationException(
        "the delegate generator is only intended for use with implementors of IEntity");
  }

  /**
   * @param type
   * @param params
   * @param d
   * @throws MappingException
   */
  public void configure(Type type, Properties params, Dialect d) throws MappingException {
    
    String generatorName = params.getProperty(DELEGATE);
    if (generatorName == null) {
      throw new MappingException("param named " + DELEGATE + " is required for delegate generation strategy");
    }

    // Create the assigned and delegate id generators
    assignedGenerator = IdentifierGeneratorFactory.create("assigned", type, params, d);
    delegateGenerator = IdentifierGeneratorFactory.create(generatorName, type, params, d);
  }

}
