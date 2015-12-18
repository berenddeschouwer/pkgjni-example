#
# SYNOPSIS
#
#   AX_CLASSPATH_CANONICAL([ANSWER], [FILE/PATH])
#
# DESCRIPTION
#
#   Make the filename canonical.  Strip out /../ and duplicate //
#
#   :ANSWER: the variable name to store the answer in.
#   :FILE/PATH: the file or directory to canonicalise
#
#   It assumes the directory separator is /.  This seems to be consistent
#   with Autoconf.
#
#   :Example:
#
#     AX_CLASSPATH_CANONICAL(NEW, [///usr////local/share/])
#     # NEW=/usr/local/share
#
#   This needs 'basename', 'dirname' and 'grep'; however autoconf always
#   provides those, so we don't need AC_CHECK_PROG()
#
# LICENSE
#
#   Copyright (c) 2015, Berend De Schouwer <berend.de.schouwer@gmail.com>
#
#   Copying and distribution of this file, with or without modification, are
#   permitted in any medium without royalty provided the copyright notice
#   and this notice are preserved.
#
AC_DEFUN([AX_CLASSPATH_CANONICAL],[

#
#  Check if the new path has $PATHSEP in it's name. If it does not,
#  then it's '.' or relative to here, so canonical=$this
#
echo "${2}" | grep -q /
if ! test $?; then
  AC_SUBST($1, [$2])
else

  #
  #  We use dirname and basename to strip out duplicates, and try and
  #  resolve ./; ../; etc.
  #
  _ax_classpath_canonical_dir=`dirname $2`
  if test "$_ax_classpath_canonical_dir" = "/"; then
    _ax_classpath_canonical_dir=""
  fi
  _ax_classpath_canonical_file=`basename $2`
  if test "$_ax_classpath_canonical_file" = "."; then
    AC_SUBST($1, [$2])
  else
    AC_SUBST($1, [$_ax_classpath_canonical_dir/$_ax_classpath_canonical_file])
  fi

fi

AC_PROVIDE([$0])dnl
])
