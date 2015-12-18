#
#  Generated from autoscan
#
#  may be necessary to limit autoconf version
AC_PREREQ([2.69])
#
#  Program, bug  report
#
AC_INIT([pkgjni-example], [1.0.0], [https://example.com/])
#
#  Allow for --enable-debug
#
AX_CHECK_ENABLE_DEBUG()
#
#  Source code
#
AC_CONFIG_AUX_DIR(config)
AC_CONFIG_SRCDIR(src)
AC_CONFIG_HEADERS([config.h])
AC_CONFIG_MACRO_DIR([m4])
#
#  Build a makefile
#
AM_INIT_AUTOMAKE
#([-Wall -Werror foreign])
AM_PROG_AR
LT_INIT
#
#  We need a C compiler, and install.
#
AC_PROG_CC
AM_PROG_CC_C_O
AC_PROG_CXX
AC_PROG_INSTALL
#
#  Data types
#
AC_TYPE_SIZE_T
AC_CHECK_SIZEOF(int *)

PKG_PROG_PKG_CONFIG([0.21])
PKG_CHECK_MODULES([PKGCONFIG], [pkg-config])
#
AC_CHECK_PROGS([DOXYGEN], [doxygen])
if test -z "$DOXYGEN";
   then AC_MSG_WARN([Doxygen not found - continuing without Doxygen support])
fi
#
#  We need Java Compiler
#
AC_CHECK_CLASSPATH
AX_PROG_JAVAC
AX_PROG_JAVA
#
#  We need Java JNI
#
AX_JNI_INCLUDE_DIR
for JNI_INCLUDE_DIR in $JNI_INCLUDE_DIRS
do
    CPPFLAGS="$CPPFLAGS -I$JNI_INCLUDE_DIR"
done
#
#  We need a Java packager
#
AX_PROG_JAR
#
#AC_CHECK_SIZEOF([long])
#
#  We don't need that many libraries... just stdlib.h
#
AC_CHECK_HEADERS([fcntl.h stddef.h])
AC_CHECK_HEADER([stdlib.h])
AC_CHECK_HEADER([math.h])
AC_CHECK_HEADER([libgen.h])
AC_CHECK_HEADER(limits.h,
                AC_DEFINE(HAVE_LIMITS_H, 1,
                          [Define if you have the <limits.h> header file.]))
AC_CHECK_HEADER(stdarg.h,
                AC_DEFINE(HAVE_STDARG_H, 1,
                          [Define if you have the <stdarg.h> header file.]))
#
#  Some glibc functions...
#
AC_FUNC_MALLOC
AC_FUNC_REALLOC
AC_FUNC_FORK
AC_CHECK_FUNCS([memchr memset strchr calloc])
AC_CHECK_FUNCS([getcwd strdup])
AC_CHECK_FUNCS([tell])
AC_CHECK_FUNCS([gettimeofday])

#
#  OS specific
#
AC_CANONICAL_HOST
case $host_os in
    linux*)
        CFLAGS+=" "
        ;;
    *)
        AC_MSG_ERROR([Your platform is not currently supported])
        ;;
esac
#
#  We need Curses to draw the boxes.
#
AC_SEARCH_LIBS(tgetent, [tinfo termcap ncurses curses])
AC_SEARCH_LIBS(wmove, [ncurses curses])
AC_SEARCH_LIBS(floor, [m])
#
#  We need ctree.  Two different versions (multi-user, standalone)
#
AC_CHECK_LIB(ctmulib, CREIDX, AC_SUBST(CTREEMU_LIB, -lctmulib))
AC_CHECK_LIB(ctalib, CREIDX, AC_SUBST(CTREEA_LIB, -lctalib))
#
#  And we need Optech Sort
#
AC_CHECK_LIB(otsort, s_1mains, AC_SUBST(OPTECH_LIB, -lotsort), AC_MSG_ERROR(We need Optech sort))

#
#  Search the classpath for required JARs.
#  hamcrest/all is required by junit.
#
AX_CLASSPATH_SEARCH(CLASSPATH,
                    [$CLASSPATH],
                    [/usr/share/java /usr/local/share/java],
                    [log4j junit hamcrest/all])
AC_SUBST(FOUNDCLASSPATH, $CLASSPATH)
#
#  Specific classes
#
#  Before we do this, we must add . to CLASSPATH.  This is a requirement
#  of AX_CHECK_RQRD_CLASS
#
export CLASSPATH
# Log4j v2 API, we can test for LogManager, but not Logger
AX_CHECK_RQRD_CLASS(org.apache.log4j.LogManager)
AX_CHECK_RQRD_CLASS(org.junit.Test)
#
#  Where do we put the jar/class files we generate?
#
AC_ARG_WITH([classdir],
  [AS_HELP_STRING([--with-classdir=DIR], [java class/jar files])],
  [classdir=$withval],
  [classdir="\${prefix}/share/java/"])
AC_SUBST([classdir], [$classdir])

#
#  And we generate these...
#
AM_CONDITIONAL([HAVE_DOXYGEN],
               [test -n "$DOXYGEN"])
AM_COND_IF([HAVE_DOXYGEN],
           [AC_CONFIG_FILES([doc/doxygen.conf doc/Makefile])])
AC_CONFIG_FILES(Makefile
                src/Makefile src/c/Makefile src/java/Makefile test/Makefile
                pkgjni-example.pc)
AC_OUTPUT

