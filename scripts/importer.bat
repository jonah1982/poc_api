set APP_PATH=\opt\importer
set CLASS_PATH=%APP_PATH%\classes
set LIB_DIR=%APP_PATH%\lib

set CLASS_PATH=%CLASS_PATH%;%LIB_DIR%\com.fasterxml.jackson.annotations.jar
set CLASS_PATH=%CLASS_PATH%;%LIB_DIR%\com.fasterxml.jackson.core.jar
set CLASS_PATH=%CLASS_PATH%;%LIB_DIR%\com.fasterxml.jackson.databind.jar
set CLASS_PATH=%CLASS_PATH%;%LIB_DIR%\commons-beanutils-1.9.2.jar
set CLASS_PATH=%CLASS_PATH%;%LIB_DIR%\commons-codec-1.8.jar
set CLASS_PATH=%CLASS_PATH%;%LIB_DIR%\commons-collections-3.2.1.jar
set CLASS_PATH=%CLASS_PATH%;%LIB_DIR%\commons-dbutils-1.5.jar
set CLASS_PATH=%CLASS_PATH%;%LIB_DIR%\commons-io-2.4.jar
set CLASS_PATH=%CLASS_PATH%;%LIB_DIR%\commons-lang3-3.2.1.jar
set CLASS_PATH=%CLASS_PATH%;%LIB_DIR%\commons-logging-1.1.3.jar
set CLASS_PATH=%CLASS_PATH%;%LIB_DIR%\commons-math3-3.2.jar
set CLASS_PATH=%CLASS_PATH%;%LIB_DIR%\commons-pool-1.4.jar
set CLASS_PATH=%CLASS_PATH%;%LIB_DIR%\fluent-hc-4.3.3.jar
set CLASS_PATH=%CLASS_PATH%;%LIB_DIR%\gson-2.2.2.jar
set CLASS_PATH=%CLASS_PATH%;%LIB_DIR%\guava-14.0.1.jar
set CLASS_PATH=%CLASS_PATH%;%LIB_DIR%\joda-time-2.3.jar
set CLASS_PATH=%CLASS_PATH%;%LIB_DIR%\jsch-0.1.50.jar
set CLASS_PATH=%CLASS_PATH%;%LIB_DIR%\jstl.jar
set CLASS_PATH=%CLASS_PATH%;%LIB_DIR%\log4j-1.2.13.jar
set CLASS_PATH=%CLASS_PATH%;%LIB_DIR%\mongo-java-driver-2.12.0.jar
set CLASS_PATH=%CLASS_PATH%;%LIB_DIR%\scala-library-2.10.4.jar
set CLASS_PATH=%CLASS_PATH%;%LIB_DIR%\slf4j-api-1.7.5.jar

echo %CLASS_PATH%

java -cp %CLASS_PATH% -Dimport.datafile.plugin=LgwDataFile com.visenti.waterwise.jdaemon.importer.CustomImporter /Users/YANLING/Desktop/ lgw



