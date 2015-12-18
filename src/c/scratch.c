/**
 * @file scratch.c
 *
 * @copyright Copyright 2015 Berend De Schouwer, Public Domain
 *
 * @brief This is an example JNI C library
 *
 * This is an example JNI C Library.  It does the minimum.  It returns the
 * Integer (Java Integer) number 1.
 */

#include <jni.h>
#include "scratch.h"

/**
 * @brief Return the answer 1.
 *
 * This function returns the Java Integer number 1.  It's part of a package.
 *
 * The full Java Class name is represented with underscores.  So
 * \_com\_example\_pkjni means this function is part of the Java package
 * com.example.pkgjni.  \_Scratch indicates the Java Class Scratch, for
 * a full Java name space of com.example.pkgjni.Scratch.
 *
 * \_nativeAnswer matches the method .nativeAnswer().
 *
 * @param env default Java JNI parameter to the VM environment.
 * @param obj Object that calls this function
 * @returns a Java Integer, which might not match the native C int.
 */
JNIEXPORT jint JNICALL Java_com_example_pkgjni_Scratch_nativeAnswer(JNIEnv *env, jobject obj)
{
  jint ret = 0;
#ifdef WORKING
  ret = WORKING;
#else
  ret = -1;
#endif
  return ret;
}
