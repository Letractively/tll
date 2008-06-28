/*
 * The Logic Lab 
 */
package com.tll.dao.hibernate.event;

import java.io.Serializable;

import org.hibernate.ejb.event.EJB3MergeEventListener;
import org.hibernate.event.EventSource;

import com.tll.model.EntityBase;

/**
 * MergeEventListener
 * 
 * @author jpk
 */
public class MergeEventListener extends EJB3MergeEventListener {
  private static final long serialVersionUID = 6153951146980391652L;

  @Override
  protected Serializable saveWithGeneratedId(Object entity, String entityName, Object anything, EventSource source,
      boolean requiresImmediateIdAccess) {
    if(entity instanceof EntityBase) {
      final EntityBase eb = (EntityBase) entity;
      if(eb.getVersion() != null) {
        throw new IllegalStateException("Attempting to save an already persisted entity!");
      }
      eb.setGenerated(eb.getId());
    }
    Serializable s = super.saveWithGeneratedId(entity, entityName, anything, source, requiresImmediateIdAccess);
    return s;
  }

}
