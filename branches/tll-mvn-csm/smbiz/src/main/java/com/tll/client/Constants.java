package com.tll.client;

/**
 * Interface to represent the constants contained in resource bundle:
 * 	'C:/development/tll/smbiz/target/classes/com/tll/client/Constants.properties'.
 */
public interface Constants extends com.google.gwt.i18n.client.Constants {
  
  /**
   * Translated "0.9.0-SNAPSHOT".
   * 
   * @return translated "0.9.0-SNAPSHOT"
   */
  @DefaultStringValue("0.9.0-SNAPSHOT")
  @Key("appVersion")
  String appVersion();

  /**
   * Translated "dev".
   * 
   * @return translated "dev"
   */
  @DefaultStringValue("dev")
  @Key("debug")
  String debug();

  /**
   * Translated "dev".
   * 
   * @return translated "dev"
   */
  @DefaultStringValue("dev")
  @Key("environment")
  String environment();
}
