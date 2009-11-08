rem ***************************************
rem Installs GWT into the local maven repo.
rem ***************************************

set /P GWT_HOME="GWT install dir (NOT source tree root)?"
set /P GWTV="GWT version?"

call mvn install:install-file -Dfile=%GWT_HOME%\dist\gwt-dev.jar -DgroupId=com.google.gwt -DartifactId=gwt-dev -Dversion=%GWTV% -Dpackaging=jar -DgeneratePom=true
call mvn install:install-file -Dfile=%GWT_HOME%\dist\gwt-servlet.jar -DgroupId=com.google.gwt -DartifactId=gwt-servlet -Dversion=%GWTV% -Dpackaging=jar -DgeneratePom=true
call mvn install:install-file -Dfile=%GWT_HOME%\dist\gwt-user.jar -DgroupId=com.google.gwt -DartifactId=gwt-user -Dversion=%GWTV% -Dpackaging=jar -DgeneratePom=true
call mvn install:install-file -Dfile=%GWT_HOME%\dist\gwt-soyc-vis.jar -DgroupId=com.google.gwt -DartifactId=gwt-soyc-vis -Dversion=%GWTV% -Dpackaging=jar -DgeneratePom=true
call mvn install:install-file -Dfile=%GWT_HOME%\dist\gwt-api-checker.jar -DgroupId=com.google.gwt -DartifactId=gwt-api-checker -Dversion=%GWTV% -Dpackaging=jar -DgeneratePom=true

pause