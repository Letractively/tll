rem ***********************************************************
rem Installs GWT into the local maven repo from GWT source root.
rem NOTE: GWT project is assumed to be built.
rem ***********************************************************

set /P GWT_HOME="GWT source root dir?"
set /P GWTV="GWT version?"

call mvn install:install-file -Dfile=%GWT_HOME%\build\lib\ant-gwt.jar -DgroupId=com.google.gwt -DartifactId=ant-gwt -Dversion=%GWTV% -Dpackaging=jar -DgeneratePom=true
call mvn install:install-file -Dfile=%GWT_HOME%\build\lib\gwt-api-checker.jar -DgroupId=com.google.gwt -DartifactId=gwt-api-checker -Dversion=%GWTV% -Dpackaging=jar -DgeneratePom=true
call mvn install:install-file -Dfile=%GWT_HOME%\build\lib\gwt-benchmark-viewer.war -DgroupId=com.google.gwt -DartifactId=gwt-benchmark-viewer -Dversion=%GWTV% -Dpackaging=war -DgeneratePom=true
call mvn install:install-file -Dfile=%GWT_HOME%\build\lib\gwt-customchecks.war -DgroupId=com.google.gwt -DartifactId=gwt-customchecks -Dversion=%GWTV% -Dpackaging=jar -DgeneratePom=true
call mvn install:install-file -Dfile=%GWT_HOME%\build\lib\gwt-dev.jar -DgroupId=com.google.gwt -DartifactId=gwt-dev -Dversion=%GWTV% -Dpackaging=jar -DgeneratePom=true
call mvn install:install-file -Dfile=%GWT_HOME%\build\lib\gwt-doctool.jar -DgroupId=com.google.gwt -DartifactId=gwt-doctool -Dversion=%GWTV% -Dpackaging=jar -DgeneratePom=true
call mvn install:install-file -Dfile=%GWT_HOME%\build\lib\gwt-servlet.jar -DgroupId=com.google.gwt -DartifactId=gwt-servlet -Dversion=%GWTV% -Dpackaging=jar -DgeneratePom=true
call mvn install:install-file -Dfile=%GWT_HOME%\build\lib\gwt-soyc-vis.jar -DgroupId=com.google.gwt -DartifactId=gwt-soyc-vis -Dversion=%GWTV% -Dpackaging=jar -DgeneratePom=true
call mvn install:install-file -Dfile=%GWT_HOME%\build\lib\gwt-user.jar -DgroupId=com.google.gwt -DartifactId=gwt-user -Dversion=%GWTV% -Dpackaging=jar -DgeneratePom=true

pause