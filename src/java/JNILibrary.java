/**
 * @file JNILibrary.java
 *
 * @copyright Copyright 2015 Berend De Schouwer, Public Domain
 */
 package com.example.pkgjni;

import java.util.Properties;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.InputStream;
import java.io.IOException;

/**
 * @brief Load a JNI Library using a sane-defaults policy.
 *
 * This will load a JNI library in Java according to sane defaults, and
 * will change those defaults according to the environment.
 *
 * This does _not_ depend on environment variables like LD_LIBRARY_PATH,
 * but will use them as a last resort.
 *
 * The premise is that Java JNI libraries should not be intermixed with OS
 * libraries, but instead be installed separately according to name space.
 *
 * It should not be required to configure CLASSPATH or LD_LIBRARY_PATH to
 * get a working application.  The application should work by default, and
 * environment and other configuration changes should be the exception.
 *
 * When the package (JAR, RPM, DEB, ZIP, ...) is created, the JNI library path
 * is known, and the default location should be embedded in the package.
 *
 * This will load the settings from most-permanent to least-permanent:
 * - hard coded
 * - jar file
 * - user configuration
 * - CLI parameter
 *
 * Only as a last resort, will the package try LD_LIBRARY_PATH.
 *
 * @see https://www.eclipse.org/jdt/core/r2.0/extension%20dir/ext-dirs.html
 * @see https://fedoraproject.org/wiki/Packaging:Java
 *
 * @warning Because this is an example implementation, all the methods and
 *          members are documented, including the private members that would
 *          normally be omitted.
 */
public abstract class JNILibrary {

    /**
     * @brief Constructor
     *
     * @param aLibraryName  is the JNI Library name.  This is required.
     * @param aLibraryPath  a directory where to load from.  If this is null,
     *                      the directory is searched.
     * @param aPropertyName the property in the property file that specifies
     *                      the location of the library.  This may be null.
     * @param aPackageName  The name of this package.  This is used to
     *                      guess the user .properties filename.  If this is
     *                      null, the filename is taken from the Classs package
     *                      name.
     */
    public JNILibrary(String aLibraryName, String aLibraryPath, String aPropertyName, String aPackageName) {
        this.JNI_LIBRARY_NAME = aLibraryName;
        this.JNI_LIBRARY_SONAME = "lib" + aLibraryName+ ".so";
        this.JNI_LIBRARY_DLLNAME = aLibraryName + ".dll";
        this.PROPERTY_NAME = aPropertyName;
        /**
         * @todo Find out if this gets the packageName() of the abstract class,
         *       or of the implementation.
         */
        /*
         * If the package name was not specified, take the package name
         * of this class.  Java uses '.properties' extension for properties
         * files.  A good place is package+'.properties'
         */
        if ((aPackageName != null) && aPackageName.isEmpty()) {
            String pn = JNILibrary.class.getPackage().getName();
            aPackageName = pn.substring(pn.lastIndexOf('.') + 1);
        }
        this.USER_PROPERTY_FILENAME = "." + aPackageName + ".properties";
        Properties props = new Properties();
        /*
         * If the programmer has a hard-coded path, the programmer must
         * know what the programmer is doing.
         */
        if (aLibraryPath != null) {
            props.putAll(createProperty(aLibraryPath));
        }
        /*
         * We load the JAR properties.  These are decided at compile
         * time, and should point to the build installation.
         */
        props.putAll(getJarProperties());
        /*
         * Then we get user filenames.  This will come from a configuration
         * file specified by the user.
         */
        props.putAll(getUserProperties());
        /*
         * Lastly we get the property used on the commandline, using
         * -DnativeLocations=/over/here
         */
        props.putAll(System.getProperties());
        loadJNILibrary(getSoLoadName(props));
    }

    /**
     * @overload
     * @brief JNILibrary(String, String, String, String) without specifying
     *        the package name.
     */
    public JNILibrary(String aLibraryName, String aLibraryPath, String aPropertyName) {
        this(aLibraryName, aLibraryPath, aPropertyName, null);
    }

    /**
     * @overload
     * @brief JNILibrary(String, String, String) without specifying the
     *        property name.
     */
    public JNILibrary(String aLibraryName, String aLibraryPath) {
        this(aLibraryName, aLibraryPath, "nativeLocations." + aLibraryName);
    }

    /**
     * @overload
     * @brief JNILibrary(String, String) with an empty library path.
     */
    public JNILibrary(String aLibraryName) {
        this(aLibraryName, null, "nativeLocations." + aLibraryName);
    }

    /**
     * @brief The native library name.
     *
     * This is what you normally would pass to loadLibrary().  It's final
     * to prevent loading multiple libraries.
     */
    private final String JNI_LIBRARY_NAME;
    /**
     * @brief The library .so name.  On *NIX systems.  eg. libabc.so.
     */
    private final String JNI_LIBRARY_SONAME;
    /**
     * @brief The library .dll name.  On Win* systems.  eg. abc.dll.
     */
    private final String JNI_LIBRARY_DLLNAME;
    /**
     * @brief The name of the property that has the location of the library.
     *
     * The property name that will be loaded from the Jar's properties, the
     * user's .properties file, or the command line -Dproperty=value.
     *
     * This will be set to 'nativeLocations.library' if it's not specified.
     * You can set this in the properties file to a file or directory,
     * eg. 'nativeLocations.mylibrary=/usr/lib32/'
     */
    private final String PROPERTY_NAME;
    /**
     * @brief The user-specific file where properties are stored.
     */
    private final String USER_PROPERTY_FILENAME;

