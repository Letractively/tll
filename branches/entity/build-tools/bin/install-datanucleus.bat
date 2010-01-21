rem ****************************************************
rem Installs DataNucleus libs into the local maven repo.
rem ****************************************************

set /P DNHOME="DataNucleus 2.0.0-m3 *full-deps* install dir? "
set DN_VCACHE=2.0.0-m2
set DN_VPOOL=2.0.0-m1
set DN_VCORE=2.0.0-m3
set DN_VDB4O=2.0.0-m2
set DN_VENHANCE=2.0.0-m3
set DN_VRDBMS=2.0.0-m3

rem set /P DNHOME="DataNucleus 1.1.6 *full-deps* install dir? "
rem set DN_VCACHE=1.1.1
rem set DN_VPOOL=1.0.2
rem set DN_VCORE=1.1.6
rem set DN_VDB4O=1.1.2
rem set DN_VENHANCE=1.1.4
rem set DN_VRDBMS=1.1.6

call mvn install:install-file -Dfile=%DNHOME%\deps\jdo2-api-2.3-eb.jar -DgroupId=javax.jdo -DartifactId=jdo2-api -Dversion=2.3-eb -Dpackaging=jar -DgeneratePom=true

call mvn install:install-file -Dfile=%DNHOME%\lib\datanucleus-cache-%DN_VCACHE%.jar -DgroupId=org.datanucleus -DartifactId=datanucleus-cache -Dversion=%DN_VCACHE% -Dpackaging=jar -DgeneratePom=true
call mvn install:install-file -Dfile=%DNHOME%\lib\datanucleus-connectionpool-%DN_VPOOL%.jar -DgroupId=org.datanucleus -DartifactId=datanucleus-connectionpool -Dversion=%DN_VPOOL% -Dpackaging=jar -DgeneratePom=true
call mvn install:install-file -Dfile=%DNHOME%\lib\datanucleus-core-%DN_VCORE%.jar -DgroupId=org.datanucleus -DartifactId=datanucleus-core -Dversion=%DN_VCORE% -Dpackaging=jar -DgeneratePom=true
call mvn install:install-file -Dfile=%DNHOME%\lib\datanucleus-db4o-%DN_VDB4O%.jar -DgroupId=org.datanucleus -DartifactId=datanucleus-db4o -Dversion=%DN_VDB4O% -Dpackaging=jar -DgeneratePom=true
call mvn install:install-file -Dfile=%DNHOME%\lib\datanucleus-enhancer-%DN_VENHANCE%.jar -DgroupId=org.datanucleus -DartifactId=datanucleus-enhancer -Dversion=%DN_VENHANCE% -Dpackaging=jar -DgeneratePom=true
call mvn install:install-file -Dfile=%DNHOME%\lib\datanucleus-rdbms-%DN_VRDBMS%.jar -DgroupId=org.datanucleus -DartifactId=datanucleus-rdbms -Dversion=%DN_VRDBMS% -Dpackaging=jar -DgeneratePom=true

pause