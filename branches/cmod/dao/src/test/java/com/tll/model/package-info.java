@TypeDefs(
  {
    @TypeDef(name="encobj", typeClass = EncryptedObjectType.class)
  }
)

@GenericGenerator(name="entity", strategy="com.tll.dao.hibernate.DelegateGenerator",
  parameters = {
    @Parameter(name="delegate", value="hilo")
  }
)

package com.tll.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

// TODO figure out how to reference the original package-info asset in the tll-model module!
