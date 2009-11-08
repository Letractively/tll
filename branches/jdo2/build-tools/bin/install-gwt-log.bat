rem ****************************************************
rem Installs gwt-log to local maven repo.
rem ****************************************************

set /P LOC="gwt-log-2.6.2.jar location (specify the parent dir)?"
set VERSION=2.6.2

call mvn install:install-file -Dfile=%LOC%\gwt-log-2.6.2.jar -DgroupId=com.allen_sauer.gwt.log -DartifactId=gwt-log -Dversion=%VERSION% -Dpackaging=jar -DgeneratePom=true

pause