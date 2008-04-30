/*
 * The Logic Lab 
 */
package com.tll.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.inject.Module;
import com.tll.TestBase;
import com.tll.guice.JpaModule;

/**
 * JpaConfigTest
 * Attempts to verify the Hibernate impl of JPA for the app
 * @author jpk
 */
public class JpaConfigTest extends TestBase {

  @Override
  protected void addModules(List<Module> modules) {
    super.addModules(modules);
    modules.add(new JpaModule(JpaMode.LOCAL));
  }
  
  @Test(groups = "jpa")
  public void test() throws Exception {
    EntityManager em = injector.getInstance(EntityManager.class);
    Assert.assertNotNull(em);
  }

}
