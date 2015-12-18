c_srcdir = $(top_srcdir)/src/c/
java_srcdir = $(top_srcdir)/src/java/
c_builddir = $(top_builddir)/src/c/
java_builddir = $(top_builddir)/src/java/

#  changed to builddir from topbuilddir/top_builddir
JAVAROOT = $(builddir)
JAVAC = javac
AM_JAVACFLAGS = -implicit:none -sourcepath -Xlint
#CLASSPATH_ENV = CLASSPATH=${JAVAROOT}:.:$$CLASSPATH
CLASSPATH_ENV = CLASSPATH=${JAVAROOT}:$(srcdir)/$(JAVAROOT):$(FOUNDCLASSPATH):$(java_builddir)
javadir = @classdir@

ScratchTest.class: $(dist_java_JAVA)
	$(JAVAC) -cp $(CLASSPATH_ENV) $(AM_JAVACFLAGS) -d . $(srcdir)/ScratchTest.java

TESTS =
EXTRA_DIST =
CLEANFILES =

do_subst = sed -e 's,[@]jnidir[@],$(libdir)/$(PACKAGE),g' \
	       -e 's,[@]CLASSPATH[@],$(CLASSPATH_ENV),g' \
	       -e 's,[@]c_builddir[@],$(c_builddir),g' \
               -e 's,[@]PACKAGE[@],$(PACKAGE),g'

TESTS += $(check_PROGRAMS)
check_PROGRAMS = scratchTest
scratchTest_SOURCES = scratchTest.c $(c_srcdir)/scratch.h
scratchTest_CFLAGS = -I$(c_srcdir)
scratchTest_LDADD = -L$(c_builddir) -lscratch

TESTS += junitTests.sh
EXTRA_DIST += junitTests.in.sh ScratchTest.java
CLEANFILES += junitTests.sh ScratchTest.class
junitTests.sh: junitTests.in.sh fakesofile.so ScratchTest.class
	$(do_subst) < $(srcdir)/junitTests.in.sh > junitTests.sh
	chmod +x junitTests.sh

EXTRA_DIST += fakesofile.in.so
CLEANFILES += fakesofile.so
fakesofile.so: fakesofile.in.so
	$(do_subst) < $(srcdir)/fakesofile.in.so > fakesofile.so
