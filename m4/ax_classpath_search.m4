#
# SYNOPSIS
#
#   AX_CLASSPATH_SEARCH([NEW_PATH],
#                       [CURRENT_PATH],
#                       [SEARCH_PATH],
#                       [JAR LIST])
#
# DESCRIPTION
#
#   Search and edit $CLASSPATH for specific .jar files.
#
#   CLASSPATH is the Java VM and compiler search path for finding
#   classes.  This PATH is often incomplete and custom set up.  It's autoconf's
#   job to fill in the blanks.
#
#   AX_CLASSPATH_SEARCH will search through a path list, and a list of search
#   directories for specified jar files, and will return a PATH that matches.
#
#   Valid CLASSPATH includes directories and .jar files.  These files are
#   often stored in OS-wide /usr/share/java/; but the .jar file might be
#   /usr/share/java/log4j.jar or /usr/share/java/log4j/log4j.jar.
#
#   The existing CURRENT_PATH will be appended to the end of the list.  That
#   way any specific user additions are preserved.
#
#   '.' will be appended to the list.  This is important for other Java
#   autoconf macros, for example AC_JAVA_RQRD_CLASS()
#
#   Duplicates (another common problem) are stripped.
#
#   :NEW_PATH:     Where to put the path (try CLASSPATH)
#   :CURRENT_PATH: expanded existing path (try $CLASSPATH)
#   :SEARCH_PATH:  whitespace separated list of directories
#                  try /usr/share/java and /usr/local/share/java
#   :JAR LIST:     whitespace separated list of .jar files
#
#   :Example:
#
#      AX_CLASSPATH_SEARCH(CLASSPATH,
#                          [$CLASSPATH],
#                          [/usr/share/java /usr/local/share/java],
#                          [log4j json_simple])
#
# LICENSE
#
#   Copyright (c) 2015, Berend De Schouwer <berend.de.schouwer@gmail.com>
#
#   Copying and distribution of this file, with or without modification, are
#   permitted in any medium without royalty provided the copyright notice
#   and this notice are preserved.
#
AC_DEFUN([AX_CLASSPATH_SEARCH],[

#
#  Add the original $PATH.  This will remove duplicates, but keep the order.
#
_ax_classpath_search_tmp=""
_ax_classpath_search_ifs=$IFS
IFS=$PATH_SEPARATOR
for _p in $2; do
  AX_CLASSPATH_ADD(_ax_classpath_search_tmp, [$_ax_classpath_search_tmp], [$_p])
done
#
#  Add the directory list.
#
for _p in $3; do
  AX_CLASSPATH_ADD(_ax_classpath_search_tmp, [$_ax_classpath_search_tmp], [$_p])
done
#
#  Now we loop over the entries and look for $ENTRY.jar, $ENTRY/$ENTRY.jar
#  and $ENTRY
#
_ax_classpath_search_answer=""
IFS=$_ax_classpath_search_ifs
for _jar in $4; do
  _ax_classpath_search_found=""
  _ax_classpath_search_ifs=$IFS
  IFS=$PATH_SEPARATOR
  for _p in $_ax_classpath_search_tmp; do
    if test "`basename ${_p}`" = "${_jar}.jar"; then
      if test -f "${_p}"; then
        AX_CLASSPATH_ADD(_ax_classpath_search_answer,
                         [$_ax_classpath_search_answer],
                         [$_p])
        _ax_classpath_search_found="Y"
        break
      fi
    fi
    if test -d "${_p}"; then
      if test -f "${_p}/${_jar}/${_jar}.jar"; then
        AX_CLASSPATH_ADD(_ax_classpath_search_answer,
                         [$_ax_classpath_search_answer],
                         ["${_p}/${_jar}/${_jar}.jar"])
        _ax_classpath_search_found="Y"
        break
      fi
      if test -f "${_p}/${_jar}.jar"; then
        AX_CLASSPATH_ADD(_ax_classpath_search_answer,
                         [$_ax_classpath_search_answer],
                         ["${_p}/${_jar}.jar"])
        _ax_classpath_search_found="Y"
        break
      fi
      if test -d "${_p}/${_jar}"; then
        AX_CLASSPATH_ADD(_ax_classpath_search_answer,
                         [$_ax_classpath_search_answer],
                         ["$_p/$_jar"])
        _ax_classpath_search_found="Y"
        break
      fi
    fi
  done
  IFS=$_ax_classpath_search_ifs
  if test "x${_ax_classpath_search_found}" != "xY"; then
    AC_MSG_ERROR([Add $_jar to \$CLASSPATH])
  fi
done
#
#  We need to add '.' for other macros like AC_JAVA_RQRD_CLASS()
#
AX_CLASSPATH_ADD(_ax_classpath_search_answer,
                 [$_ax_classpath_search_answer],
                 [.])
if ! test "x$2" = "x"; then
  if test "x$_ax_classpath_search_answer" = "x"; then
    _ax_classpath_search_answer=$2
  else
    _ax_classpath_search_answer="$_ax_classpath_search_answer""$PATH_SEPARATOR""$2"
  fi
fi
AC_SUBST($1, $_ax_classpath_search_answer)

AC_PROVIDE([$0])dnl
])
