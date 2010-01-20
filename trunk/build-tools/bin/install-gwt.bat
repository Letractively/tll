rem ***********************************************************
rem Installs GWT into the local maven repo from GWT source root.
rem NOTE: GWT project is assumed to be built.
rem ***********************************************************

set /P GWT_INSTALL_DIR="GWT install dir? "
set /P GWTV="GWT version? "

call mvn install:install-file -Dfile=%GWT_INSTALL_DIR%\gwt-api-checker.jar -DgroupId=com.google.gwt -DartifactId=gwt-api-checker -Dversion=%GWTV% -Dpackaging=jar -DgeneratePom=true
call mvn install:install-file -Dfile=%GWT_INSTALL_DIR%\gwt-dev.jar -DgroupId=com.google.gwt -DartifactId=gwt-dev -Dversion=%GWTV% -Dpackaging=jar -DgeneratePom=true
call mvn install:install-file -Dfile=%GWT_INSTALL_DIR%\gwt-servlet.jar -DgroupId=com.google.gwt -DartifactId=gwt-servlet -Dversion=%GWTV% -Dpackaging=jar -DgeneratePom=true
call mvn install:install-file -Dfile=%GWT_INSTALL_DIR%\gwt-soyc-vis.jar -DgroupId=com.google.gwt -DartifactId=gwt-soyc-vis -Dversion=%GWTV% -Dpackaging=jar -DgeneratePom=true
call mvn install:install-file -Dfile=%GWT_INSTALL_DIR%\gwt-user.jar -DgroupId=com.google.gwt -DartifactId=gwt-user -Dversion=%GWTV% -Dpackaging=jar -DgeneratePom=true

pause