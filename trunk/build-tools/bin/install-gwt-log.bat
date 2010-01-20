rem ****************************************************
rem Installs gwt-log to local maven repo.
rem ****************************************************

set /P LOC="gwt-log jar location? "
set /P VERSION="gwt-log version? "

call mvn install:install-file -Dfile=%LOC%\gwt-log-%VERSION%.jar -DgroupId=com.allen_sauer.gwt.log -DartifactId=gwt-log -Dversion=%VERSION% -Dpackaging=jar -DgeneratePom=true

pause