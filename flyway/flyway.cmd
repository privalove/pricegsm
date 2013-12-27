@REM
@REM Copyright (C) 2010-2012 the original author or authors.
@REM
@REM Licensed under the Apache License, Version 2.0 (the "License");
@REM you may not use this file except in compliance with the License.
@REM You may obtain a copy of the License at
@REM
@REM         http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing, software
@REM distributed under the License is distributed on an "AS IS" BASIS,
@REM WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@REM See the License for the specific language governing permissions and
@REM limitations under the License.
@REM

@Echo off

setlocal

@REM Save current directory
set OLDDIR=%CD%

@REM Set the current directory to the installation directory
chdir /d %~dp0

@REM Use JAVA_HOME if it is set
if "%JAVA_HOME%"=="" (
 set JAVA_CMD=java
) else (
 set JAVA_CMD="%JAVA_HOME%\bin\java.exe"
)


if not exist bin (
  mkdir bin
)
if not exist bin/flyway-commandline-2.1.jar (
  call mvn org.apache.maven.plugins:maven-dependency-plugin:2.6:get -Dartifact=com.googlecode.flyway:flyway-commandline:2.1 -Ddest=bin/
)
if not exist bin/flyway-core-2.1.jar (
  call mvn org.apache.maven.plugins:maven-dependency-plugin:2.6:get -Dartifact=com.googlecode.flyway:flyway-core:2.1 -Ddest=bin/ -Dtransitive=false
)
if not exist jars (
  mkdir jars
)
if not exist jars/postgresql-9.1-901-1.jdbc4.jar (
  call mvn org.apache.maven.plugins:maven-dependency-plugin:2.6:get -Dartifact=postgresql:postgresql:9.1-901-1.jdbc4 -Ddest=jars/
)


%JAVA_CMD% -cp bin\flyway-commandline-2.1.jar;bin\flyway-core-2.1.jar;bin\spring-jdbc-2.5.6.jar;bin\commons-logging-1.1.1.jar;bin\spring-beans-2.5.6.jar;bin\spring-core-2.5.6.jar;bin\spring-context-2.5.6.jar;bin\aopalliance-1.0.jar;bin\spring-tx-2.5.6.jar;bin\log4j-1.2.16.jar com.googlecode.flyway.commandline.Main %*

@REM Save the exit code
set JAVA_EXIT_CODE=%ERRORLEVEL%

@REM Restore current directory
chdir /d %OLDDIR%

@REM Exit using the same code returned from Java
EXIT /B %JAVA_EXIT_CODE%