    /**
     * @brief An extension of System.loadLibrary() with additional error
     * messages.
     *
     * System.load() and System.loadLibrary() load native libraries.  One loads
     * by full filename, and one does a heuristic search.  This is a heuristic
     * to choose the right method based on the provided soname.
     *
     * It extends the stacktrace by adding the full library name and location
     * searched, if known.  This is useful if the JNI library name looks like
     * the Java Class name, which is common.  It prevents ambiguous errors
     * that the Java Class was not found.
     *
     * @param soname The native library short name or full pathname.  If soname
     *               is "" or NULL, the name used in the constructor is used.
     */
    private void loadJNILibrary(String soname) {
        String errorMessage;
        if (soname.isEmpty()) {
            try {
                errorMessage = String.format("loading native library using LD_LIBRARY_PATH=[%s] failed: [%s]",
                                             System.getenv(""), JNI_LIBRARY_NAME);
            } catch (NullPointerException npe) {
                errorMessage = String.format("loading native library using LD_LIBRARY_PATH=[] failed: [%s]",
                                             JNI_LIBRARY_NAME);
            } catch (SecurityException se) {
                errorMessage = String.format("loading native library failed: [%s]",
                                             JNI_LIBRARY_NAME);
            }
        } else {
            errorMessage = String.format("loading native library with full path failed: [%s]",
                                         soname);
        }
        try {
            if (soname.isEmpty()) {
                System.loadLibrary(JNI_LIBRARY_NAME);
            } else {
                System.load(soname);
            }
        } catch (UnsatisfiedLinkError baseException) {
            UnsatisfiedLinkError extendedException = new UnsatisfiedLinkError(errorMessage);
            extendedException.setStackTrace(baseException.getStackTrace());
            throw extendedException;
        }
    }

    /**
     * @overload
     * @brief loadJNILibrary(String) with defaults.
     */
    private void loadJNILibrary() {
        loadJNILibrary(null);
    }

    /**
     * @brief Get the sofilename using the properties system.
     *
     * This will search through the loaded properties, for the property
     * specified, and will search the file or directory for the
     * necessary library.
     *
     * @param   props The properties list to search.
     *
     * @returns The specified path, if the library is found at that location,
     *          or null in the properties are empty, unspecified, or no library
     *          exists at that location.
     */
    private String getSoLoadName(Properties props) {
        String sodir = props.getProperty(PROPERTY_NAME);
        if (sodir == null) {
            return ""; // default to LD_LIBRARY_PATH resolution.
        }
        File f = new File(sodir);
        if (!f.exists()) {
            return "";
        }
        if (f.exists() && !f.isDirectory()) {
            return f.getAbsolutePath();
        }
        String sep = System.getProperty("file.separator");
        String soname = sodir + sep + JNI_LIBRARY_SONAME;
        f = new File(soname);
        if (f.exists() && !f.isDirectory()) {
            return f.getAbsolutePath();
        }
        String dllname = sodir + sep + JNI_LIBRARY_DLLNAME;
        f = new File(dllname);
        if (f.exists() && !f.isDirectory()) {
            return f.getAbsolutePath();
        }
        return "";
    }

    /**
     * @brief Load properties from the JAR file, if found.
     *
     * This will load the JAR file -- either .jar archive or expanded -- for
     * the package specific properties.
     *
     * @returns Properties
     */
    private Properties getJarProperties() {
        Properties props = new Properties();
        InputStream stream = JNILibrary.class.getResourceAsStream("properties");
        try {
            props.load(stream);
        } catch (IOException ignored) {
            // Ignore could not read contents of properties.
        } catch (NullPointerException ignored) {
            // Ignore could not find properties file inside jar.
        }
        return props;
    }

    /**
     * @brief Load package properties from the user's home directory.
     *
     * @returns Properties
     */
    private Properties getUserProperties() {
        Properties props = new Properties();
        String homeDir = System.getProperty("user.home");
        String sep = System.getProperty("file.separator");
        String propFilename = homeDir + sep + USER_PROPERTY_FILENAME;
        try {
            FileInputStream propFile = new FileInputStream(propFilename);
            try {
                props.load(propFile);
            } catch (IOException e) {
                // File might be empty or we might not have permissions.
            }
        } catch (FileNotFoundException ignored) {
            // It's expected that the user does not have a file.
        }
        return props;
    }

    /**
     * @brief Create a property.
     *
     * @returns a single property entry with the values from the constructor.
     */
    private Properties createProperty(String path) {
        Properties props = new Properties();
        props.setProperty(PROPERTY_NAME, path);
        return props;
    }

}
