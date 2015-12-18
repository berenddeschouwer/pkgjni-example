/**
 * @file Scratch.java
 *
 * @copyright Copyright 2015 Berend De Schouwer, Public Domain
 */
package com.example.pkgjni;

/**
 * @brief This is an example Java wrapper around the JNI library
 *
 * This is an example wrapper around the abstract class JNILibrary.
 * It implements a minimalistic loader to load the JNI library 'scratch'
 * which likely has the on-disk filename libscratch.so.
 *
 * JNILibrary will default to doing 'the right thing', so this wrapper is
 * really minimalistic.
 */
public class Scratch extends JNILibrary {

    /**
     * @brief Static variable with the name of the library.  The JVM won't
     * re-load the library, so this is a final String.
     */
    private static final String C_LIBRARY = "scratch";

    /**
     * @brief Default constructor
     */
    public Scratch() {
        this(null);
    }

    /**
     * @brief constructor with a library override.
     *
     * This constructor will attempt to use the library path specified to
     * load the library.  It will prefer the user specified path to any
     * guessed paths.
     *
     * @param libraryPath Filename or directory name to search.  May be null.
     */
    public Scratch(String libraryPath) {
        super(C_LIBRARY, libraryPath);
    }

    /**
     * @brief Call the native C function \_package\_Scratch\_nativeAnswer()
     *
     * Convention uses the word 'native'.  The first two parameter to the
     * C declaration are the JVM environment, and this object.  It's not
     * required to specify them explicitly.
     *
     * @param none
     * @returns Java int, not a native C integer.
     */
    public native int nativeAnswer();

    /**
     * @brief Helper function in case this library is called directly.
     *
     * Helper function to display help in case this library is called directly.
     * The .jar manifest will call this if the jar is accidentally run.
     *
     * @param args an array of string arguments, the command line options.
     * @returns nothing.  Quits.
     */
    public static void main(String[] args) {
        String myName = Scratch.class.getCanonicalName();
        System.out.format("Not an executable: %s\n", myName);
    }

}
