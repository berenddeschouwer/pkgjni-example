# pkgjni-example

## Abstract

This is an example implementation of a Java Package that follows various build
and distribution guidelines.  It includes problematic topics such as finding
a correct CLASSPATH, and loading JNI libraries.

## Description

This example implementation attempts to build a Java Package the Open Source
way.  The specific focus is to load a JNI library using a normal escalation
of location searching, the ability to Unit test against a JNI library in the
build directory, documentation generation and installation, and final package
build.

As specific highlights, it includes
* Autodetection of CLASSPATH
* A JNI library, and autodetection of the JNI location
* Ability to override the JNI location to unit test against the development
  version
* Use autotools to generate normal build files (configure; Makefile)
* Include Pkgconfig
* Include documentation
* Include packaging information (RPM spec file)
* Pass 'make distcheck', which includes the unit tests.

## Idea

Java programs by default need to be configured with a correct CLASSPATH to
load the libraries.  If the Java Package includes a JNI library they
additionally need to be told where to load the JNI library, usually by
modifying LD_LIBRARY_PATH.

If your project requires multiple Java Packages, keeping these variables
correct can be daunting.  They then must be configured for every user.

This is an example of how to autodetect CLASSPATH, and make the project
"just work" without a dependency on any environment variables.

The output is complete.  The package is split into -devel and -doc, and it
includes pkgconfig files so projects that depend on this package can easily
load it.

### CLASSPATH

This project includes m4/ files for autoconf to find the correct CLASSPATH,
and to save the CLASSPATH to Makefiles to build correctly.

### LD_LIBRARY_PATH

The JNI library will be loaded in order of permanence to variable from:
* Hardcoded in the .java source
* The Package JAR file .properties
* The users' .properties in HOME
* Command line properties (java -Dproperty=value)

### Unit Tests

Simple example unit tests are provided.  One for C and a few for Java using
junit.

In addition, the tests are run using normal 'make check'

### pkgconfig

The final files include pkgconfig files, so that project that depend on this
package can find the package easily, add to the CLASSPATH, and create their own
Makefiles using autotools.

### Documentation

Documentation is generated using Doxygen.  It could also be generated using
Javadoc, but this example includes a JNI file, which is written in C, and
hence needs Doxygen.

### Packaging

A sample .spec file for RPM packages is included.

The intent is to include additonal packaging files, like debian/rules.

## Goal

Provide an example implementation of "just works".
