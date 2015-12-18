/**
 * @file scratchTest.c
 *
 * @copyright Copyright 2015 Berend De Schouwer, Public Domain
 *
 * @brief C Native unit test.
 *
 * We can't do much as a unit test here, because all Java JNI libraries
 * expect two parameters: JNIEnv* and jobject.
 *
 * We just include the one for example.
 *
 * Those tests get done in Java.
 */
#include <jni.h>
#include "scratch.h"

int main() {
    jint testAnswer = Java_com_example_pkgjni_Scratch_nativeAnswer(NULL, NULL);
    return (1 == testAnswer ? 0 : 1);
}
