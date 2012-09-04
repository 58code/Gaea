@echo off

rem ======================================================================
rem                                                                       
rem                       GAEA server bootstrap script                     
rem                                                                      
rem ======================================================================


set "USAGE=Usage: startup.bat <service-name> [<other-gaea-config>]"
set "SYSTEM_PROPERTY="

rem if no args specified, show usage
if not "%1" == "" goto args_ok
echo "%USAGE%"
goto end


:args_ok
rem get arguments
set "SERVICE_NAME=%1"
set "OTHER_GAEA_CONFIG=%2%3%4%5%6%7%8%9"


rem get path
set DIR=%cd%
set ROOT_PATH=%DIR%/../
set DEPLOY_PATH=%DIR%/../service/deploy
set PID_PATH=%DIR%/../tmp/pid
#set TOOLS_JAR_PATH=%JAVA_HOME/%lib/tools.jar
set TOOLS_JAR_PATH=%ROOT_PATH%/lib/tools.jar




rem check tools.jar
if "%TOOLS_JAR_PATH%" == "" (goto tools_jar_not_exist) else (goto vm_args)

:tools_jar_not_exist
echo "Can't find tools.jar in JAVA_HOME"
echo "Need a JDK to run javac"
goto end

rem java opts
:vm_args
if not "%VM_XMS%" == "" goto vm_xmx
set VM_XMS=256m

:vm_xmx
if not "%VM_XMX%" == "" goto vm_xmn
set VM_XMX=256m

:vm_xmn
if not "%VM_XMN%" == "" goto java_opts
set VM_XMN=128m

:java_opts
set JAVA_OPTS=-Xms%VM_XMS% -Xmx%VM_XMX% -Xmn%VM_XMN% -Xss1024K -XX:PermSize=64m -XX:MaxPermSize=64m -XX:ParallelGCThreads=20 -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:+UseCMSCompactAtFullCollection -XX:SurvivorRatio=65536 -XX:MaxTenuringThreshold=0 -XX:CMSInitiatingOccupancyFraction=80


rem class path
set CLASS_PATH=.;%TOOLS_JAR_PATH%
cd %ROOT_PATH%lib/
SETLOCAL ENABLEDELAYEDEXPANSION 
for /f %%i in ('dir /b /s "*.jar"') do (
	set str=%%i
	set str=!str:\=/!
	set CLASS_PATH=!CLASS_PATH!;!str!
)

rem main class
set MAIN_CLASS=com.bj58.spat.gaea.server.bootstrap.Main

java %JAVA_OPTS% -classpath %CLASS_PATH% -Duser.dir=%DIR% %SYSTEM_PROPERTY% %MAIN_CLASS% %OTHER_GAEA_CONFIG% -Dgaea.service.name=%SERVICE_NAME%

:end