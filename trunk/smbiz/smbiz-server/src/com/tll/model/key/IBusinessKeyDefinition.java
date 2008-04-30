package com.tll.model.key;

import com.tll.model.IEntity;

/**
 * Business key: Represents a unique key serving as an alternative to a primary
 * key for entities. A business key only applies to a single entity
 * class. These keys should corres. to db-level unique constraints for a table
 * <p>
 * A business key definition is able to provide the names of the fields
 * constituting the business key.
 * 
 * @author jpk
 */
public interface IBusinessKeyDefinition<E extends IEntity> extends IEntityKey<E> {

  /**
   * @return The field names
   */
  String[] getFieldNames();
}