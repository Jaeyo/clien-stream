set CURRENT_PATH=%~dp0
set CLASSPATH=%CURRENT_PATH%\..\WEB-INF\lib\*;%CURRENT_PATH%\PresentationChat-0.0.1-SNAPSHOT.jar

"%JAVA_HOME%\bin\java" -cp %CLASSPATH% org.lasti.PresentationChat.App