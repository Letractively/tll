/*
 * NOTE: This file ends up being spurious since the hibernate JPA loading scheme 
 * requires it be in the *same location* as the actual defined concrete entities.
 * So, this file has to be copied and present in the implementing model package (jar). 
 */
@TypeDefs(
  {
    @TypeDef(name="encobj", typeClass = EncryptedObjectType.class)
  }
)

@GenericGenerator(name = "entity", strategy = "com.tll.dao.orm.DelegateGenerator",
  parameters = {
    @Parameter(name="delegate", value="hilo")
  }
)

package com.tll.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

