#
# SYNOPSIS
#
#   AX_CLASSPATH_ADD([PATH_VAR], [CURRENT_PATH], [NEW_ENTRY])
#
# DESCRIPTION
#
#   Add another entry to $PATH_VAR.  This will canonicalise the entries, and
#   then check for duplicates.
#
#   :PATH_VAR:     The variable, eg. PATH or CLASSPATH
#   :CURRENT_PATH: The current contents of $PATH_VAR
#   :NEW_ENTRY:    The new entry
#
#   :Example:
#
#     CLASSPATH=/tmp
#     AX_CLASSPATH_ADD(CLASSPATH, [/usr/local/java /usr/share/java])
#     # CLASSPATH=/tmp:/usr/local/java:/usr/sjare/java
#
# LICENSE
#
#   Copyright (c) 2015, Berend De Schouwer <bdschouwer@argility.com>
#
#   Copying and distribution of this file, with or without modification, are
#   permitted in any medium without royalty provided the copyright notice
#   and this notice are preserved.
#
AC_DEFUN([AX_CLASSPATH_ADD],[

AC_SUBST(_ax_classpath_add_answer, [$2])
AX_CLASSPATH_CANONICAL(_ax_classpath_add_new, [$3])
#
#  Look for duplicates
#
_ax_classpath_add_found=""
_ax_classpath_add_ifs=$IFS
IFS=$PATH_SEPARATOR
for _ax_classpath_add_p in $_ax_classpath_add_answer; do
  if test "$_ax_classpath_add_p" = "$_ax_classpath_add_new"; then
    _ax_classpath_add_found="Y"
    break
  fi
done
IFS=$_ax_classpath_add_ifs
#
#  If it's not a duplicate, add it.
#  If the current PATH is empty, just return the new value, otherwise return
#  with $PATH_SEPARATOR
#
if test "x$_ax_classpath_add_found" = "xY"; then
  AC_SUBST($1, [$_ax_classpath_add_answer])
else
  if test "x$_ax_classpath_add_answer" = "x"; then
    AC_SUBST($1, [$_ax_classpath_add_new])
  else
    AC_SUBST($1, [${_ax_classpath_add_answer}${PATH_SEPARATOR}${_ax_classpath_add_new}])
  fi
fi

AC_PROVIDE([$0])dnl
])
