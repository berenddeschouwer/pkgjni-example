#!/bin/sh

#
#  Wrapper for the Java unit tests.
#
#  We override -DnativeLocations= so that the unit tests run against
#  the built JNI library, and not the installed JNI library.
#

#
#  cd to the builddir because Java classes load in relative paths.
#
cd `dirname ${0}`
exec java -cp @CLASSPATH@ \
	-DnativeLocations.scratch=@c_builddir@/.libs/libscratch.so \
	ScratchTest
