# Overview

The purpose of this project is to provide core elements for other business registry projects.

# Getting started

Project can be built using either `sbt` installed locally or `bin/sbt` script. First approach will be used in all examples presented here.

## How to build it

To build the project please execute following command:

```
sbt package
```

Above command will create `business-core_2.10-1.0.0-SNAPSHOT.jar` JAR file in `target/scala-2.10` directory.

## How to publish package to local repository

To publish package to local repository please execute following command:

```
sbt publishLocal
```

Above command will copy descriptor and artifacts to `~/.ivy2/local/uk.gov.ons.business-register/business-core_2.10/1.0.0-SNAPSHOT` directory so that other projects can specify this project as a dependency.

## How to use it as a dependency in other projects

To use this project as a dependency in other SBT projects please add following line to `build.sbt` file:

```
libraryDependencies += "uk.gov.ons.business-register" %% "business-core" % "1.0.0-SNAPSHOT"
```

To use this project as a dependency in other Maven projects please add following fragment to `pom.xml` file:

```
<dependency>
  <groupId>uk.gov.ons.business-register</groupId>
  <artifactId>business-core_2.10</artifactId>
  <version>1.0.0-SNAPSHOT</version>
</dependency>
```