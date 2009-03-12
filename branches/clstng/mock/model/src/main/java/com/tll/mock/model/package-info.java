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

package com.tll.mock.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.tll.model.EncryptedObjectType;

