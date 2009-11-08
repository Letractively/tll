rem *********************************************
rem Installs db4o libs into the local maven repo.
rem *********************************************

set /P DB40_HOME="db4o install dir?"
set /P DB40V="db4o version (E.g.: 7.4.88.12908)?"
cd %DB40_HOME%\lib

call mvn install:install-file -Dfile=.\db4o-%DB40V%-java5.jar -DgroupId=db4o -DartifactId=db4o -Dversion=%DB40V% -Dpackaging=jar -DgeneratePom=true

pause