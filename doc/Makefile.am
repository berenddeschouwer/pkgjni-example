if HAVE_DOXYGEN

docpkg = $(PACKAGE_NAME)-doxy-$(PACKAGE_VERSION).tar.gz

doc_DATA = $(docpkg)

$(docpkg): doxygen.stamp
	tar chof - html | gzip -9 -c >$@

doxygen.stamp: doxygen.conf $(top_srcdir)/src/c/*.c \
			    $(top_srcdir)/src/java/*.java \
			    $(top_srcdir)/test/*.c \
			    $(top_srcdir)/test/*.java
	$(DOXYGEN) $(DOXYFLAGS) $<
	echo Timestamp >$@

CLEANFILES = doxygen.stamp $(docpkg)

install-data-hook:
	cd $(DESTDIR)$(docdir) && tar xf $(docpkg)
	cd $(DESTDIR)$(docdir) && rm $(docpkg)

uninstall-hook:
	cd $(DESTDIR)$(docdir) && rm -rf html/

clean-local:
	rm -rf html/

endif # HAVE_DOXYGEN
