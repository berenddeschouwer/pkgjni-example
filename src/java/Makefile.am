#  changed to builddir from topbuilddir/top_builddir
JAVAROOT = $(builddir)
JAVAC = javac
AM_JAVACFLAGS = -implicit:none -sourcepath -Xlint
#CLASSPATH_ENV = CLASSPATH=${JAVAROOT}:.:$$CLASSPATH
CLASSPATH_ENV = CLASSPATH=${JAVAROOT}:$(srcdir)/$(JAVAROOT):$(FOUNDCLASSPATH)
javadir = @classdir@
java_DATA = com.example.pkgjni.jar

dist_java_JAVA = Scratch.java JNILibrary.java
dist_java_CLASS = com/example/pkgjni/Scratch.class com/example/pkgjni/JNILibrary.class
EXTRA_DIST =
CLEANFILES =

do_subst = sed -e 's,[@]jnidir[@],$(libdir)/$(PACKAGE),g' \
	       -e 's,[@]CLASSPATH[@],$(CLASSPATH_ENV),g' \
               -e 's,[@]PACKAGE[@],$(PACKAGE),g'

# added this, so make will stop if it's not there
com/example/pkgjni/Scratch.class: $(dist_java_JAVA)
	$(JAVAC) -cp $(CLASSPATH_ENV) $(AM_JAVACFLAGS) -d $(srcdir) Scratch.java
com/example/pkgjni/JNILibrary.class: $(dist_java_JAVA)
	$(JAVAC) -cp $(CLASSPATH_ENV) $(AM_JAVACFLAGS) -d $(srcdir) JNILibrary.java

EXTRA_DIST += com/example/pkgjni/properties.in
CLEANFILES += com/example/pkgjni/properties
com/example/pkgjni/properties: com/example/pkgjni/properties.in
	$(do_subst) < $(srcdir)/com/example/pkgjni/properties.in > com/example/pkgjni/properties

EXTRA_DIST += manifest.in
CLEANFILES += manifest
manifest: manifest.in
	$(do_subst) < $(srcdir)/manifest.in > manifest

# made it dependent on $(dist_java_CLASS) and manifest
# made it _tmp.jar && mv so make fails if jar cfm fails
com.example.pkgjni.jar: $(dist_java_CLASS) manifest com/example/pkgjni/properties
	jar cfm com.example.pkgjni_tmp.jar manifest \
	    $(dist_java_CLASS) com/example/pkgjni/properties
	mv com.example.pkgjni_tmp.jar com.example.pkgjni.jar

clean-local:
	-rm -f $(dist_java_CLASS) com.example.pkgjni.jar com.example.pkgjni.jar
