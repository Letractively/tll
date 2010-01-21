rem ****************************************************
rem Installs org.springextensions.db4o.jar
rem NOTE: This is a customized version that supports the db4o v7.4
rem ****************************************************

set LOC=%TLL_ROOT%\build-tools\lib
set VERSION=1.0.0e
set GID=org.springextensions.db4o
set JAR=%GID%-%VERSION%.jar
set SOURCES=%GID%-%VERSION%-sources.jar

call mvn install:install-file -Dfile=%LOC%\%JAR% -DgroupId=org.springextensions.db4o -DartifactId=org.springextensions.db4o -Dversion=%VERSION% -Dpackaging=jar -DgeneratePom=true -Dsources=%SOURCES%

pause