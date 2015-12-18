ACLOCAL_AMFLAGS = -I m4
SUBDIRS = src test doc
EXTRA_DIST = autogen.sh

JAVAROOT = $(topbuilddir)
JAVAC = javac
AM_JAVACFLAGS = . -implicit:none -sourcepath -Xlint
CLASSPATH_ENV = CLASSPATH=${JAVAROOT}:$(srcdir)/$(JAVAROOT):$(FOUNDCLASSPATH)

pkgconfdir = @datadir@/pkgconfig
pkgconf_DATA = pkgjni-example.pc
CLEANFILES = pkgjni-example.pc

extradir = $(docdir)
extra_DATA = COPYING NEWS README.md
EXTRA_DIST += pkgjni.spec README.md
