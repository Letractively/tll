@GenericGenerator(name = "entity", strategy = "com.tll.dao.orm.DelegateGenerator",
  parameters = {
    @Parameter(name="delegate", value="hilo")
  }
)

package com.tll.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

