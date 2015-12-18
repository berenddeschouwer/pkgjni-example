Name: pkgjni-example
Version: 1.0.0
Release: 1%{?dist}
License: Example.Com
Group: Development/Libraries
Source: %{name}-%{version}.tar.gz
Summary: JNI Interface to PKG files
BuildRoot: /var/tmp/%{name}-root
Vendor: Example.Com
Requires: glibc >= 2.5
Requires: log4j, json_simple
Requires: java
BuildRequires: gcc, glibc-devel
%if 0%{?el5}
BuildRequires: jdk
%else
BuildRequires: java-devel
%endif
BuildRequires: log4j

%description
Example JNI Interface to show
- autoconf
- rpm package
- javadoc
- junit
- pkgconfig
all working

%package devel
Summary:        Support for compiling pkgjni programs
License:        Argility
Group:          Development/Libraries
%if 0%{?_isa:1}
Requires:       %{name}%{_isa} = %{version}-%{release}
%else
Requires:       %{name} = %{version}-%{release}
%endif

%description devel
This package provides libraries and header files needed for
using the example files

%package doc
Summary:        API documentation pkgjni applications
License:        Argility
Group:          Development/Libraries
%if 0%{?_isa:1}
Requires:       %{name}%{_isa} = %{version}-%{release}
%else
Requires:       %{name} = %{version}-%{release}
%endif

%description doc
API documenation for the example files

%prep
%setup -q -n %{name}-%{version}

%build
#cd %{name}-%{version}
#export CLASSPATH=/usr/share/java/log4j.jar:/usr/share/java/json_simple.jar
%{configure} --libdir=%{_libdir}/%{name}
make

%install
make install DESTDIR=$RPM_BUILD_ROOT

%post
:;

%postun
:;

%files
%defattr(-,root,root)
%{_libdir}/%{name}/libscratch.so*
%{_javadir}/com.example.pkgjni.jar
%{_docdir}/%{name}/COPYING
%{_docdir}/%{name}/README.md
%{_docdir}/%{name}/NEWS

%files devel
%{_libdir}/%{name}/libscratch.a
%{_libdir}/%{name}/libscratch.la
%{_datadir}/pkgconfig/%{name}.pc

%files doc
%{_docdir}/%{name}/html/*

%changelog
* Fri Dec 11 2015 Berend De Schouwer <berend@deschouwer.co.za> 1.0.0-1
- First try
