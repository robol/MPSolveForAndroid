#include <jni.h>

/* Header for class it_unipi_dm_mpsolve_android_MainActivity */

#ifndef _Included_it_unipi_dm_mpsolve_android_MainActivity
#define _Included_it_unipi_dm_mpsolve_android_MainActivity
#ifdef __cplusplus
extern "C" {
#endif

/*
 * Class:     it_unipi_dm_mpsolve_android_MainActivity
 * Method:    nativeTest
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_it_unipi_dm_mpsolve_android_PolynomialSolver_nativeTest
  (JNIEnv *, jobject);

JNIEXPORT jobjectArray JNICALL Java_it_unipi_dm_mpsolve_android_PolynomialSolver_nativeSolvePolynomial
  (JNIEnv *, jobject, jstring);

#ifdef __cplusplus
}
#endif
#endif
