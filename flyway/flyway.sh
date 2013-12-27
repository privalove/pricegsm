#!/bin/bash
#
# Copyright (C) 2010-2011 the original author or authors.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#         http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

# Save current directory
OLDDIR=`pwd`

# Set the current directory to the installation directory
INSTALLDIR=`dirname $0`
cd "$INSTALLDIR"

# Use JAVA_HOME if it is set
if [ -z $JAVA_HOME ]; then
 JAVA_CMD=java
else
 JAVA_CMD=$JAVA_HOME/bin/java
fi


if [ ! -d bin ]; then
  mkdir bin
fi
if [ ! -f bin/flyway-commandline-2.1.jar ]; then
  mvn org.apache.maven.plugins:maven-dependency-plugin:2.6:get -Dartifact=com.googlecode.flyway:flyway-commandline:2.1 -Ddest=bin/
fi
if [ ! -f bin/flyway-core-2.1.jar ]; then
  mvn org.apache.maven.plugins:maven-dependency-plugin:2.6:get -Dartifact=com.googlecode.flyway:flyway-core:2.1 -Ddest=bin/ -Dtransitive=false
fi

if [ ! -d jars ]; then
  mkdir jars
fi
if [ ! -f jars/postgresql-9.1-901-1.jdbc4.jar ]; then
  mvn org.apache.maven.plugins:maven-dependency-plugin:2.6:get -Dartifact=postgresql:postgresql:9.1-901-1.jdbc4 -Ddest=jars/
fi


$JAVA_CMD -cp bin/flyway-commandline-2.1.jar:bin/flyway-core-2.1.jar:bin/spring-jdbc-2.5.6.jar:bin/commons-logging-1.1.1.jar:bin/spring-beans-2.5.6.jar:bin/spring-core-2.5.6.jar:bin/spring-context-2.5.6.jar:bin/aopalliance-1.0.jar:bin/spring-tx-2.5.6.jar:bin/log4j-1.2.16.jar com.googlecode.flyway.commandline.Main $@

# Save the exit code
JAVA_EXIT_CODE=$?

# Restore current directory
cd "$OLDDIR"

# Exit using the same code returned from Java
exit $JAVA_EXIT_CODE
