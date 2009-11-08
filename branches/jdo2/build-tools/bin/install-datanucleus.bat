rem ****************************************************
rem Installs DataNucleus libs into the local maven repo.
rem ****************************************************

set /P DNHOME="DataNucleus install dir?"
set DN_VCACHE=1.1.1
set DN_VPOOL=1.0.2
set DN_VCORE=1.1.6
set DN_VDB4O=1.1.2
set DN_VENHANCE=1.1.4
set DN_VRDBMS=1.1.6

call mvn install:install-file -Dfile=%DNHOME%\lib\datanucleus-cache-%DN_VCACHE%.jar -DgroupId=org.datanucleus -DartifactId=datanucleus-cache -Dversion=%DN_VCACHE% -Dpackaging=jar -DgeneratePom=true
call mvn install:install-file -Dfile=%DNHOME%\lib\datanucleus-connectionpool-%DN_VPOOL%.jar -DgroupId=org.datanucleus -DartifactId=datanucleus-connectionpool -Dversion=%DN_VPOOL% -Dpackaging=jar -DgeneratePom=true
call mvn install:install-file -Dfile=%DNHOME%\lib\datanucleus-core-%DN_VCORE%.jar -DgroupId=org.datanucleus -DartifactId=datanucleus-core -Dversion=%DN_VCORE% -Dpackaging=jar -DgeneratePom=true
call mvn install:install-file -Dfile=%DNHOME%\lib\datanucleus-db4o-%DN_VDB4O%.jar -DgroupId=org.datanucleus -DartifactId=datanucleus-db4o -Dversion=%DN_VDB4O% -Dpackaging=jar -DgeneratePom=true
call mvn install:install-file -Dfile=%DNHOME%\lib\datanucleus-enhancer-%DN_VENHANCE%.jar -DgroupId=org.datanucleus -DartifactId=datanucleus-enhancer -Dversion=%DN_VENHANCE% -Dpackaging=jar -DgeneratePom=true
call mvn install:install-file -Dfile=%DNHOME%\lib\datanucleus-rdbms-%DN_VRDBMS%.jar -DgroupId=org.datanucleus -DartifactId=datanucleus-rdbms -Dversion=%DN_VRDBMS% -Dpackaging=jar -DgeneratePom=true

pause