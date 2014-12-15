set CURRENT_PATH=%~dp0
set CLASSPATH=%CURRENT_PATH%\..\WEB-INF\lib\*;%CURRENT_PATH%\clien-stream-0.0.1-SNAPSHOT.jar

"%JAVA_HOME%\bin\java" -cp %CLASSPATH% org.jaeyo.clien_stream.App