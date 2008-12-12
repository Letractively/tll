/*
 * The Logic Lab 
 */
package com.tll.model;

import com.tll.model.key.IPrimaryKeyGenerator;

/**
 * MockPrimaryKeyGenerator
 * @author jpk
 */
public class MockPrimaryKeyGenerator implements IPrimaryKeyGenerator {

	static int nextId = 0;
	
  public synchronized Integer generateIdentifier(Class<? extends IEntity> entityClass) {
    //return RandomUtils.nextInt();
  	return ++nextId;
  }

}
